import argparse
import os
import yaml
import re
import base64
import threading
import queue
import time


ST_REGEX_DICT = {
    'app_name': re.compile(r'\s*name\s*:\s*\"(?P<app_name>.*)\"\s*', re.IGNORECASE),
    'author_name': re.compile(r'\s*author\s*:\s*\"(?P<author_name>.*)\"\s*', re.IGNORECASE),
    'author_namespace': re.compile(r'\s*namespace\s*:\s*\"(?P<author_namespace>.*)\"\s*', re.IGNORECASE),
    'rdlm_uri': re.compile(r'\s*def\s*uri\s*\=\s*\"(?P<rdlm_uri>.*)\"\s*', re.IGNORECASE),
    'rdlm_http_auth': re.compile(r'\s*headers\.put\s*\(\s*\"Authorization\s*\"\s*\,\s*\"\s*Basic\s*(?P<rdlm_http_auth>.*)\"\s*', re.IGNORECASE),
    'rdlm_lock_path': re.compile(r'\s*def\s*rdlm_lock_path\s*\=\s*\"(?P<rdlm_lock_path>.*)\"\s*', re.IGNORECASE),
    'rdlm_lock_lifetime': re.compile(r'\s*lifetime\s*:\s*(?P<rdlm_lock_lifetime>.*),\s*', re.IGNORECASE),
    'instrumented_smartapps_list_section': re.compile(r'\s*(?P<instrumented_smartapps_list_section>//@{INSTRUMENTED_SMARTAPPS_LIST_SECTION})\s*', re.IGNORECASE),
    'initializing_policies_section': re.compile(r'\s*(?P<initializing_policies_section>//@{INITIALIZING_POLICIES_SECTION})\s*', re.IGNORECASE),
    'encoded_policies_section': re.compile(r'\s*(?P<encoded_policies_section>//@{ENCODED_POLICIES_SECTION})\s*', re.IGNORECASE),
    'policies_permission_section': re.compile(r'\s*(?P<policies_permission_section>//@{POLICIES_PERMISSION_SECTION})\s*', re.IGNORECASE),
    'smartapp_definition': re.compile(r'\s*.*definition\s*\(\s*(?P<smartapp_definition>(.|\n)*)\s*\)\s*preferences(.|\n)*', re.IGNORECASE),
}

OH_REGEX_DICT = {
    'policies_data_structure_section': re.compile(r'\s*(?P<policies_data_structure_section>//@{POLICIES_DATA_STRUCTURE_SECTION})\s*', re.IGNORECASE),
    'policy_section': re.compile(r'\s*(?P<policy_section>//@{POLICY_SECTION})\s*', re.IGNORECASE),
    'policy_declaration_section': re.compile(r'\s*(?P<policy_declaration_section>//@{POLICY_DECLARATION_SECTION})\s*', re.IGNORECASE),
    'policies_permission_section': re.compile(r'\s*(?P<policies_permission_section>//@{POLICIES_PERMISSION_SECTION})\s*', re.IGNORECASE),
    'rule': re.compile(r'rule\s+\"(?P<rule_name>.*)\"\s*', re.IGNORECASE),
    'trigger': re.compile(
        r'\s*item\s+(?P<item_name>.[^\s]*)\s+(received)?\s*(?P<cmd_type>(command|update|changed))\s*(from)?\s*(.[^\s]*)?\s*(to)?\s*(?P<cmd>.[^\s]*)?\s*',
        re.IGNORECASE),
    'then': re.compile(r'\s*then\s*', re.IGNORECASE),
    'action': re.compile(
        r'\s*(?P<action_item>.[^\s]*)\s*\.\s*(sendCommand|postUpdate)\s*\(\s*(?P<action_cmd>.[^\s]*)\s*\)\s*',
        re.IGNORECASE),
}

EVA_REGEX_DICT = {
    'pickled_data_path': re.compile(r'\s*(?P<pickled_data_path>//@{PICKLED_DATA_PATH})\s*', re.IGNORECASE),
    'pickled_data_section': re.compile(r'\s*(?P<pickled_data_section>//@{PICKLED_DATA_SECTION})\s*', re.IGNORECASE),
    'encoded_policy_section': re.compile(r'\s*(?P<encoded_policy_section>//@{ENCODED_POLICIES_SECTION})\s*', re.IGNORECASE),
    'pdp_body_permission_section': re.compile(r'\s*(?P<pdp_body_permission_section>//@{PDP_BODY_PERMISSION_SECTION})\s*', re.IGNORECASE),
    'action': re.compile(
        r'\s*action\s*\(\s*\'(?P<action_item>.[^\s]*)\'\s*(,\s*status\s*=\s*(?P<status>\d)?\s*)?(,\s*value\s*=\s*(?P<value>\d)?\s*)?\)\s*',
        re.IGNORECASE),
    'action_toggle': re.compile(
        r'\s*action_toggle\s*\(\s*\'(?P<action_item>.[^\s]*)\'\s*(,\s*value\s*=\s*(?P<value>\d)?\s*)?\)\s*',
        re.IGNORECASE),
}


def os_cmd(cmd, q):
    os_out = os.system(cmd)
    q.put(os_out)


def check_file(file_name):
    if os.path.exists(file_name):
        return True
    else:
        print("The file, %s, does not exist!" % file_name)
        return False


def setup_args():
    arg_parser = argparse.ArgumentParser(description="PATRIOT - Policy Assisted Threat-Resilient Internet of Things")
    arg_parser.add_argument('target', choices=['st', 'oh', 'eva'])
    arg_parser.add_argument('task', choices=['analysis', 'instrument'])
    arg_parser.add_argument("policy", help="The input file containing user policies.")
    arg_parser.add_argument("app_folder", help="The folder path containing automation units.", nargs='?')
    arg_parser.add_argument("inst_app_folder",
                            help="The folder path containing instrumented version of automation units.",
                            nargs='?')
    args = arg_parser.parse_args()
    return args


