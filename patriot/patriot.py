from lib.util import *
import sys
import traceback

from antlr4 import *
from lib.PATRIOTLexer import PATRIOTLexer
from lib.policy import *
from lib.z3_policy_analysis import analyze_policies


class Patriot:
    def __init__(self):
        args = setup_args()
        if check_file(args.policy):
            getattr(self, args.target)(args)

    def st(self, args):
        tree, err = self.get_parse_tree(args.policy)
        if err == 0:
            visitor = PolicyVisitor()
            try:
                tree.accept(visitor)
            except Exception as e:
                print(e)
                print("\nSyntax error occurred in the policy file!\n")
                sys.exit(1)
            policies = visitor.policies
            if args.task == 'analysis':
                try:
                    self.policy_analysis(policies)
                except Exception as e:
                    print("\nAn error occurred in analyzing the policies!\n")
                    sys.exit(1)
                print('Analysis done!')
            else:
                try:
                    self.instrument_smartapps(policies, args.app_folder, args.inst_app_folder)
                except Exception as e:
                    print(e)
                    #traceback.print_exc()
                    print("\nAn error occurred in instrumenting the apps!\n")
                    sys.exit(1)
                print('Instrumentation done!')




    def oh(self, args):
        tree, err = self.get_parse_tree(args.policy)
        if err == 0:
            visitor = PolicyVisitor()
            try:
                tree.accept(visitor)
            except Exception as e:
                print(e)
                print("\nSyntax error occurred in the policy file!\n")
                sys.exit(1)
            policies = visitor.policies
            if args.task == 'analysis':
                try:
                    self.policy_analysis(policies)
                except Exception as e:
                    print("\nAn error occurred in analyzing the policies!\n")
                    sys.exit(1)
                print('Analysis done!')
            else:
                try:
                    self.instrument_rules(policies, args.app_folder, args.inst_app_folder)
                except Exception as e:
                    print(e)
                    traceback.print_exc()
                    print("\nAn error occurred in instrumenting the rules!\n")
                    sys.exit(1)
                print('Instrumentation done!')


    def eva(self, args):
        tree, err = self.get_parse_tree(args.policy)
        if err == 0:
            visitor = PolicyVisitor()
            try:
                tree.accept(visitor)
            except Exception as e:
                print(e)
                print("\nSyntax error occurred in the policy file!\n")
                sys.exit(1)
            policies = visitor.policies
            if args.task == 'analysis':
                try:
                    self.policy_analysis(policies)
                except Exception as e:
                    print("\nAn error occurred in analyzing the policies!\n")
                    sys.exit(1)
                print('Analysis done!')
            else:
                try:
                    self.instrument_macros(policies, args.app_folder, args.inst_app_folder)
                except Exception as e:
                    print(e)
                    traceback.print_exc()
                    print("\nAn error occurred in instrumenting the macros!\n")
                    sys.exit(1)
                print('Instrumentation done!')

    def get_parse_tree(self, file_name):
        pol_src_code = FileStream(file_name)
        lexer = PATRIOTLexer(pol_src_code)
        stream = CommonTokenStream(lexer)
        parser = PATRIOTParser(stream)
        tree = parser.policy_language()
        return tree, parser.getNumberOfSyntaxErrors()

    def policy_analysis(self, policies):
        analyze_policies(policies)

    def instrument_smartapps(self, policies, app_folder, inst_app_folder):
        conf = get_config()
        app_names = preprocess_st_smartapps(app_folder, conf['smart_things']['preprocessed_smartapps_folder'])
        create_st_policy_manager(conf['smart_things']['policy_manager_template_file_path'],
                                 inst_app_folder,
                                 app_names,
                                 policies)
        guard_smartapps_actions(conf['smart_things']['preprocessed_smartapps_folder'],
                                inst_app_folder,
                                conf['smart_things']['actions_list'],
                                conf['smart_things']['instrumentation_log_path'])

    def instrument_rules(self, policies, rule_file, inst_rule_folder):
        conf = get_config()
        header = get_oh_encoded_policy_function(policies, conf['openhab']['policy_manager_template_file_path'])
        parse_n_instrument(rule_file, header, inst_rule_folder)

    def instrument_macros(self, policies, macro_folder, inst_macro_folder):
        conf = get_config()
        create_policy_decision_point(conf, policies, inst_macro_folder)
        eva_parse_n_instrument(macro_folder, inst_macro_folder)



if __name__ == '__main__':
    Patriot()
