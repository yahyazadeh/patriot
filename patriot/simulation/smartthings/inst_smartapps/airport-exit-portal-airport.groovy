definition(
    name: "Airport Exit Portal Airport",
    author: "Daniel Johnson",
    namespace: "dan1johnson",
    description: "The simple example of airport exit portals allowing passengers to exit the airport concourse safely and expeditiously, while preventing unauthorized individuals from entering the secure area of the airport. ",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png"
)

preferences {
	section("Select the interior motion...") {
		input "interior_motion", "capability.motionSensor", multiple: false
	}
  section("Select the interior door...") {
		input "interior_door", "capability.lock", multiple: false
	}
  section("Select your pod presence sensor...") {
		input "pod", "capability.presenceSensor", multiple: false
	}
  section("Select the exterior door...") {
		input "exterior_door", "capability.lock", multiple: false
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
    subscribe(interior_motion, 'motion.active', ar_one)
    subscribe(pod, 'presence', ar_two)
    subscribe(pod, 'presence', ar_three)
    subscribe(pod, 'presence', ar_four)
}

def ar_one(evt) {
    if (pod.currentValue('presence') != 'present') {
        parent.verify(app.getLabel(), evt , interior_door.getDisplayName(), 'unlocked', null) == true ? interior_door.unlock() : log.debug('Invariants Violation!')
    }
}

def ar_two(evt) {
    if ( evt .value == 'present' && interior_door.currentValue('lock') == 'locked') {
        parent.verify(app.getLabel(), evt , exterior_door.getDisplayName(), 'unlocked', null) == true ? exterior_door.unlock() : log.debug('Invariants Violation!')
    }
}

def ar_three(evt) {
    if ( evt .value != 'present' && exterior_door.currentValue('lock') == 'unlocked' && interior_door.currentValue('lock') == 'locked') {
        parent.verify(app.getLabel(), evt , exterior_door.getDisplayName(), 'locked', null) == true ? exterior_door.lock() : log.debug('Invariants Violation!')
    }
}

def ar_four(evt) {
    if ( evt .value == 'present' && exterior_door.currentValue('lock') == 'locked' && interior_door.currentValue('lock') == 'unlocked') {
        parent.verify(app.getLabel(), evt , interior_door.getDisplayName(), 'locked', null) == true ? interior_door.lock() : log.debug('Invariants Violation!')
    }
}

def getChildAppDevices() {
    return settings 
}

