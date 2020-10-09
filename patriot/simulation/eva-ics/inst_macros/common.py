import pickle
import copy
#import time
from datetime import datetime

#PIK = 'persist.dat'
PIK = '/home/sec/Projects/iiot/eva-3.2.4/xc/lm/persist.dat'

def p1(data, automation_unit, evt, action_device, action_command, action_command_arg):
    current_time = time()
    current_date = current_time
    p1 = data['p1']
    p1['idx'] += 1
    idx = p1['idx']
    cur = p1['cur']
    prv = p1['prv']
    tss = p1['tss']
    cur[0] = state('unit:g1/lifter_robot')['status']== on
    cur[1] = lastly(cur[0], prv[0], tss, 1, 0, 0, current_time, False)
    cur[2] = lvar_value('g1/processed')== 0
    cur[3] = since(cur[1], cur[2], tss, 3, 0, 0, current_time, idx, False)
    cur[4] = not cur[3]
    cur[5] = action_command == on
    cur[6] = action_device == 'g1/lifter_robot'
    cur[7] = cur[5] and cur[6]
    cur[8] = implies(cur[7], cur[4])
    res = p1['cur'][8]
    p1['prv'] = copy.deepcopy(p1['cur'])
    return res


def p2(data, automation_unit, evt, action_device, action_command, action_command_arg):
    current_time = time()
    current_date = current_time
    p2 = data['p2']
    p2['idx'] += 1
    idx = p2['idx']
    cur = p2['cur']
    prv = p2['prv']
    tss = p2['tss']
    cur[0] = state('sensor:g1/lli01')['value']== on
    cur[1] = not cur[0]
    cur[2] = action_command == on
    cur[3] = action_device == 'g1/lifting_arm'
    cur[4] = cur[2] and cur[3]
    cur[5] = implies(cur[4], cur[1])
    res = p2['cur'][5]
    p2['prv'] = copy.deepcopy(p2['cur'])
    return res


def p3(data, automation_unit, evt, action_device, action_command, action_command_arg):
    current_time = time()
    current_date = current_time
    p3 = data['p3']
    p3['idx'] += 1
    idx = p3['idx']
    cur = p3['cur']
    prv = p3['prv']
    tss = p3['tss']
    cur[0] = state('sensor:g1/lli01')['value']== on
    cur[1] = not cur[0]
    cur[2] = action_command == on
    cur[3] = action_device == 'g1/compound_valve01'
    cur[4] = cur[2] and cur[3]
    cur[5] = implies(cur[4], cur[1])
    res = p3['cur'][5]
    p3['prv'] = copy.deepcopy(p3['cur'])
    return res


def p4(data, automation_unit, evt, action_device, action_command, action_command_arg):
    current_time = time()
    current_date = current_time
    p4 = data['p4']
    p4['idx'] += 1
    idx = p4['idx']
    cur = p4['cur']
    prv = p4['prv']
    tss = p4['tss']
    cur[0] = state('sensor:g1/lli01')['value']== on
    cur[1] = not cur[0]
    cur[2] = action_command == on
    cur[3] = action_device == 'g1/compound_valve02'
    cur[4] = cur[2] and cur[3]
    cur[5] = implies(cur[4], cur[1])
    res = p4['cur'][5]
    p4['prv'] = copy.deepcopy(p4['cur'])
    return res


def p5(data, automation_unit, evt, action_device, action_command, action_command_arg):
    current_time = time()
    current_date = current_time
    p5 = data['p5']
    p5['idx'] += 1
    idx = p5['idx']
    cur = p5['cur']
    prv = p5['prv']
    tss = p5['tss']
    cur[0] = state('sensor:g1/lli01')['value']== on
    cur[1] = not cur[0]
    cur[2] = action_command == on
    cur[3] = action_device == 'g1/water_valve'
    cur[4] = cur[2] and cur[3]
    cur[5] = implies(cur[4], cur[1])
    res = p5['cur'][5]
    p5['prv'] = copy.deepcopy(p5['cur'])
    return res


def p6(data, automation_unit, evt, action_device, action_command, action_command_arg):
    current_time = time()
    current_date = current_time
    p6 = data['p6']
    p6['idx'] += 1
    idx = p6['idx']
    cur = p6['cur']
    prv = p6['prv']
    tss = p6['tss']
    cur[0] = state('unit:g1/mixing_robot')['status']== off
    cur[1] = lastly(cur[0], prv[0], tss, 1, 0, 0, current_time, False)
    cur[2] = state('unit:g1/water_valve')['status']== off
    cur[3] = since(cur[1], cur[2], tss, 3, 0, 0, current_time, idx, False)
    cur[4] = action_command == on
    cur[5] = action_device == 'g1/mixing_robot'
    cur[6] = cur[4] and cur[5]
    cur[7] = implies(cur[6], cur[3])
    res = p6['cur'][7]
    p6['prv'] = copy.deepcopy(p6['cur'])
    return res


def pdp(action_device, action_command, action_command_arg):
    # _0: current macro ID
    automation_unit = _0

    # _source: item generated the event.
    evt = _source.item_id

    data = {}
    with open(PIK, 'rb') as f:
        data = pickle.load(f)

    permission =    p1(data, automation_unit, evt, action_device, action_command, action_command_arg) and \
    p2(data, automation_unit, evt, action_device, action_command, action_command_arg) and \
    p3(data, automation_unit, evt, action_device, action_command, action_command_arg) and \
    p4(data, automation_unit, evt, action_device, action_command, action_command_arg) and \
    p5(data, automation_unit, evt, action_device, action_command, action_command_arg) and \
    p6(data, automation_unit, evt, action_device, action_command, action_command_arg) 

    with open(PIK, 'wb') as f:
        pickle.dump(data, f)

    return permission

def since(p, q, tss, i, l, r, current_time, idx, bounded):
    if q:
        tss[i]['q']['i'] = idx
        tss[i]['q']['tau'] = current_time

    if p:
        if not tss[i]['p']:
            tss[i]['p'] = idx

    else:
        tss[i]['p'] =  None

    tss_q = tss[i]['q']
    tss_p = tss[i]['p']
    q_tau = tss_q['tau']
    q_i = tss_q['i']
    if tss_q and tss_p and q_tau:
        if bounded:
            period = time_diff_sec(current_time, q_tau)
            return ((q_i <= idx) and (tss_p <= q_i + 1) and (period >= l and period <= r))
        else:
            return ((q_i <= idx) and (tss_p <= q_i + 1))

    else:
        return False


def once(p, tss, i, l, r, current_time, idx, bounded):
    return since(true, p, tss, i, l, r, current_time, idx, bounded)

def lastly(p, prv, tss, i, l, r, current_time, bounded):
    y = False
    if prv:
        if bounded:
            period = time_diff_sec(current_time, tss[i]['tau'])
            y = period >= l and period <= r

        else:
            y = True

    if p:
        tss[i]['tau'] = current_time

    return y


def implies(p, q):
    return (not p) or q


def time_diff_sec(t2, t1):
    if t2 < t1:
        return -1

    else:
        return t2 - t1