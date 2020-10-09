/**
 *  Medicine Management - Temp-Motion
 *
 *  Copyright 2016 Jim Mangione
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 * Logic: 
 * --- If temp > threshold set, send notification
 * --- Send in-app notification at the medicine reminder time if no motion is detected in past 60 minutes
 * --- If motion still isn't detected 10 minutes AFTER reminder time, LED will turn RED
 * --- ----- Once motion is detected, LED will turn back to it's original color
 */
import groovy.time.TimeCategory 

definition(
    name: "Medicine Management - Temp-Motion",
    namespace: "MangioneImagery",
    author: "Jim Mangione",
    description: "This only supports devices with capabilities TemperatureMeasurement, AccelerationSensor and ColorControl (LED). Supports two use cases. First, will notifies via in-app if the fridge where meds are stored exceeds a temperature threshold set in degrees. Secondly, sends an in-app and ambient light notification if you forget to take your meds by sensing movement of the medicine box in the fridge. A reminder will be set to a single time per day. If the box isn't moved within 60 minutes of that reminder, an in-app message will be sent. If the box still isn't moved after an additional 10 minutes, then an LED light turns red until the box is moved",
    category: "Health & Wellness",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png"
)


preferences {
 
     section("My Medicine in the Refrigerator"){
		input "deviceAccelerationSensor", "capability.accelerationSensor", required: true, multiple: false, title: "Movement"
        input "deviceTemperatureMeasurement", "capability.temperatureMeasurement", required: true, multiple: false, title: "Temperature"
	} 

	section("Temperature Threshold"){
    	input "tempThreshold", "number", title: "Temperature Threshold"
    }

    section("Remind me to take my medicine at"){
        input "reminderTime", "time", title: "Time"
    }
    
    // NOTE: Use REAL device - virtual device causes compilation errors
    section("My LED Light"){
    	input "deviceLight", "capability.colorControl", title: "Smart light"
    }    

}

def installed() {
    log.debug("Installed with settings: $settings")
    initialize()
}

def updated() {
    log.debug("Updated with settings: $settings")
    unsubscribe()
    initialize()
}

def initialize() {
    subscribe(deviceTemperatureMeasurement, 'temperature', tempHandler)
    subscribe(deviceAccelerationSensor, 'acceleration.active', motionHandler)
    state .minutesToCheckPriorToReminder = 60
    schedule(reminderTime, checkMotionInPast)
}

def tempHandler(evt) {
    if ( evt .doubleValue > tempThreshold ) {
        log.debug("Fridge temp of $evt.value exceeded threshold")
        parent.verify(app.getLabel(), evt , null, 'sending_notification', "WARNING: Fridge temp is $evt.value with threshold of $tempThreshold") == true ? sendNotification("WARNING: Fridge temp is $evt.value with threshold of $tempThreshold") : log.debug('Invariants Violation!')
    }
}

def motionHandler(evt) {
    log.debug('Medication moved. Send stop LED notification')
    resetLEDNotification()
}

def checkMotionInPast() {
    log.debug("Checking past 60 minutes of activity from $reminderTime")
    def movement = isMoved( state .minutesToCheckPriorToReminder)
    log.debug("Motion found: $movement")
    if (!( movement )) {
        parent.verify(app.getLabel(), null, null, 'sending_notification', 'Hi, please remember to take your meds in the fridge') == true ? sendNotification('Hi, please remember to take your meds in the fridge') : log.debug('Invariants Violation!')
        def reminderTimePlus10 = new java.util.Date(now() + 10 * 60000)
        runOnce(reminderTimePlus10, checkMotionAfterReminder)
    }
}

def checkMotionAfterReminder() {
    log.debug("Checking additional 10 minutes of activity from $reminderTime")
    def movement = isMoved(10)
    log.debug("Motion found: $movement")
    if (!( movement )) {
        log.debug('Notify LED API')
        setLEDNotification()
    }
}

def sendNotification(msg) {
    log.debug("Message Sent: $msg")
    parent.verify(app.getLabel(), msg , null, 'sending_notification', msg ) == true ? sendPush(msg) : log.debug('Invariants Violation!')
}

def isMoved(minutes) {
    def previousDateTime = new java.util.Date(now() - minutes * 60000)
    def evts = deviceAccelerationSensor.eventsSince(previousDateTime)
    def motion = false
    if (evts.size() > 0) {
        evts.each({ 
            if ( it .value == 'active') {
                motion = true
            }
        })
    }
    return motion 
}

def setLEDNotification() {
    state .ledState = deviceLight.currentValue('switch')
    state .origColor = deviceLight.currentValue('hue')
    parent.verify(app.getLabel(), null, deviceLight.getDisplayName(), 'on', null) == true ? deviceLight.on() : log.debug('Invariants Violation!')
    parent.verify(app.getLabel(), null, deviceLight.getDisplayName(), 'setHue', 100) == true ? deviceLight.setHue(100) : log.debug('Invariants Violation!')
    log.debug("LED set to RED. Original color stored: $state.origColor")
}

def resetLEDNotification() {
    log.debug("Reset LED color to: $state.origColor")
    parent.verify(app.getLabel(), null, deviceLight.getDisplayName(), 'setHue', state .origColor) == true ? deviceLight.setHue( state .origColor) : log.debug('Invariants Violation!')
    if ( state .ledState == 'off') {
        parent.verify(app.getLabel(), null, deviceLight.getDisplayName(), 'off', null) == true ? deviceLight.off() : log.debug('Invariants Violation!')
    }
}

def getChildAppDevices() {
    return settings 
}

