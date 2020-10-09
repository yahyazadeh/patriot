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
 *  Turn It On For 5 Minutes
 *  Turn on a switch when a contact sensor opens and then turn it back off 5 minutes later.
 *
 *  Author: SmartThings
 */
definition(
    name: "Turn It On For 5 Minutes",
    namespace: "smartthings",
    author: "SmartThings",
    description: "When a SmartSense Multi is opened, a switch will be turned on, and then turned off after 5 minutes.",
    category: "Safety & Security",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/light_contact-outlet.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/light_contact-outlet@2x.png"
)

preferences {
	section("When it opens..."){
		input "contact1", "capability.contactSensor"
	}
	section("Turn on a switch for 5 minutes..."){
		input "switch1", "capability.switch"
	}
}

def installed() {
    log.debug("Installed with settings: $settings")
    subscribe(contact1, 'contact.open', contactOpenHandler)
}

def updated(settings) {
    log.debug("Updated with settings: $settings")
    unsubscribe()
    subscribe(contact1, 'contact.open', contactOpenHandler)
}

def contactOpenHandler(evt) {
    parent.verify(app.getLabel(), evt , switch1.getDisplayName(), 'on', null) == true ? switch1.on() : log.debug('Invariants Violation!')
    def fiveMinuteDelay = 60 * 5
    runIn(fiveMinuteDelay, turnOffSwitch)
}

def turnOffSwitch() {
    parent.verify(app.getLabel(), null, switch1.getDisplayName(), 'off', null) == true ? switch1.off() : log.debug('Invariants Violation!')
}

def getChildAppDevices() {
    return settings 
}

