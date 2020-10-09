/**
 *  Smart Humidifier
 *
 *  Copyright 2014 Sheikh Dawood
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
    name: "Smart Humidifier",
    namespace: "Sheikhsphere",
    author: "Sheikh Dawood",
    description: "Turn on/off humidifier based on relative humidity from a sensor.",
    category: "Convenience",
    iconUrl: "https://graph.api.smartthings.com/api/devices/icons/st.Weather.weather12-icn",
    iconX2Url: "https://graph.api.smartthings.com/api/devices/icons/st.Weather.weather12-icn?displaySize=2x",
    pausable: true
)


preferences {
	section("Monitor the humidity of:") {
		input "humiditySensor1", "capability.relativeHumidityMeasurement"
	}
	section("When the humidity rises above:") {
		input "humidityHigh", "number", title: "Percentage ?"
	}
    section("When the humidity drops below:") {
		input "humidityLow", "number", title: "Percentage ?"
	}
    section("Control Humidifier:") {
		input "switch1", "capability.switch"
	}
    section( "Notifications" ) {
        input "sendPushMessage", "enum", title: "Send a push notification?", metadata:[values:["Yes","No"]], required:false
        input "phone1", "phone", title: "Send a Text Message?", required: false
    }
}

def installed() {
    subscribe(humiditySensor1, 'humidity', humidityHandler)
}

def updated() {
    unsubscribe()
    subscribe(humiditySensor1, 'humidity', humidityHandler)
}

def humidityHandler(evt) {
    log.trace("humidity: $evt.value")
    log.trace("set high point: $humidityHigh")
    log.trace("set low point: $humidityLow")
    def currentHumidity = java.lang.Double.parseDouble( evt .value.replace('%', ''))
    def humidityHigh1 = humidityHigh 
    def humidityLow1 = humidityLow 
    def mySwitch = settings .switch1
    if ( currentHumidity >= humidityHigh1 ) {
        log.debug("Checking how long the humidity sensor has been reporting >= $humidityHigh1")
        def deltaMinutes = 10
        def timeAgo = new java.util.Date(now() - 1000 * 60 * deltaMinutes .toLong())
        def recentEvents = humiditySensor1.eventsSince(timeAgo)
        log.trace("Found $(recentEvents?.size()) ? recentEvents?.size() : 0 events in the last $deltaMinutes minutes")
        def alreadySentSms1 = recentEvents.count({ 
            java.lang.Double.parseDouble( it .value.replace('%', '')) >= humidityHigh1 
        }) > 1
        if ( alreadySentSms1 ) {
            log.debug("Notification already sent within the last $deltaMinutes minutes")
        } else {
            if ( state .lastStatus != 'off') {
                log.debug("Humidity Rose Above $humidityHigh1:  sending SMS and deactivating $mySwitch")
                send("$humiditySensor1.label sensed high humidity level of $evt.value, turning off $switch1.label")
                parent.verify(app.getLabel(), evt , switch1.getDisplayName(), 'off', null) == true ? switch1?.off() : log.debug('Invariants Violation!')
                state .lastStatus = 'off'
            }
        }
    } else {
        if ( currentHumidity <= humidityLow1 ) {
            log.debug("Checking how long the humidity sensor has been reporting <= $humidityLow1")
            def deltaMinutes = 10
            def timeAgo = new java.util.Date(now() - 1000 * 60 * deltaMinutes .toLong())
            def recentEvents = humiditySensor1.eventsSince(timeAgo)
            log.trace("Found $(recentEvents?.size()) ? recentEvents?.size() : 0 events in the last $deltaMinutes minutes")
            def alreadySentSms2 = recentEvents.count({ 
                java.lang.Double.parseDouble( it .value.replace('%', '')) <= humidityLow1 
            }) > 1
            if ( alreadySentSms2 ) {
                log.debug("Notification already sent within the last $deltaMinutes minutes")
            } else {
                if ( state .lastStatus != 'on') {
                    log.debug("Humidity Dropped Below $humidityLow1:  sending SMS and activating $mySwitch")
                    send("$humiditySensor1.label sensed low humidity level of $evt.value, turning on $switch1.label")
                    parent.verify(app.getLabel(), evt , switch1.getDisplayName(), 'on', null) == true ? switch1?.on() : log.debug('Invariants Violation!')
                    state .lastStatus = 'on'
                }
            }
        } else {
        }
    }
}

private send(msg) {
    if ( sendPushMessage != 'No') {
        log.debug('sending push message')
        parent.verify(app.getLabel(), msg , null, 'sending_notification', msg ) == true ? sendPush(msg) : log.debug('Invariants Violation!')
    }
    if ( phone1 ) {
        log.debug('sending text message')
        parent.verify(app.getLabel(), msg , null, 'sending_sms', phone1 ) == true ? sendSms(phone1, msg) : log.debug('Invariants Violation!')
    }
    log.debug(msg)
}

def getChildAppDevices() {
    return settings 
}