def get_config():
    with open("conf/conf.yml", 'r') as ymlfile:
        return yaml.load(ymlfile, Loader=yaml.FullLoader)


def get_base64_auth_string(username, password):
    return base64.b64encode(('%s:%s' % (username, password)).encode()).decode()


def get_child_app_encoding(app_names):
    conf = get_config()
    encoded_string = "\n"
    for app in app_names:
        encoded_string += '\t\t\tapp(name: "ChildApp", appName: "' + app['app_name'] + '", namespace: "' + app['author_namespace'] + '", title: "New Child App", defaultValue: "' + app['app_name'] + '")\n'
    return encoded_string


def parse_line(line, regex_dict):
    for key, rx in regex_dict.items():
        match = rx.search(line)
        if match:
            return key, match
    return None, None


def create_st_policy_manager(file_name, inst_file_path, app_names, policies):
    if check_file(file_name):
        conf = get_config()
        with open(file_name, 'r') as template_file:
            with open(os.path.join(inst_file_path, os.path.splitext(os.path.basename(file_name))[0] + ".groovy"), 'w+') as tmp_file:
                line = template_file.readline()
                while line:
                    key, match = parse_line(line, ST_REGEX_DICT)
                    if key == 'app_name':
                        tmp_file.write(line.replace(match.group('app_name'),
                                                    conf['smart_things']['policy_manager_appname']))
                    elif key == 'author_name':
                        tmp_file.write(line.replace(match.group('author_name'),
                                                    conf['smart_things']['author_name']))
                    elif key == 'author_namespace':
                        tmp_file.write(line.replace(match.group('author_namespace'),
                                                    conf['smart_things']['author_namespace']))
                    elif key == 'rdlm_uri':
                        tmp_file.write(line.replace(match.group('rdlm_uri'),
                                                    conf['smart_things']['rdlm_uri']))
                    elif key == 'rdlm_http_auth':
                        tmp_file.write(line.replace(match.group('rdlm_http_auth'),
                                                    get_base64_auth_string(conf['smart_things']['rdlm_http_auth']['username'],
                                                    conf['smart_things']['rdlm_http_auth']['password'])))
                    elif key == 'rdlm_lock_path':
                        tmp_file.write(line.replace(match.group('rdlm_lock_path'),
                                                    conf['smart_things']['rdlm_lock_path']))
                    elif key == 'rdlm_lock_lifetime':
                        tmp_file.write(line.replace(match.group('rdlm_lock_lifetime'),
                                                    str(conf['smart_things']['rdlm_lock_lifetime'])))
                    elif key == 'instrumented_smartapps_list_section':
                        tmp_file.write(line.replace(match.group('instrumented_smartapps_list_section'),
                                                    get_child_app_encoding(app_names)))
                    elif key == 'initializing_policies_section':
                        tmp_file.write(line.replace(match.group('initializing_policies_section'),
                                                    get_st_encoded_policy_initialization(policies)))
                    elif key == 'encoded_policies_section':
                        tmp_file.write(line.replace(match.group('encoded_policies_section'),
                                                    get_st_encoded_policy_function(policies)))
                    elif key == 'policies_permission_section':
                        tmp_file.write(line.replace(match.group('policies_permission_section'),
                                                    get_st_encoded_policy_permission(policies)))
                    else:
                        tmp_file.write(line)
                    line = template_file.readline()


def preprocess_st_smartapps(source_path, destination_path):
    apps = []
    for smartapp in os.listdir(source_path):
        if smartapp.endswith(".groovy"):
            source = ""
            with open(os.path.join(source_path, smartapp), 'r') as smartapp_file:
                source = smartapp_file.read()
            source += "\n\ndef getChildAppDevices() {\n\treturn settings\n}"
            match = ST_REGEX_DICT['smartapp_definition'].search(source)
            if match:
                app = {}
                definition = match.group('smartapp_definition')
                modified_def = ""
                for line in definition.splitlines():
                    key, line_match = parse_line(line, ST_REGEX_DICT)
                    if key == 'app_name':
                        app['app_name'] = line_match.group('app_name')
                        modified_def += line + '\n'
                    elif key == 'author_name':
                        app['author_name'] = line_match.group('author_name')
                        modified_def += line + '\n'
                    elif key == 'author_namespace':
                        app['author_namespace'] = line_match.group('author_namespace')
                        modified_def += line + '\n'
                    else:
                        modified_def += line + '\n'
                apps.append(app)
                with open(os.path.join(destination_path, smartapp), 'w+') as prep_smartapp_file:
                    prep_smartapp_file.write(source.replace(definition, modified_def))
        else:
            continue
    return apps


def guard_smartapps_actions(source_path, destination_path, action_list_path, log_path):
    cmd = 'groovy simulation/smartthings/res/instrumentor.groovy ' + action_list_path + ' ' + source_path + ' ' + destination_path + ' ' + '> ' + log_path + ' 2>&1'
    res = queue.Queue()
    task1 = threading.Thread(target=os_cmd,
                             args=(cmd, res))
    task1.start()
    print('please wait... ')
    task1.join()

