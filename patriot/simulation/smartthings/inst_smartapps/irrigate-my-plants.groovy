definition(
    name: "irrigate-my-plants",
    namespace: "Daniel Johnson",
    author: "dan1johnson",
    description: "Irrigates my plant when the garden moisture sensor is dry",
    category: "Safety & Security",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/SafetyAndSecurity/App-UndeadEarlyWarning.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/SafetyAndSecurity/App-UndeadEarlyWarning@2x.png"
)

preferences {
	section("When dryness is sensed by the moisture sensor...") {
		input "sensor", "capability.waterSensor", title: "Where?", required: true
	}
	section("Turn on the sprinkler...") {
		input "sprinkler", "capability.switch", title: "Which?", required: true
	}
}

def installed() {
    subscribe(sensor, 'water.dry', waterHandler)
    subscribe(sensor, 'water.wet', waterHandler)
}

def updated() {
    unsubscribe()
    subscribe(sensor, 'water.dry', waterHandler)
    subscribe(sensor, 'water.wet', waterHandler)
}

def waterHandler(evt) {
    log.debug("Sensor says $evt.value")
    if ( evt .value == 'wet') {
        parent.verify(app.getLabel(), evt , sprinkler.getDisplayName(), 'off', null) == true ? sprinkler.off() : log.debug('Invariants Violation!')
    } else {
        if ( evt .value == 'dry') {
            parent.verify(app.getLabel(), evt , sprinkler.getDisplayName(), 'on', null) == true ? sprinkler.on() : log.debug('Invariants Violation!')
        }
    }
}

def getChildAppDevices() {
    return settings 
}

