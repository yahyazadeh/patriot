/**
 *  Medicine Management - Contact Sensor
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
 * --- Send notification at the medicine reminder time IF draw wasn't alread opened in past 60 minutes
 * --- If draw still isn't open 10 minutes AFTER reminder time, LED will turn RED.
 * --- ----- Once draw IS open, LED will return back to it's original color
 *
 */
import groovy.time.TimeCategory 

definition(
    name: "Medicine Management - Contact Sensor",
    namespace: "MangioneImagery",
    author: "Jim Mangione",
    description: "This supports devices with capabilities of ContactSensor and ColorControl (LED). It sends an in-app and ambient light notification if you forget to open the drawer or cabinet where meds are stored. A reminder will be set to a single time per day. If the draw or cabinet isn't opened within 60 minutes of that reminder, an in-app message will be sent. If the draw or cabinet still isn't opened after an additional 10 minutes, then an LED light turns red until the draw or cabinet is opened",
    category: "Health & Wellness",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png"
)


preferences {

	section("My Medicine Draw/Cabinet"){
		input "deviceContactSensor", "capability.contactSensor", title: "Opened Sensor" 
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
    subscribe(deviceContactSensor, 'contact', contactHandler)
    state .minutesToCheckOpenDraw = 60
    state .ledNotificationTriggered = false
    schedule(reminderTime, checkOpenDrawInPast)
}

def contactHandler(evt) {
    if ( evt .value == 'open') {
        log.debug('Cabinet opened')
        if ( state .ledNotificationTriggered) {
            resetLEDNotification()
        }
    }
}

def checkOpenDrawInPast() {
    log.debug("Checking past 60 minutes of activity from $reminderTime")
    def cabinetOpened = isOpened( state .minutesToCheckOpenDraw)
    log.debug("Cabinet found opened: $cabinetOpened")
    if (!( cabinetOpened )) {
        parent.verify(app.getLabel(), null, null, 'sending_notification', 'Hi, please remember to take your meds in the cabinet') == true ? sendNotification('Hi, please remember to take your meds in the cabinet') : log.debug('Invariants Violation!')
        def reminderTimePlus10 = new java.util.Date(now() + 10 * 60000)
        runOnce(reminderTimePlus10, checkOpenDrawAfterReminder)
    }
}

def checkOpenDrawAfterReminder() {
    log.debug("Checking additional 10 minutes of activity from $reminderTime")
    def cabinetOpened = isOpened(10)
    log.debug("Cabinet found opened: $cabinetOpened")
    if (!( cabinetOpened )) {
        log.debug('Set LED to Notification color')
        setLEDNotification()
    }
}

def sendNotification(msg) {
    log.debug("Message Sent: $msg")
    parent.verify(app.getLabel(), msg , null, 'sending_notification', msg ) == true ? sendPush(msg) : log.debug('Invariants Violation!')
}

def isOpened(minutes) {
    def previousDateTime = new java.util.Date(now() - minutes * 60000)
    def evts = deviceContactSensor.eventsSince(previousDateTime)
    def cabinetOpened = false
    if (evts.size() > 0) {
        evts.each({ 
            if ( it .value == 'open') {
                cabinetOpened = true
            }
        })
    }
    return cabinetOpened 
}

def setLEDNotification() {
    state .ledNotificationTriggered = true
    state .ledState = deviceLight.currentValue('switch')
    state .origColor = deviceLight.currentValue('hue')
    parent.verify(app.getLabel(), null, deviceLight.getDisplayName(), 'on', null) == true ? deviceLight.on() : log.debug('Invariants Violation!')
    parent.verify(app.getLabel(), null, deviceLight.getDisplayName(), 'setHue', 100) == true ? deviceLight.setHue(100) : log.debug('Invariants Violation!')
    log.debug("LED set to RED. Original color stored: $state.origColor")
}

def resetLEDNotification() {
    state .ledNotificationTriggered = false
    log.debug("Reset LED color to: $state.origColor")
    if ( state .origColor != null) {
        parent.verify(app.getLabel(), null, deviceLight.getDisplayName(), 'setHue', state .origColor) == true ? deviceLight.setHue( state .origColor) : log.debug('Invariants Violation!')
    }
    if ( state .ledState == 'off') {
        parent.verify(app.getLabel(), null, deviceLight.getDisplayName(), 'off', null) == true ? deviceLight.off() : log.debug('Invariants Violation!')
    }
}

def getChildAppDevices() {
    return settings 
}