def get_st_encoded_policy_initialization(policies):
    decl_string = ""
    for policy in policies:
        decl_string += "\tdef " + str(policy.name) + " = [\n\t\t\tprv: " + str(policy.prv).lower() + ",\n"
        decl_string += "\t\t\tcur: " + str(policy.cur).lower() + ",\n"
        decl_string += "\t\t\ttss: " + str(policy.tss).replace('{', '[').replace('}', ']').replace("'", "").replace('None', 'null').replace('[]', '[:]') + ",\n"
        decl_string += "\t\t\tidx: " + str(policy.idx) + "\n\t]"
        decl_string += "\n"
    decl_string += "\tdef myPolicies = [:]\n"
    for policy in policies:
        decl_string += '\tmyPolicies.put("{}", {})\n'.format(policy.name, policy.name)
    decl_string += "\tatomicState.policies = myPolicies"
    return decl_string


def get_st_encoded_policy_permission(policies):
    argument = "(myDevices, automation_unit, evt, action_device, action_command, action_command_arg)"
    permission = "\t\tpermission =\n"
    for policy in policies:
        permission += "\t\t\t{}{} &&\n".format(policy.name, argument)
    return permission[:-3]


def get_st_encoded_policy_function(policies):
    device_type = {}
    cap_attribute = {}
    with open('simulation/smartthings/res/device_type.csv', 'r') as dfile:
        devs = dfile.readlines()
        for dev in devs:
            x = dev.split(',')
            device_type[x[0].strip()] = x[1].strip()
    with open('simulation/smartthings/res/cap_attribute.csv', 'r') as cfile:
        caps = cfile.readlines()
        for cap in caps:
            if ',' in cap:
                x = cap.strip().split(',')
                cap_attribute[x[0].strip()] = x[1].strip()

    signature = "(myDevices, automation_unit, evt, action_device, action_command, action_command_arg)"
    functions = ""
    for policy in policies:
        functions += "def {}{} {}\n".format(policy.name, signature, "{")
        functions += "\tdef current_time = (long) (now()/1000)\n"
        functions += "\tdef current_date = current_time\n"
        functions += "\tdef myPolicies = atomicState.policies\n"
        functions += '\tdef {} = myPolicies.get("{}")\n'.format(policy.name, policy.name)
        functions += '\t{}.put("idx", {}.get("idx") + 1)\n'.format(policy.name, policy.name)
        functions += '\tdef idx = {}.get("idx")\n'.format(policy.name)
        functions += '\tdef cur = {}.get("cur")\n'.format(policy.name)
        functions += '\tdef prv = {}.get("prv")\n'.format(policy.name)
        functions += '\tdef tss = {}.get("tss")\n'.format(policy.name)
        i = 0
        for subformula in policy.enc:
            functions += '\tcur[{}] = '.format(i)
            if subformula.type == 'attribute':
                if subformula.children[0] in ['automation_unit', 'current_time', 'current_date']:
                    functions += subformula.children[0] + ' '
                    functions += subformula.value.replace('=', '==') + ' '
                    value = ""
                    if subformula.children[1].type == 'number':
                        value = subformula.children[1].value
                    elif subformula.children[1].type == 'boolean':
                        value = subformula.children[1].value.lower()
                    elif subformula.children[1].type == 'string': # needed for date and time
                        value = '"{}"'.format(subformula.children[1].value)
                    elif subformula.children[1].type == 'time':
                        value = '((long) (timeToday("{}", location.timeZone).getTime()/1000))'.format(subformula.children[1].value)
                    elif subformula.children[1].type == 'date':
                        value = "((long) (Date.parse('MM-dd-yyyy', '{}').getTime()/1000))".format(subformula.children[1].value)
                    functions += value + '\n'
                else:
                    functions += "myDevices.get('{}').currentValue('{}') ".format(subformula.children[0].split('#')[1], cap_attribute[device_type[subformula.children[0].split('#')[1]]])
                    functions += subformula.value.replace('=', '==') + ' '
                    value = ""
                    if subformula.children[1].type == 'number':
                        value = subformula.children[1].value
                    elif subformula.children[1].type == 'boolean':
                        value = subformula.children[1].value.lower()
                    elif subformula.children[1].type == 'string':  # needed for date and time
                        value = '"{}"'.format(subformula.children[1].value)
                    elif subformula.children[1].type == 'time':
                        value = '((long) (timeToday("{}", location.timeZone).getTime()/1000))'.format(
                            subformula.children[1].value)
                    elif subformula.children[1].type == 'date':
                        value = "((long) (Date.parse('MM-dd-yyyy', '{}').getTime()/1000))".format(
                            subformula.children[1].value)
                    functions += value + '\n'
            elif subformula.type in ['not_prop', 'not_mtl']:
                functions += "!cur[{}]\n".format(i-1)
            elif subformula.type == 'everything':
                functions += "true\n"
            elif subformula.type in ['bexp_prop', 'bexp_prop_mlt', 'bexp_mlt_prop', 'bexp_mlt', 'bexp_act']:
                functions += "cur[{}] ".format(i - int(subformula.children[0]))
                functions += subformula.value.replace('and', '&&').replace('or', '||') + ' '
                functions += "cur[{}]\n".format(i - 1)
            elif subformula.type == 'mlt_lastly_interval':
                interval = subformula.value.split(':')
                functions += "lastly(cur[{}], prv[{}], tss, '{}', {}, {}, current_time, true)\n".format(i-1, i-1, i, interval[0], interval[1])
            elif subformula.type == 'mlt_lastly':
                functions += "lastly(cur[{}], prv[{}], tss, '{}', 0, 0, current_time, false)\n".format(i-1, i-1, i)
            elif subformula.type == 'mlt_once_interval':
                interval = subformula.value.split(':')
                functions += "once(cur[{}], tss, '{}', {}, {}, current_time, idx, true)\n".format(i-1, i, interval[0], interval[1])
            elif subformula.type == 'mlt_once':
                functions += "once(cur[{}], tss, '{}', 0, 0, current_time, idx, false)\n".format(i-1, i)
            elif subformula.type == 'mlt_since_interval':
                interval = subformula.value.split(':')
                functions += "since(cur[{}], cur[{}], tss, '{}', {}, {}, current_time, idx, true)\n".format(i - int(subformula.children[0]), i-1, i, interval[0], interval[1])
            elif subformula.type == 'mlt_since':
                functions += "since(cur[{}], cur[{}], tss, '{}', 0, 0, current_time, idx, false)\n".format(i - int(subformula.children[0]), i-1, i)
            elif subformula.type == 'action':
                functions += subformula.children[0] + ' '
                functions += subformula.value.replace('=', '==') + ' '
                functions += '"{}"\n'.format(subformula.children[1])
            elif subformula.type == 'action_arg':
                functions += subformula.children[0] + ' '
                functions += subformula.value.replace('=', '==') + ' '
                value = ""
                if subformula.children[1].type == 'number':
                    value = subformula.children[1].value
                elif subformula.children[1].type == 'boolean':
                    value = subformula.children[1].value.lower()
                elif subformula.children[1].type == 'string':  # needed for date and time
                    value = '"{}"'.format(subformula.children[1].value)
                elif subformula.children[1].type == 'time':
                    value = '((long) (timeToday("{}", location.timeZone).getTime()/1000))'.format(
                        subformula.children[1].value)
                elif subformula.children[1].type == 'date':
                    value = "((long) (Date.parse('MM-dd-yyyy', '{}').getTime()/1000))".format(
                        subformula.children[1].value)
                functions += value + '\n'
            elif subformula.type == 'implies':
                functions += "implies(cur[{}], cur[{}])\n".format(i - 1, i - int(subformula.children[0]))
            i += 1

        functions += '\tdef res = {}.get("cur")[{}]\n'.format(policy.name, i - 1)
        functions += '\t{}.put("prv", {}.get("cur"))\n'.format(policy.name, policy.name)
        functions += '\tatomicState.policies = myPolicies\n'
        functions += '\treturn res\n'
        functions += "}\n\n"
    return functions[:-2]


