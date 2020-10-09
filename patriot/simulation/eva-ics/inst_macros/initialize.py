#!/usr/bin/env python

import pickle

#PIK = 'persist.dat'
PIK = '/home/sec/Projects/iiot/eva-3.2.4/xc/lm/persist.dat'

p1 = {
	'prv': [False, False, False, False, False, False, False, False, False],
	'cur': [False, False, False, False, False, False, False, False, False],
	'tss': {1: {'tau': None}, 3: {'q': {'i': None, 'tau': None}, 'p': None}},
	'idx': -1
}
p2 = {
	'prv': [False, False, False, False, False, False],
	'cur': [False, False, False, False, False, False],
	'tss': {},
	'idx': -1
}
p3 = {
	'prv': [False, False, False, False, False, False],
	'cur': [False, False, False, False, False, False],
	'tss': {},
	'idx': -1
}
p4 = {
	'prv': [False, False, False, False, False, False],
	'cur': [False, False, False, False, False, False],
	'tss': {},
	'idx': -1
}
p5 = {
	'prv': [False, False, False, False, False, False],
	'cur': [False, False, False, False, False, False],
	'tss': {},
	'idx': -1
}
p6 = {
	'prv': [False, False, False, False, False, False, False, False],
	'cur': [False, False, False, False, False, False, False, False],
	'tss': {1: {'tau': None}, 3: {'q': {'i': None, 'tau': None}, 'p': None}},
	'idx': -1
}
data = {}
data['p1'] = p1
data['p2'] = p2
data['p3'] = p3
data['p4'] = p4
data['p5'] = p5
data['p6'] = p6


with open(PIK, 'wb') as f:
    pickle.dump(data, f)