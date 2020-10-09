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
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(interior_motion, "motion.active", ar_one)
    subscribe(pod, "presence", ar_two)
    subscribe(pod, "presence", ar_three)
    subscribe(pod, "presence", ar_four)
}

def ar_one(evt) {
  if (pod.currentValue("presence") != "present")
  {
    interior_door.unlock()
  }
}


def ar_two(evt) {
  if (evt.value == "present" && interior_door.currentValue("lock") == "locked") {
    exterior_door.unlock()
  }
}
def ar_three(evt) {
  if (evt.value != "present" && exterior_door.currentValue("lock") == "unlocked" && interior_door.currentValue("lock") == "locked") {
    exterior_door.lock()
  }
}
def ar_four(evt) {
  if (evt.value == "present" && exterior_door.currentValue("lock") == "locked" && interior_door.currentValue("lock") == "unlocked") {
    interior_door.lock()
  }
}


def getChildAppDevices() {
	return settings
}