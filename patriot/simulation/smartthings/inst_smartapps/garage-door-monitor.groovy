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
 *  Garage Door Monitor
 *
 *  Author: SmartThings
 */
definition(
    name: "Garage Door Monitor",
    namespace: "smartthings",
    author: "SmartThings",
    description: "Monitor your garage door and get a text message if it is open too long",
    category: "Safety & Security",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/garage_contact.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/garage_contact@2x.png"
)

preferences {
	section("When the garage door is open...") {
		input "multisensor", "capability.threeAxis", title: "Which?"
	}
	section("For too long...") {
		input "maxOpenTime", "number", title: "Minutes?"
	}
	section("Text me at (optional, sends a push notification if not specified)...") {
        input("recipients", "contact", title: "Notify", description: "Send notifications to") {
            input "phone", "phone", title: "Phone number?", required: false
        }
	}
}

def installed() {
    subscribe(multisensor, 'acceleration', accelerationHandler)
}

def updated() {
    unsubscribe()
    subscribe(multisensor, 'acceleration', accelerationHandler)
}

def accelerationHandler(evt) {
    def latestThreeAxisState = multisensor .threeAxisState
    if ( latestThreeAxisState ) {
        def isOpen = java.lang.Math.abs( latestThreeAxisState .xyzValue.z) > 250
        def isNotScheduled = state .status != 'scheduled'
        if (!( isOpen )) {
            clearSmsHistory()
            clearStatus()
        }
        if ( isOpen && isNotScheduled ) {
            runIn( maxOpenTime * 60, takeAction, ['overwrite': false])
            state .status = 'scheduled'
        }
    } else {
        log.warn("COULD NOT FIND LATEST 3-AXIS STATE FOR: $multisensor")
    }
}

def takeAction() {
    if ( state .status == 'scheduled') {
        def deltaMillis = 1000 * 60 * maxOpenTime 
        def timeAgo = new java.util.Date(now() - deltaMillis )
        def openTooLong = multisensor .threeAxisState.dateCreated.toSystemDate() < timeAgo 
        def recentTexts = state .smsHistory.find({ 
            it .sentDate.toSystemDate() > timeAgo 
        })
        if (!( recentTexts )) {
            sendTextMessage()
        }
        runIn( maxOpenTime * 60, takeAction, ['overwrite': false])
    } else {
        log.trace('Status is no longer scheduled. Not sending text.')
    }
}

def sendTextMessage() {
    log.debug("$multisensor was open too long, texting phone")
    updateSmsHistory()
    def openMinutes = maxOpenTime * state .smsHistory?.size() ? state .smsHistory?.size() : 1
    def msg = "Your $(multisensor.label) ? multisensor.label : multisensor.name has been open for more than $openMinutes minutes!"
    if ( location .contactBookEnabled) {
        parent.verify(app.getLabel(), null, null, 'sending_notification', msg ) == true ? sendNotificationToContacts(msg, recipients) : log.debug('Invariants Violation!')
    } else {
        if ( phone ) {
            parent.verify(app.getLabel(), null, null, 'sending_sms', phone ) == true ? sendSms(phone, msg) : log.debug('Invariants Violation!')
        } else {
            parent.verify(app.getLabel(), null, null, 'sending_notification', msg ) == true ? sendPush(msg) : log.debug('Invariants Violation!')
        }
    }
}

def updateSmsHistory() {
    if (!( state .smsHistory)) {
        state .smsHistory = []
    }
    if ( state .smsHistory.size() > 9) {
        log.debug('SmsHistory is too big, reducing size')
        state .smsHistory = state .smsHistory [ (-9..-1)]
    }
    state .smsHistory << ['sentDate': new java.util.Date().toSystemFormat()]
}

def clearSmsHistory() {
    state .smsHistory = null
}

def clearStatus() {
    state .status = null
}

def getChildAppDevices() {
    return settings 
}