def get_oh_encoded_policies_initialization(policies):
    decl_string = 'val HashMap<String, HashMap<String, Object>> policies = newHashMap(\n'
    i = 1
    for policy in policies:
        decl_string += '\t\t"policy{}" -> newHashMap(\n'.format(i)
        decl_string += '\t\t\t\t"history" ->  newHashMap(\n'
        decl_string += '\t\t\t\t\t\t"prv" ->  newArrayList{},\n'.format(str(policy.prv).lower().replace('[', '(').replace(']', ')'))
        decl_string += '\t\t\t\t\t\t"cur" ->  newArrayList{}\n'.format(str(policy.cur).lower().replace('[', '(').replace(']', ')'))
        decl_string += '\t\t\t\t\t\t),\n'
        t1 = {}
        t2 = {}
        for key, value in policy.tss.items():
            for k, v in value.items():
                if k == 'q':
                    t1[key] = {k: v}
                else:
                    t2[key] = {k: v}

        decl_string += '\t\t\t\t"timestamp1" -> {},\n'.format(str(t1).replace(':', ' ->').replace('None', 'null')
                                                              .replace("'", '"').replace('{', 'newHashMap(')
                                                              .replace('}', ')'))

        decl_string += '\t\t\t\t"timestamp2" -> {},\n'.format(str(t2).replace(':', ' ->').replace('None', 'null')
                                                              .replace("'", '"').replace('{', 'newHashMap(')
                                                              .replace('}', ')'))

        decl_string += '\t\t\t\t"index" -> -1\n\t\t),\n'
        i += 1
    return decl_string[:-2] + '\n)\n'


def get_oh_policy_declaration(policies):
    decl_string = ''
    i = 1
    for policy in policies:
        decl_string += '\tval HashMap<String, Object> policy{} = myPolicies.get("policy{}")\n'.format(i, i)
        i += 1
    return decl_string[:-1]


def get_oh_policy_permission(policies):
    decl_string = '\tpermission = \n'
    i = 1
    for policy in policies:
        decl_string += '\t\tp{}.apply(policy{}, operators) &&\n'.format(i, i)
        i += 1
    return decl_string[:-3]


