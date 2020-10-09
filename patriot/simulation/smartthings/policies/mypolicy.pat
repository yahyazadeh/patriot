POLICY P1:
ALLOW   action_command = sending_sms
ONLY IF automation_unit = flood-alert OR
        automation_unit = energy-alerts OR
        automation_unit = humidity-alert OR
        automation_unit = mail-arrived OR
        automation_unit = medicine-reminder OR
        automation_unit = presence-change-text OR
        automation_unit = ready-for-rain OR
        automation_unit = laundry-monitor

POLICY P2:
DENY    action_command = http_request

POLICY P3:
ALLOW   action_command = unlocked AND
        action_device = FrontDoorLock
ONLY IF automation_unit = enhanced-auto-lock-door

POLICY P4:
ALLOW   action_device = WaterValve AND
        action_command = closed
ONLY IF LASTLY(state(FireSprinkler) != on) WITHIN [0, 18000]

POLICY P5:
ALLOW   action_device = WaterValve AND
        action_command = closed
ONLY IF LASTLY(state(WaterLeakSensor) = wet) WITHIN [0, 60]

POLICY P6:
ALLOW   action_device = Light1 AND
        action_command = on
ONLY IF state(VacationMode) != on

POLICY P7:
DENY    action_device = SurveillanceCamera AND
        action_command = off
EXCEPT  state(MyPresence) = present

POLICY P8:
ALLOW   action_device = Light1 AND
        action_command = on
ONLY IF state(MyPresence) = present

POLICY P9:
ALLOW   action_device = HallwayLight AND
        action_command = on
ONLY IF LASTLY(state(HallwayMotionSensor) = active) WITHIN [0, 20]

POLICY P10:
ALLOW   action_device = MyAlarm AND
        action_command = siren
ONLY IF LASTLY(state(MySmokeDetector) = detected) WITHIN [0, 60] OR
        LASTLY(state(MyCo2Detector) = detected) WITHIN [0, 60] OR
        LASTLY(state(FloodSensor) = wet) WITHIN [0, 60] OR
        LASTLY(state(HallwayMotionSensor) = active) WITHIN [0, 60] AND state(MyPresence) != present

POLICY P11:
DENY    action_device = CoffeeMachine AND
        action_command = on
ONLY IF state(MyPresence) != present

POLICY P12:
DENY    action_device = Refrigerator OR
        action_device = TV AND
        action_command = off
ONLY IF automation_unit = energy-saver

POLICY P13:
ALLOW   action_device = Light1 AND
        action_command = off
ONLY IF LASTLY(state(Light1) = on) WITHIN [0, 30]

POLICY P14:
DENY    action_device = GarageDoor AND
        action_command = open
EXCEPT  automation_unit = garage-door-opener

POLICY P15:
DENY    action_device = FrontDoorLock AND
        action_command = unlocked
ONLY IF state(MyPresence) != present OR
        state(SleepMode) = on
EXCEPT  LASTLY(state(MySmokeDetector) = detected) WITHIN [0, 60]

POLICY P16:
ALLOW   action_device = AC AND
        action_command = on
ONLY IF state(Heater) = off

POLICY P17:
ALLOW   action_device = Heater AND
        action_command = on
ONLY IF state(AC) = off

POLICY P18:
ALLOW   action_device = LivingRoomWindow AND
        action_command = on
ONLY IF state(AC) = off AND
        state(Heater) = off

POLICY P19:
ALLOW   action_device = LivingRoomWindow AND
        action_command = on
ONLY IF LASTLY(state(HallwayMotionSensor) = active) WITHIN [0, 60]

POLICY P20:
ALLOW   action_device = LivingRoomWindow AND
        action_command = off
ONLY IF LASTLY(state(LivingRoomWindow) = on) WITHIN [0, 30]

POLICY P21:
ALLOW   action_device = InteriorLock AND
        action_command = unlocked
ONLY IF state(ExteriorLock) = locked AND
        LASTLY(state(Pod) != present)

POLICY P22:
DENY    action_device = IrrigationSprinkler AND
        action_command = on
ONLY IF SINCE(state(GardenMoistureSensor) = wet, state(GardenMoistureSensor) = dry) WITHIN [0, 172800]

POLICY P23:
ALLOW   action_device = AwayMode AND
        action_command = on
ONLY IF state(MyPresence) != present

POLICY P24:
ALLOW   action_device = HomeMode AND
        action_command = on
ONLY IF state(MyPresence) = present

POLICY P25:
ALLOW   action_device = CoffeeMachine AND
        action_command = off
ONLY IF LASTLY(state(CoffeeMachine) = on) WITHIN [0, 30]

POLICY P26:
ALLOW   action_device = TV AND
        action_command = off
ONLY IF LASTLY(state(TV) = on) WITHIN [0, 30]

POLICY P27:
ALLOW   action_device = WaterPump AND
        action_command = off
ONLY IF LASTLY(state(WaterPump) = on) WITHIN [0, 120]

POLICY P28:
ALLOW   action_device = WaterPump AND
        action_command = off
ONLY IF state(FloodSensor) = dry

POLICY P29:
ALLOW   action_device = Thermostat AND
        action_command = off
ONLY IF LASTLY(state(Thermostat) = on) WITHIN [0, 120]

POLICY P30:
ALLOW   action_device = LightDimmer AND
        action_command = set_level
ONLY IF state(MyPresence) = present

POLICY P31:
DENY    action_device = TV AND
        action_command = on
ONLY IF current_time < 05:00:00

POLICY P32:
ALLOW   action_device = LivingRoomWindow AND
        action_command = on
ONLY IF state(VacationMode) != on

POLICY P33:
ALLOW   action_device = LivingRoomWindow AND
        action_command = on
ONLY IF state(MyPresence) = present