definition(
    name: "unlock-exterior-door",
    namespace: "dan1johnson",
	parent: "dan1johnson:PolicyManager",
    author: "Daniel Johnson",
    description: "Unlock the exterior door when a motion sensor trips",
    category: "",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png"
)


preferences {
	section ("Device List:") {
	    input "exterior", "capability.lock", required: true, title: "Unlock this door:"
		input "motion", "capability.motionSensor", required: true, title: "When this motion sensor trips:"
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(motion, "motion.active", motionHandler)
}

def motionHandler(evt) {
	exterior.unlock()
}

def getChildAppDevices() {
	return settings
}