def get_oh_policies_lambda(policies):
    decl_string = ''
    j = 1
    for policy in policies:
        decl_string += '\tval p{} = [HashMap<String, Object> policy, HashMap<String, Object> operators |\n'.format(j)
        decl_string += '\t\t// Section 1: unpacking the operators\n'
        decl_string += '\t\tval Functions$Function6<Boolean,\n'
        decl_string += '\t\t\t\tHashMap<Number, HashMap<String, HashMap<String, Number>>>,\n'
        decl_string += '\t\t\t\tHashMap<Number, HashMap<String, Number>>,\n'
        decl_string += '\t\t\t\tHashMap<String, Number>,\n'
        decl_string += '\t\t\t\tBoolean,\n'
        decl_string += '\t\t\t\tFunctions$Function5<HashMap<String, Boolean>,\n'
        decl_string += '\t\t\t\t\tHashMap<Number, HashMap<String, HashMap<String, Number>>>,\n'
        decl_string += '\t\t\t\t\tHashMap<Number, HashMap<String, Number>>,\n'
        decl_string += '\t\t\t\t\tHashMap<String, Number>,\n'
        decl_string += '\t\t\t\t\tBoolean,\n'
        decl_string += '\t\t\t\t\tBoolean>,\n'
        decl_string += '\t\t\t\tBoolean> once = operators.get("once")\n'
        decl_string += '\t\tval Functions$Function5<HashMap<String, Boolean>,\n'
        decl_string += '\t\t\t\tHashMap<Number, HashMap<String, HashMap<String, Number>>>,\n'
        decl_string += '\t\t\t\tHashMap<Number, HashMap<String, Number>>,\n'
        decl_string += '\t\t\t\tHashMap<String, Number>,\n'
        decl_string += '\t\t\t\tBoolean,\n'
        decl_string += '\t\t\t\tBoolean> since = operators.get("since")\n'
        decl_string += '\t\tval Functions$Function4<HashMap<String, Boolean>,\n'
        decl_string += '\t\t\t\tHashMap<Number, HashMap<String, Number>>,\n'
        decl_string += '\t\t\t\tHashMap<String, Number>,\n'
        decl_string += '\t\t\t\tBoolean,\n'
        decl_string += '\t\t\t\tBoolean> lastly = operators.get("lastly")\n'
        decl_string += '\t\tval Functions$Function2<Boolean,\n'
        decl_string += '\t\t\t\tBoolean,\n'
        decl_string += '\t\t\t\tBoolean> implies = operators.get("implies")\n\n'
        decl_string += '\t\t// Section 2: unpacking the policy data structure to retrive its elements\n'
        decl_string += '\t\tval HashMap<String, ArrayList<Boolean>> history = policy.get("history")\n'
        decl_string += '\t\tval ArrayList<Boolean> prv = history.get("prv")\n'
        decl_string += '\t\tval ArrayList<Boolean> cur = history.get("cur")\n'
        decl_string += '\t\tval HashMap<Number, HashMap<String, HashMap<String, Number>>> timestamp1 = policy.get("timestamp1")\n'
        decl_string += '\t\tval HashMap<Number, HashMap<String, Number>> timestamp2 = policy.get("timestamp2")\n'
        decl_string += '\t\tvar Number index = policy.get("index")\n\n'
        decl_string += '\t\t// Section 3: initializing variables\n'
        decl_string += '\t\tval current_time = now.getSecondOfDay\n'
        decl_string += '\t\tval current_date = now\n'
        decl_string += '\t\tindex += 1\n'
        decl_string += '\t\tpolicy.put("index", index)\n\n'
        decl_string += '\t\t// Section 4: encoding the user defined policy\n'

        functions = ""
        i = 0
        for subformula in policy.enc:
            functions += '\t\tcur.set({}, '.format(i)
            if subformula.type == 'attribute':
                if subformula.children[0] in ['automation_unit', 'current_time', 'current_date']:
                    functions += subformula.children[0] + ' '
                    value = ""
                    if subformula.children[1].type == 'number':
                        functions += subformula.value.replace('=', '==') + ' '
                        value = subformula.children[1].value
                    elif subformula.children[1].type == 'boolean':
                        functions += subformula.value.replace('=', '==') + ' '
                        value = subformula.children[1].value.lower()
                    elif subformula.children[1].type == 'string':  # needed for date and time
                        functions += subformula.value.replace('=', '==') + ' '
                        value = '"{}"'.format(subformula.children[1].value)
                    elif subformula.children[1].type == 'time':
                        functions += subformula.value.replace('=', '==') + ' '
                        time = subformula.children[1].value(':')
                        value = str((int(time[0]) * 3600) + (int(time[1]) * 60) + int(time[2]))
                    elif subformula.children[1].type == 'date':
                        if subformula.value == '>':
                            functions += '.isAfter'
                        elif subformula.value == '<':
                            functions += '.isBefore'
                        elif subformula.value == '=':
                            functions += '.equals'
                        value = "(new DateTime(" + subformula.children[1].value + "))"
                    functions += value + ')\n'
                else:
                    functions += '{}.state '.format(subformula.children[0].split('#')[1])
                    functions += subformula.value.replace('=', '==') + ' '
                    functions += subformula.children[1].value + ')\n'
            elif subformula.type in ['not_prop', 'not_mtl']:
                functions += "!cur.get({}))\n".format(i - 1)
            elif subformula.type == 'everything':
                functions += "true)\n"
            elif subformula.type in ['bexp_prop', 'bexp_prop_mlt', 'bexp_mlt_prop', 'bexp_mlt', 'bexp_act']:
                functions += "cur.get({}) ".format(i - int(subformula.children[0]))
                functions += subformula.value.replace('and', '&&').replace('or', '||') + ' '
                functions += "cur.get({}))\n".format(i - 1)
            elif subformula.type == 'mlt_lastly_interval':
                interval = subformula.value.split(':')
                functions += 'lastly.apply(newHashMap("p" -> cur.get({}), "prv" -> prv.get({})), timestamp2, newHashMap("i" -> {}, "l" -> {}, "r" -> {}, "current_time" -> current_time), true))\n'.format(i - 1, i - 1, i,
                                                                                                       interval[0], interval[1])
            elif subformula.type == 'mlt_lastly':
                functions += 'newHashMap("p" -> cur.get({}), "prv" -> prv.get({})), timestamp2, newHashMap("i" -> {}, "l" -> 0, "r" -> 0, "current_time" -> current_time), false))\n'.format(i - 1, i - 1, i)
            elif subformula.type == 'mlt_once_interval':
                interval = subformula.value.split(':')
                functions += 'once.apply(cur.get({}), timestamp1, timestamp2, newHashMap("i" -> {}, "l" -> {}, "r" -> {}, "current_time" -> current_time, "idx" -> index), true, since))\n'.format(i - 1, i,
                                                                                                 interval[0], interval[1])
            elif subformula.type == 'mlt_once':
                functions += 'once.apply(cur.get({}), timestamp1, timestamp2, newHashMap("i" -> {}, "l" -> 0, "r" -> 0, "current_time" -> current_time, "idx" -> index), false, since))\n'.format(i - 1, i)
            elif subformula.type == 'mlt_since_interval':
                interval = subformula.value.split(':')
                functions += 'since.apply(newHashMap("p" -> cur.get({}), "q" -> cur.get({})), timestamp1, timestamp2, newHashMap("i" -> {}, "l" -> {}, "r" -> {}, "current_time" -> current_time, "idx" -> index), true))\n'.format(
                    i - int(subformula.children[0]), i - 1, i, interval[0], interval[1])
            elif subformula.type == 'mlt_since':
                functions += 'since.apply(newHashMap("p" -> cur.get({}), "q" -> cur.get({})), timestamp1, timestamp2, newHashMap("i" -> {}, "l" -> 0, "r" -> 0, "current_time" -> current_time, "idx" -> index), false))\n'.format(
                    i - int(subformula.children[0]), i - 1, i)
            elif subformula.type == 'action':
                functions += subformula.children[0] + ' '
                functions += subformula.value.replace('=', '==') + ' '
                functions += '{})\n'.format(subformula.children[1])
            elif subformula.type == 'action_arg':
                functions += subformula.children[0] + ' '
                functions += subformula.value.replace('=', '==') + ' '
                value = ""
                if subformula.children[1].type == 'number':
                    value = subformula.children[1].value
                elif subformula.children[1].type == 'boolean':
                    value = subformula.children[1].value.lower()
                elif subformula.children[1].type == 'string':  # needed for date and time
                    value = '"{}"'.format(subformula.children[1].value)
                functions += value + ')\n'
            elif subformula.type == 'implies':
                functions += "implies.apply(cur.get({}), cur.get({})))\n".format(i - 1, i - int(subformula.children[0]))
            i += 1

        functions += '\t\tval result = cur.get({})\n'.format(i - 1)
        decl_string += functions
        decl_string += '\t\thistory.put("prv", history.get("cur"))\n'
        decl_string += '\t\treturn result\n\t]\n'
        j += 1
    return decl_string[:-1]


