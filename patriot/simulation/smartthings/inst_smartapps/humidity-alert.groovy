/**
 *  Its too humid!
 *
 *  Copyright 2014 Brian Critchlow
 *  Based on Its too cold code by SmartThings
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
    name: "Humidity Alert!",
    namespace: "docwisdom",
    author: "Brian Critchlow",
    description: "Notify me when the humidity rises above or falls below the given threshold. It will turn on a switch when it rises above the first threshold and off when it falls below the second threshold.",
    category: "Convenience",
    iconUrl: "https://graph.api.smartthings.com/api/devices/icons/st.Weather.weather9-icn",
    iconX2Url: "https://graph.api.smartthings.com/api/devices/icons/st.Weather.weather9-icn?displaySize=2x",
    pausable: true
)


preferences {
	section("Monitor the humidity of:") {
		input "humiditySensor1", "capability.relativeHumidityMeasurement"
	}
	section("When the humidity rises above:") {
		input "humidity1", "number", title: "Percentage ?"
	}
    section("When the humidity falls below:") {
		input "humidity2", "number", title: "Percentage ?"
	}
    section( "Notifications" ) {
        input "sendPushMessage", "enum", title: "Send a push notification?", metadata:[values:["Yes","No"]], required:false
        input "phone1", "phone", title: "Send a Text Message?", required: false
    }
	section("Control this switch:") {
		input "switch1", "capability.switch", required: false
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
    log.trace("set point: $humidity1")
    def currentHumidity = java.lang.Double.parseDouble( evt .value.replace('%', ''))
    def tooHumid = humidity1 
    def notHumidEnough = humidity2 
    def mySwitch = settings .switch1
    def deltaMinutes = 10
    def timeAgo = new java.util.Date(now() - 1000 * 60 * deltaMinutes .toLong())
    def recentEvents = humiditySensor1.eventsSince(timeAgo)
    log.trace("Found $(recentEvents?.size()) ? recentEvents?.size() : 0 events in the last $deltaMinutes minutes")
    def alreadySentSms = recentEvents.count({ 
        java.lang.Double.parseDouble( it .value.replace('%', '')) >= tooHumid 
    }) > 1 || recentEvents.count({ 
        java.lang.Double.parseDouble( it .value.replace('%', '')) <= notHumidEnough 
    }) > 1
    if ( currentHumidity >= tooHumid ) {
        log.debug("Checking how long the humidity sensor has been reporting >= $tooHumid")
        if ( alreadySentSms ) {
            log.debug("Notification already sent within the last $deltaMinutes minutes")
        } else {
            log.debug("Humidity Rose Above $tooHumid:  sending SMS and activating $mySwitch")
            send("$humiditySensor1.label sensed high humidity level of $evt.value")
            parent.verify(app.getLabel(), evt , switch1.getDisplayName(), 'on', null) == true ? switch1?.on() : log.debug('Invariants Violation!')
        }
    }
    if ( currentHumidity <= notHumidEnough ) {
        log.debug("Checking how long the humidity sensor has been reporting <= $notHumidEnough")
        if ( alreadySentSms ) {
            log.debug("Notification already sent within the last $deltaMinutes minutes")
        } else {
            log.debug("Humidity Fell Below $notHumidEnough:  sending SMS and activating $mySwitch")
            send("$humiditySensor1.label sensed high humidity level of $evt.value")
            parent.verify(app.getLabel(), evt , switch1.getDisplayName(), 'off', null) == true ? switch1?.off() : log.debug('Invariants Violation!')
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

