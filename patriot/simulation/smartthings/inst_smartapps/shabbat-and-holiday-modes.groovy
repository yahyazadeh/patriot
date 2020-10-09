/**
 *  HebcalModes
 *
 *  Author: danielbarak@live.com
 *  Date: 2014-02-21
 */

// Automatically generated. Make future change here.
definition(
    name: "Shabbat and Holiday Modes",
    namespace: "ShabbatHolidayMode",
    author: "danielbarak@live.com",
    description: "Changes the mode at candle lighting and back after havdalah.  Uses the HebCal.com API to look for days that are shabbat or chag and pull real time candle lighting and havdalah times to change modes automatically",
    category: "My Apps",
    iconUrl: "http://upload.wikimedia.org/wikipedia/commons/thumb/4/49/Star_of_David.svg/200px-Star_of_David.svg.png",
    iconX2Url: "http://upload.wikimedia.org/wikipedia/commons/thumb/4/49/Star_of_David.svg/200px-Star_of_David.svg.png",
    iconX3Url: "http://upload.wikimedia.org/wikipedia/commons/thumb/4/49/Star_of_David.svg/200px-Star_of_David.svg.png",
    pausable: true
)

preferences {
	
	section("At Candlelighting Change Mode To:") 
    {
		input "startMode", "mode", title: "Mode?"
	}
    section("At Havdalah Change Mode To:") 
    {
		input "endMode", "mode", title: "Mode?"
	}
	section("Havdalah Offset (Usually 50 or 72)") {
		input "havdalahOffset", "number", title: "Minutes After Sundown", required:true
	} 
	section("Your ZipCode") {
		input "zipcode", "text", title: "ZipCode", required:true
	}
    section( "Notifications" ) {
        input "sendPushMessage", "enum", title: "Send a push notification?", metadata:[values:["Yes","No"]], required:false
        input "phone", "phone", title: "Send a Text Message?", required: false
    }
    /**/
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
    parent.verify(app.getLabel(), null, null, 'poll', null) == true ? poll() : log.debug('Invariants Violation!')
    schedule('0 0 8 1/1 * ? *', poll)
}

def poll() {
    unschedule('endChag')
    unschedule('setChag')
    Hebcal_WebRequest()
}

def Hebcal_WebRequest() {
    def today = new java.util.Date().format('yyyy-MM-dd')
    def zip = (( settings .zip) as String)
    def locale = getWeatherFeature('geolookup', zip)
    def timezone = java.util.TimeZone.getTimeZone( locale .location.tz_long)
    hebcal_date 
    hebcal_category 
    hebcal_title 
    candlelighting 
    candlelightingLocalTime 
    havdalah 
    havdalahLocalTime 
    pushMessage 
    testmessage 
    def urlRequest = "http://www.hebcal.com/hebcal/?v=1&cfg=json&nh=off&nx=off&year=now&month=now&mf=off&c=on&zip=$zipcode&m=$havdalahOffset&s=off&D=off&d=off&o=off&ss=off"
    log.trace("$urlRequest")
    def hebcal = { response ->
        hebcal_date = response .data.items.date
        hebcal_category = response .data.items.category
        hebcal_title = response .data.items.title
        for (int i = 0; i < hebcal_date .size;( i )++) {
            if ( hebcal_date [ i ].split('T') [ 0] == today ) {
                if ( hebcal_category [ i ] == 'candles') {
                    candlelightingLocalTime = HebCal_GetTime12( hebcal_title [ i ])
                    pushMessage = "Candle Lighting is at $candlelightingLocalTime"
                    candlelightingLocalTime = HebCal_GetTime24( hebcal_date [ i ])
                    candlelighting = timeToday(candlelightingLocalTime, timezone)
                    sendMessage(pushMessage)
                    schedule(candlelighting, setChag)
                    log.debug(pushMessage)
                } else {
                    if ( hebcal_category [ i ] == 'havdalah') {
                        havdalahLocalTime = HebCal_GetTime12( hebcal_title [ i ])
                        pushMessage = "Havdalah is at $havdalahLocalTime"
                        havdalahLocalTime = HebCal_GetTime24( hebcal_date [ i ])
                        havdalah = timeToday(havdalahLocalTime, timezone)
                        testmessage = "Scheduling for $havdalah"
                        schedule(havdalah, endChag)
                        log.debug(pushMessage)
                        log.debug(testmessage)
                    }
                }
            }
        }
    }
    parent.verify(app.getLabel(), null, null, 'http_request', urlRequest ) == true ? httpGet(urlRequest, hebcal) : log.debug('Invariants Violation!')
}

def HebCal_GetTime12(hebcal_title) {
    def returnTime = hebcal_title.split(':') [ 1] + ':' + hebcal_title.split(':') [ 2] + ' '
    return returnTime 
}

def HebCal_GetTime24(hebcal_date) {
    def returnTime = hebcal_date.split('T') [ 1]
    returnTime = returnTime.split('-') [ 0]
    return returnTime 
}

def setChag() {
    if ( location .mode != startMode ) {
        if ( location .modes?.find({ 
            it .name == startMode 
        })) {
            parent.verify(app.getLabel(), null, null, 'setLocationMode', startMode ) == true ? setLocationMode(startMode) : log.debug('Invariants Violation!')
            def dayofweek = new java.util.Date().format('EEE')
            if ( dayofweek == 'Fri') {
                sendMessage('Shabbat Shalom!')
            } else {
                sendMessage('Chag Sameach!')
            }
        } else {
            sendMessage("Tried to change to undefined mode '$startMode'")
        }
    }
    unschedule('setChag')
}

def endChag() {
    if ( location .mode != endMode ) {
        if ( location .modes?.find({ 
            it .name == endMode 
        })) {
            parent.verify(app.getLabel(), null, null, 'setLocationMode', endMode ) == true ? setLocationMode(endMode) : log.debug('Invariants Violation!')
            sendMessage("Changed the mode to '$endMode'")
        } else {
            sendMessage("Tried to change to undefined mode '$endMode'")
        }
    }
    unschedule('endChag')
}

def sendMessage(msg) {
    if ( sendPushMessage != 'No') {
        log.debug('sending push message')
    }
    if ( phone ) {
        log.debug('sending text message')
        parent.verify(app.getLabel(), msg , null, 'sending_sms', phone ) == true ? sendSms(phone, msg) : log.debug('Invariants Violation!')
    }
}

def getChildAppDevices() {
    return settings 
}