def get_oh_encoded_policy_function(policies, oh_template_file):
    if check_file(oh_template_file):
        with open(oh_template_file, 'r') as template_file:
            header = ""
            line = template_file.readline()
            while line:
                key, match = parse_line(line, OH_REGEX_DICT)
                if key == 'policies_data_structure_section':
                    header += line.replace(match.group('policies_data_structure_section'),
                                           get_oh_encoded_policies_initialization(policies))

                elif key == 'policy_section':
                    header += line.replace(match.group('policy_section'),
                                           get_oh_policies_lambda(policies))

                elif key == 'policy_declaration_section':
                    header += line.replace(match.group('policy_declaration_section'),
                                           get_oh_policy_declaration(policies))

                elif key == 'policies_permission_section':
                    header += line.replace(match.group('policies_permission_section'),
                                           get_oh_policy_permission(policies))

                else:
                    header += line
                line = template_file.readline()

            return header


def parse_n_instrument(filepath, header, inst_file_path):
    with open(filepath, 'r') as file_object:
        with open(inst_file_path, 'w+') as tmp_file:
            tmp_file.write(header)
            line = file_object.readline()
            rule_name_stmt = ''
            triggered_event_device_stmt = ''
            triggered_event_stmt = ''
            while line:
                key, match = parse_line(line, OH_REGEX_DICT)
                if key == 'rule':
                    rule_name_stmt = "\tval automation_unit = '" + match.group('rule_name').lower() + "'\n"
                    tmp_file.write(line)
                elif key == 'trigger':
                    triggered_event_device_stmt = "\tval triggered_device = " + match.group('item_name') + "\n"
                    triggered_event_stmt = "\tval triggered_event = {}\n".format('null' if not match.group(
                        'cmd') else match.group('cmd').strip())
                    tmp_file.write(line)
                elif key == 'then':
                    tmp_file.write(line)
                    tmp_file.write(rule_name_stmt)
                    tmp_file.write(triggered_event_device_stmt)
                    tmp_file.write(triggered_event_stmt)
                elif key == 'action':
                    action_item = match.group('action_item')
                    action_cmd = match.group('action_cmd')
                    indent = line.split(action_item)
                    tmp_file.write(indent[0] + 'lock.lock()\n')
                    tmp_file.write(indent[0] + 'try {\n')
                    tmp_file.write(indent[
                                       0] + '\tif (verify.apply(policies, automation_unit, triggered_device, triggered_event, ' + action_item + ', ' + action_cmd + ')) {\n')
                    tmp_file.write('\t\t' + line)
                    tmp_file.write(indent[0] + '\t}\n')
                    tmp_file.write(indent[0] + '} finally{\n')
                    tmp_file.write(indent[0] + '\tlock.unlock()\n')
                    tmp_file.write(indent[0] + '}\n')
                else:
                    tmp_file.write(line)
                line = file_object.readline()

def get_eva_encoded_policies_initialization(policies):
    decl_string = ""
    for policy in policies:
        decl_string += "{} = {{\n\t'prv': {},\n".format(policy.name, str(policy.prv))
        decl_string += "\t'cur': {},\n".format(str(policy.cur))
        decl_string += "\t'tss': {},\n".format(str(policy.tss))
        decl_string += "\t'idx': {}\n}}\n".format(str(policy.idx))
    decl_string += "data = {}\n"
    for policy in policies:
        decl_string += "data['{}'] = {}\n".format(policy.name, policy.name)
    return decl_string

