/**
 *  My Light Toggle
 *
 *  Copyright 2015 Jesse Silverberg
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
 */
definition(
    name: "My Light Toggle",
    namespace: "JLS",
    author: "Jesse Silverberg",
    description: "Toggle lights on/off with a motion sensor",
    category: "Convenience",
    iconUrl: "https://www.dropbox.com/s/6kxtd2v5reggonq/lightswitch.gif?raw=1",
    iconX2Url: "https://www.dropbox.com/s/6kxtd2v5reggonq/lightswitch.gif?raw=1",
    iconX3Url: "https://www.dropbox.com/s/6kxtd2v5reggonq/lightswitch.gif?raw=1"
)


preferences {
	section("When this sensor detects motion...") {
		input "motionToggler", "capability.motionSensor", title: "Motion Here", required: true, multiple: false
    }
    
    section("Master switch for the toggle reference...") {
    	input "masterToggle", "capability.switch", title: "Reference switch", required: true, multiple: false
    }
    
    section("Toggle lights...") {
	    input "switchesToToggle", "capability.switch", title: "These go on/off", required: true, multiple: true
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
    subscribe(motionToggler, 'motion', toggleSwitches)
}

def toggleSwitches(evt) {
    log.debug("$evt.value")
    if ( evt .value == 'active' && masterToggle .currentSwitch == 'off') {
        parent.verify(app.getLabel(), evt , switchesToToggle.getDisplayName(), 'on', null) == true ? switchesToToggle.on() : log.debug('Invariants Violation!')
        parent.verify(app.getLabel(), evt , masterToggle.getDisplayName(), 'on', null) == true ? masterToggle.on() : log.debug('Invariants Violation!')
    } else {
        if ( evt .value == 'active' && masterToggle .currentSwitch == 'on') {
            parent.verify(app.getLabel(), evt , switchesToToggle.getDisplayName(), 'off', null) == true ? switchesToToggle.off() : log.debug('Invariants Violation!')
            parent.verify(app.getLabel(), evt , masterToggle.getDisplayName(), 'off', null) == true ? masterToggle.off() : log.debug('Invariants Violation!')
        }
    }
}

def getChildAppDevices() {
    return settings 
}

