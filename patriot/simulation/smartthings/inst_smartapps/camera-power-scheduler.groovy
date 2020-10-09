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
 *  Schedule the Camera Power
 *
 *  Author: danny@smartthings.com
 *  Date: 2013-10-07
 */

definition(
    name: "Camera Power Scheduler",
    namespace: "smartthings",
    author: "SmartThings",
    description: "Turn the power on and off at a specific time. ",
    category: "Available Beta Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Partner/dropcam-on-off-schedule.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Partner/dropcam-on-off-schedule@2x.png"
)

preferences {
	section("Camera power..."){
		input "switch1", "capability.switch", multiple: true
	}
	section("Turn the Camera On at..."){
		input "startTime", "time", title: "Start Time", required:false
	}
	section("Turn the Camera Off at..."){
		input "endTime", "time", title: "End Time", required:false
	}    
}

def installed() {
    initialize()
}

def updated() {
    unschedule()
    initialize()
}

def initialize() {
    if ( startTime ) {
        runDaily(startTime, turnOnCamera)
    }
    if ( endTime ) {
        runDaily(endTime, turnOffCamera)
    }
}

def turnOnCamera() {
    log.info('turned on camera')
    parent.verify(app.getLabel(), null, switch1.getDisplayName(), 'on', null) == true ? switch1.on() : log.debug('Invariants Violation!')
}

def turnOffCamera() {
    log.info('turned off camera')
    parent.verify(app.getLabel(), null, switch1.getDisplayName(), 'off', null) == true ? switch1.off() : log.debug('Invariants Violation!')
}

def getChildAppDevices() {
    return settings 
}