def get_eva_initialize_code(policies, eva_initialize_template_file, pickled_data_path):
    if check_file(eva_initialize_template_file):
        with open(eva_initialize_template_file, 'r') as template_file:
            initialize_code = ""
            line = template_file.readline()
            while line:
                key, match = parse_line(line, EVA_REGEX_DICT)
                if key == 'pickled_data_path':
                    initialize_code += line.replace(match.group('pickled_data_path'),
                                                    "'{}'".format(pickled_data_path))

                elif key == 'pickled_data_section':
                    initialize_code += line.replace(match.group('pickled_data_section'),
                                                    get_eva_encoded_policies_initialization(policies))
                else:
                    initialize_code += line
                line = template_file.readline()

            return initialize_code

def get_eva_encoded_policy_function(policies):
    signature = "(data, automation_unit, evt, action_device, action_command, action_command_arg)"
    functions = ""
    for policy in policies:
        functions += "def {}{}{}\n".format(policy.name, signature, ":")
        functions += "{}current_time = time()\n".format(' '*4)
        functions += "{}current_date = current_time\n".format(' '*4)
        functions += "{}{} = data['{}']\n".format(' '*4, policy.name, policy.name)
        functions += "{}{}['idx'] += 1\n".format(' '*4, policy.name)
        functions += "{}idx = {}['idx']\n".format(' '*4, policy.name)
        functions += "{}cur = {}['cur']\n".format(' '*4, policy.name)
        functions += "{}prv = {}['prv']\n".format(' '*4, policy.name)
        functions += "{}tss = {}['tss']\n".format(' '*4, policy.name)
        i = 0
        for subformula in policy.enc:
            functions += '{}cur[{}] = '.format(' '*4, i)
            if subformula.type == 'attribute':
                if subformula.children[0] in ['automation_unit', 'current_time', 'current_date']:
                    functions += subformula.children[0] + ' '
                    functions += subformula.value.replace('=', '==').replace('!==', '!=') + ' '
                    value = ""
                    if subformula.children[1].type == 'number':
                        value = subformula.children[1].value
                    elif subformula.children[1].type == 'boolean':
                        value = subformula.children[1].value
                    elif subformula.children[1].type == 'string': # needed for date and time
                        value = '"{}"'.format(subformula.children[1].value)
                    elif subformula.children[1].type == 'time':
                        value = "time.mktime(time.strptime('{{}} {{}}'.format(datetime.today().strftime('%Y-%m-%d'), '{}'), '%Y-%m-%d %H:%M:%S'))".format(subformula.children[1].value)
                    elif subformula.children[1].type == 'date':
                        value = "time.mktime(time.strptime('{} 00:00:00', '%Y-%m-%d %H:%M:%S'))".format(subformula.children[1].value)
                    functions += value + '\n'
                else:
                    device_state_code = ""
                    if subformula.children[0].split('#')[0] == 'state':
                        device_state_code += "state('{}')['status']".format(subformula.children[0].split('#')[1])
                    else:
                        device_state_code += "state('{}')['value']".format(subformula.children[0].split('#')[1])
                    functions += device_state_code
                    functions += subformula.value.replace('=', '==').replace('!==', '!=') + ' '
                    value = ""
                    if subformula.children[1].type == 'number':
                        value = subformula.children[1].value
                    elif subformula.children[1].type == 'boolean':
                        value = subformula.children[1].value.lower()
                    elif subformula.children[1].type == 'string':  # needed for date and time
                        value = '{}'.format(subformula.children[1].value)
                    elif subformula.children[1].type == 'time':
                        value = "time.mktime(time.strptime('{{}} {{}}'.format(datetime.today().strftime('%Y-%m-%d'), '{}'), '%Y-%m-%d %H:%M:%S'))".format(
                            subformula.children[1].value)
                    elif subformula.children[1].type == 'date':
                        value = "time.mktime(time.strptime('{} 00:00:00', '%Y-%m-%d %H:%M:%S'))".format(
                            subformula.children[1].value)
                    functions += value + '\n'
            elif subformula.type in ['not_prop', 'not_mtl']:
                functions += "not cur[{}]\n".format(i-1)
            elif subformula.type == 'everything':
                functions += "True\n"
            elif subformula.type in ['bexp_prop', 'bexp_prop_mlt', 'bexp_mlt_prop', 'bexp_mlt', 'bexp_act']:
                functions += "cur[{}] ".format(i - int(subformula.children[0]))
                functions += subformula.value + ' '
                functions += "cur[{}]\n".format(i - 1)
            elif subformula.type == 'mlt_lastly_interval':
                interval = subformula.value.split(':')
                functions += "lastly(cur[{}], prv[{}], tss, {}, {}, {}, current_time, True)\n".format(i-1, i-1, i, interval[0], interval[1])
            elif subformula.type == 'mlt_lastly':
                functions += "lastly(cur[{}], prv[{}], tss, {}, 0, 0, current_time, False)\n".format(i-1, i-1, i)
            elif subformula.type == 'mlt_once_interval':
                interval = subformula.value.split(':')
                functions += "once(cur[{}], tss, {}, {}, {}, current_time, idx, True)\n".format(i-1, i, interval[0], interval[1])
            elif subformula.type == 'mlt_once':
                functions += "once(cur[{}], tss, '{}', 0, 0, current_time, idx, False)\n".format(i-1, i)
            elif subformula.type == 'mlt_since_interval':
                interval = subformula.value.split(':')
                functions += "since(cur[{}], cur[{}], tss, {}, {}, {}, current_time, idx, True)\n".format(i - int(subformula.children[0]), i-1, i, interval[0], interval[1])
            elif subformula.type == 'mlt_since':
                functions += "since(cur[{}], cur[{}], tss, {}, 0, 0, current_time, idx, False)\n".format(i - int(subformula.children[0]), i-1, i)
            elif subformula.type == 'action':
                functions += subformula.children[0] + ' '
                functions += subformula.value.replace('=', '==').replace('!==', '!=') + ' '
                functions += '{}\n'.format(subformula.children[1])
            elif subformula.type == 'action_arg':
                functions += subformula.children[0] + ' '
                functions += subformula.value.replace('=', '==').replace('!==', '!=') + ' '
                value = ""
                if subformula.children[1].type == 'number':
                    value = subformula.children[1].value
                elif subformula.children[1].type == 'boolean':
                    value = subformula.children[1].value.lower()
                elif subformula.children[1].type == 'string':  # needed for date and time
                    value = '"{}"'.format(subformula.children[1].value)
                elif subformula.children[1].type == 'time':
                    value = "time.mktime(time.strptime('{{}} {{}}'.format(datetime.today().strftime('%Y-%m-%d'), '{}'), '%Y-%m-%d %H:%M:%S'))".format(
                        subformula.children[1].value)
                elif subformula.children[1].type == 'date':
                    value = "time.mktime(time.strptime('{} 00:00:00', '%Y-%m-%d %H:%M:%S'))".format(
                        subformula.children[1].value)
                functions += value + '\n'
            elif subformula.type == 'implies':
                functions += "implies(cur[{}], cur[{}])\n".format(i - 1, i - int(subformula.children[0]))
            i += 1

        functions += "{}res = {}['cur'][{}]\n".format(' '*4, policy.name, i - 1)
        functions += "{}{}['prv'] = copy.deepcopy({}['cur'])\n".format(' '*4, policy.name, policy.name)
        functions += "{}return res\n\n\n".format(' '*4)
    return functions[:-2]


