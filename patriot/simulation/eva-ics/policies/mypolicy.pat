POLICY P1:
DENY action_command = on and action_device = g1/lifter_robot
ONLY IF LASTLY(SINCE(state(unit:g1/lifter_robot) = on), value(g1/processed) = 0)

POLICY P2:
DENY action_command = on and action_device = g1/lifting_arm
ONLY IF value(sensor:g1/lli01) = on

POLICY P3:
DENY action_command = on and action_device = g1/compound_valve01
ONLY IF value(sensor:g1/lli01) = on

POLICY P4:
DENY action_command = on and action_device = g1/compound_valve02
ONLY IF value(sensor:g1/lli01) = on

POLICY P5:
DENY action_command = on and action_device = g1/water_valve
ONLY IF value(sensor:g1/lli01) = on

POLICY P6:
ALLOW action_command = on and action_device = g1/mixing_robot
ONLY IF LASTLY(SINCE(state(unit:g1/mixing_robot) = off), state(unit:g1/water_valve) = off)