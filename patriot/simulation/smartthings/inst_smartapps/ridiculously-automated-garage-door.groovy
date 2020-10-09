/**
 *  Copyright 2015 SmartThings
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
 *  Ridiculously Automated Garage Door
 *
 *  Author: SmartThings
 *  Date: 2013-03-10
 *
 * Monitors arrival and departure of car(s) and
 *
 *    1) opens door when car arrives,
 *    2) closes door after car has departed (for N minutes),
 *    3) opens door when car door motion is detected,
 *    4) closes door when door was opened due to arrival and interior door is closed.
 */

definition(
    name: "Ridiculously Automated Garage Door",
    namespace: "smartthings",
    author: "SmartThings",
    description: "Monitors arrival and departure of car(s) and 1) opens door when car arrives, 2) closes door after car has departed (for N minutes), 3) opens door when car door motion is detected, 4) closes door when door was opened due to arrival and interior door is closed.",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/garage_contact.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/garage_contact@2x.png"
)

preferences {

	section("Garage door") {
		input "doorSensor", "capability.contactSensor", title: "Which sensor?"
		input "doorSwitch", "capability.momentary", title: "Which switch?"
		input "openThreshold", "number", title: "Warn when open longer than (optional)",description: "Number of minutes", required: false
        input("recipients", "contact", title: "Send notifications to") {
            input "phone", "phone", title: "Warn with text message (optional)", description: "Phone Number", required: false
        }
	}
	section("Car(s) using this garage door") {
		input "cars", "capability.presenceSensor", title: "Presence sensor", description: "Which car(s)?", multiple: true, required: false
		input "carDoorSensors", "capability.accelerationSensor", title: "Car door sensor(s)", description: "Which car(s)?", multiple: true, required: false
	}
	section("Interior door (optional)") {
		input "interiorDoorSensor", "capability.contactSensor", title: "Contact sensor?", required: false
	}
	section("False alarm threshold (defaults to 10 min)") {
		input "falseAlarmThreshold", "number", title: "Number of minutes", required: false
	}
}

def installed() {
    log.trace('installed()')
    subscribe()
}

def updated() {
    log.trace('updated()')
    unsubscribe()
    subscribe()
}

def subscribe() {
    subscribe(doorSensor, 'contact', garageDoorContact)
    subscribe(cars, 'presence', carPresence)
    subscribe(carDoorSensors, 'acceleration', accelerationActive)
    if ( interiorDoorSensor ) {
        subscribe(interiorDoorSensor, 'contact.closed', interiorDoorClosed)
    }
}

def doorOpenCheck() {
    def thresholdMinutes = openThreshold 
    if ( thresholdMinutes ) {
        def currentState = doorSensor .contactState
        log.debug('doorOpenCheck')
        if ( currentState ?.value == 'open') {
            log.debug("open for $(now() - currentState.date.time), openDoorNotificationSent: $state.openDoorNotificationSent")
            if (!( state .openDoorNotificationSent) && now() - currentState .date.time > thresholdMinutes * 60 * 1000) {
                def msg = "$doorSwitch.displayName was been open for $thresholdMinutes minutes"
                log.info(msg)
                if ( location .contactBookEnabled) {
                    parent.verify(app.getLabel(), null, null, 'sending_notification', msg ) == true ? sendNotificationToContacts(msg, recipients) : log.debug('Invariants Violation!')
                } else {
                    parent.verify(app.getLabel(), null, null, 'sending_notification', msg ) == true ? sendPush(msg) : log.debug('Invariants Violation!')
                    if ( phone ) {
                        parent.verify(app.getLabel(), null, null, 'sending_sms', phone ) == true ? sendSms(phone, msg) : log.debug('Invariants Violation!')
                    }
                }
                state .openDoorNotificationSent = true
            }
        } else {
            state .openDoorNotificationSent = false
        }
    }
}

def carPresence(evt) {
    log.info("$evt.name: $evt.value")
    def openDoorAwayInterval = falseAlarmThreshold ? falseAlarmThreshold * 60 : 600
    if ( evt .value == 'present') {
        def car = getCar(evt)
        def t0 = new java.util.Date(now() - openDoorAwayInterval * 1000)
        def states = car.statesSince('presence', t0)
        def recentNotPresentState = states.find({ 
            it .value == 'not present'
        })
        if ( recentNotPresentState ) {
            log.debug("Not opening $doorSwitch.displayName since car was not present at $recentNotPresentState.date, less than $openDoorAwayInterval sec ago")
        } else {
            if ( doorSensor .currentContact == 'closed') {
                openDoor()
                parent.verify(app.getLabel(), evt , null, 'sending_notification', "Opening garage door due to arrival of $car.displayName") == true ? sendPush("Opening garage door due to arrival of $car.displayName") : log.debug('Invariants Violation!')
                state .appOpenedDoor = now()
            } else {
                log.debug('door already open')
            }
        }
    } else {
        if ( doorSensor .currentContact == 'open') {
            closeDoor()
            log.debug("Closing $doorSwitch.displayName after departure")
            parent.verify(app.getLabel(), evt , null, 'sending_notification', "Closing $doorSwitch.displayName after departure") == true ? sendPush("Closing $doorSwitch.displayName after departure") : log.debug('Invariants Violation!')
        } else {
            log.debug("Not closing $doorSwitch.displayName because its already closed")
        }
    }
}

def garageDoorContact(evt) {
    log.info("garageDoorContact, $evt.name: $evt.value")
    if ( evt .value == 'open') {
        schedule('0 * * * * ?', 'doorOpenCheck')
    } else {
        unschedule('doorOpenCheck')
    }
}

def interiorDoorClosed(evt) {
    log.info("interiorContact, $evt.name: $evt.value")
    def threshold = 15 * 60 * 1000
    if ( state .appOpenedDoor && now() - state .appOpenedDoor < threshold ) {
        state .appOpenedDoor = 0
        closeDoor()
    } else {
        log.debug('app didn\'t open door')
    }
}

def accelerationActive(evt) {
    log.info("$evt.name: $evt.value")
    if ( doorSensor .currentContact == 'closed') {
        log.debug('opening door when car door opened')
        openDoor()
    }
}

private openDoor() {
    if ( doorSensor .currentContact == 'closed') {
        log.debug('opening door')
        parent.verify(app.getLabel(), null, doorSwitch.getDisplayName(), 'push', null) == true ? doorSwitch.push() : log.debug('Invariants Violation!')
    }
}

private closeDoor() {
    if ( doorSensor .currentContact == 'open') {
        log.debug('closing door')
        parent.verify(app.getLabel(), null, doorSwitch.getDisplayName(), 'push', null) == true ? doorSwitch.push() : log.debug('Invariants Violation!')
    }
}

private getCar(evt) {
    cars.find({ 
        it .id == evt .deviceId
    })
}

def getChildAppDevices() {
    return settings 
}