def get_eva_encoded_policy_permission(policies):
    argument = "(data, automation_unit, evt, action_device, action_command, action_command_arg)"
    permission = "permission ="
    for policy in policies:
        permission += "{}{}{} and \\\n".format(' '*4, policy.name, argument)
    return permission[:-6]


def get_eva_policy_decision_point_code(policies, eva_pdp_template_file, pickled_data_path):
    if check_file(eva_pdp_template_file):
        with open(eva_pdp_template_file, 'r') as template_file:
            pdp_code = ""
            line = template_file.readline()
            while line:
                key, match = parse_line(line, EVA_REGEX_DICT)
                if key == 'pickled_data_path':
                    pdp_code += line.replace(match.group('pickled_data_path'),
                                                    "'{}'".format(pickled_data_path))

                elif key == 'encoded_policy_section':
                    pdp_code += line.replace(match.group('encoded_policy_section'),
                                             get_eva_encoded_policy_function(policies))

                elif key == 'pdp_body_permission_section':
                    pdp_code += line.replace(match.group('pdp_body_permission_section'),
                                                    get_eva_encoded_policy_permission(policies))

                else:
                    pdp_code += line
                line = template_file.readline()

            return pdp_code

def eva_parse_n_instrument(source_path, destination_path):
    for macro in os.listdir(source_path):
        if macro.endswith(".py"):
            with open(os.path.join(source_path, macro), 'r') as file_object:
                with open(os.path.join(destination_path, macro), 'w+') as tmp_file:
                    line = file_object.readline()
                    while line:
                        key, match = parse_line(line, EVA_REGEX_DICT)
                        if key == 'action':
                            action_item = match.group('action_item')
                            status = match.group('status')
                            value = match.group('value')
                            indent = line.split('action')
                            tmp_file.write("{}if lock('pdp_lock', timeout=3, expires=2):\n".format(indent[0]))
                            tmp_file.write("{}{}if pdp('{}', {}, {}):\n".format(indent[0], ' '*4, action_item, status, value))
                            tmp_file.write("{}{}{}\n".format(indent[0], ' '*8, line))
                            tmp_file.write("{}{}unlock('pdp_lock')\n\n".format(indent[0], ' '*4))
                        elif key == 'action_toggle':
                            action_item = match.group('action_item')
                            status = 'toggle'
                            value = match.group('value')
                            indent = line.split('action_toggle')
                            tmp_file.write("{}if lock('pdp_lock', timeout=3, expires=2):\n".format(indent[0]))
                            tmp_file.write("{}{}if pdp('{}', '{}', {}):\n".format(indent[0], ' '*4, action_item, status, value))
                            tmp_file.write("{}{}{}\n".format(indent[0], ' '*8, line))
                            tmp_file.write("{}{}unlock('pdp_lock')\n\n".format(indent[0], ' '*4))
                        else:
                            tmp_file.write(line)
                        line = file_object.readline()

def create_policy_decision_point(conf, policies, inst_macro_folder):
    initialize_code = get_eva_initialize_code(policies,
                                              conf['eva_ics']['initialize_template_file_path'],
                                              conf['eva_ics']['pickled_data_path'])
    with open(os.path.join(inst_macro_folder, conf['eva_ics']['initialize_generated_file_name']), 'w') as f:
        f.write(initialize_code)

    pdp_code = get_eva_policy_decision_point_code(policies,
                                                  conf['eva_ics']['pdp_template_file_path'],
                                                  conf['eva_ics']['pickled_data_path'])
    with open(os.path.join(inst_macro_folder, conf['eva_ics']['pdp_generated_file_name']), 'w') as f:
        f.write(pdp_code)
