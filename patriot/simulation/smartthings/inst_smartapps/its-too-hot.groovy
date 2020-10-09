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
 *  It's Too Hot
 *
 *  Author: SmartThings
 */
definition(
    name: "It's Too Hot",
    namespace: "smartthings",
    author: "SmartThings",
    description: "Monitor the temperature and when it rises above your setting get a notification and/or turn on an A/C unit or fan.",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/its-too-hot.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/its-too-hot@2x.png"
)

preferences {
	section("Monitor the temperature...") {
		input "temperatureSensor1", "capability.temperatureMeasurement"
	}
	section("When the temperature rises above...") {
		input "temperature1", "number", title: "Temperature?"
	}
    section( "Notifications" ) {
        input("recipients", "contact", title: "Send notifications to") {
            input "sendPushMessage", "enum", title: "Send a push notification?", options: ["Yes", "No"], required: false
            input "phone1", "phone", title: "Send a Text Message?", required: false
        }
    }
	section("Turn on which A/C or fan...") {
		input "switch1", "capability.switch", required: false
	}
}

def installed() {
    subscribe(temperatureSensor1, 'temperature', temperatureHandler)
}

def updated() {
    unsubscribe()
    subscribe(temperatureSensor1, 'temperature', temperatureHandler)
}

def temperatureHandler(evt) {
    log.trace("temperature: $evt.value, $evt")
    def tooHot = temperature1 
    def mySwitch = settings .switch1
    if ( evt .doubleValue >= tooHot ) {
        log.debug("Checking how long the temperature sensor has been reporting <= $tooHot")
        def deltaMinutes = 10
        def timeAgo = new java.util.Date(now() - 1000 * 60 * deltaMinutes .toLong())
        def recentEvents = temperatureSensor1.eventsSince(timeAgo)?.findAll({ 
            it .name == 'temperature'
        })
        log.trace("Found $(recentEvents?.size()) ? recentEvents?.size() : 0 events in the last $deltaMinutes minutes")
        def alreadySentSms = recentEvents.count({ 
            it .doubleValue >= tooHot 
        }) > 1
        if ( alreadySentSms ) {
            log.debug("SMS already sent within the last $deltaMinutes minutes")
        } else {
            log.debug("Temperature rose above $tooHot:  sending SMS and activating $mySwitch")
            def tempScale = location .temperatureScale ? location .temperatureScale : 'F'
            send("$temperatureSensor1.displayName is too hot, reporting a temperature of $evt.value$(evt.unit) ? evt.unit : tempScale")
            parent.verify(app.getLabel(), evt , switch1.getDisplayName(), 'on', null) == true ? switch1?.on() : log.debug('Invariants Violation!')
        }
    }
}

private send(msg) {
    if ( location .contactBookEnabled) {
        log.debug("sending notifications to: $recipients?.size()")
        parent.verify(app.getLabel(), msg , null, 'sending_notification', msg ) == true ? sendNotificationToContacts(msg, recipients) : log.debug('Invariants Violation!')
    } else {
        if ( sendPushMessage != 'No') {
            log.debug('sending push message')
            parent.verify(app.getLabel(), msg , null, 'sending_notification', msg ) == true ? sendPush(msg) : log.debug('Invariants Violation!')
        }
        if ( phone1 ) {
            log.debug('sending text message')
            parent.verify(app.getLabel(), msg , null, 'sending_sms', phone1 ) == true ? sendSms(phone1, msg) : log.debug('Invariants Violation!')
        }
    }
    log.debug(msg)
}

def getChildAppDevices() {
    return settings 
}

