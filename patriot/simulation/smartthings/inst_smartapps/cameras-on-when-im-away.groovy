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
 *  Cameras On When I'm Away
 *
 *  Author: danny@smartthings.com
 *  Date: 2013-10-07
 */

definition(
    name: "Cameras On When I'm Away",
    namespace: "smartthings",
    author: "SmartThings",
    description: "Turn cameras on when I'm away",
    category: "Available Beta Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Partner/dropcam-on-off-presence.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Partner/dropcam-on-off-presence@2x.png"
)

preferences {
	section("When all of these people are home...") {
		input "people", "capability.presenceSensor", multiple: true
	}
	section("Turn off camera power..."){
		input "switches1", "capability.switch", multiple: true
	}
}

def installed() {
    log.debug("Installed with settings: $settings")
    log.debug("Current people = $people.collect({ -> ... })")
    subscribe(people, 'presence', presence)
}

def updated() {
    log.debug("Updated with settings: $settings")
    log.debug("Current people = $people.collect({ -> ... })")
    unsubscribe()
    subscribe(people, 'presence', presence)
}

def presence(evt) {
    log.debug("evt.name: $evt.value")
    if ( evt .value == 'not present') {
        log.debug('checking if everyone is away')
        if (everyoneIsAway()) {
            log.debug('starting on Sequence')
            runIn(60 * 2, 'turnOn')
        }
    } else {
        if (!(everyoneIsAway())) {
            turnOff()
        }
    }
}

def turnOff() {
    log.debug('canceling On requests')
    unschedule('turnOn')
    log.info('turning off the camera')
    parent.verify(app.getLabel(), null, switches1.getDisplayName(), 'off', null) == true ? switches1.off() : log.debug('Invariants Violation!')
}

def turnOn() {
    log.info('turned on the camera')
    parent.verify(app.getLabel(), null, switches1.getDisplayName(), 'on', null) == true ? switches1.on() : log.debug('Invariants Violation!')
    unschedule('turnOn')
}

private everyoneIsAway() {
    def result = true
    for (person : people ) {
        if ( person .currentPresence == 'present') {
            result = false
            break
        }
    }
    log.debug("everyoneIsAway: $result")
    return result 
}

def getChildAppDevices() {
    return settings 
}

