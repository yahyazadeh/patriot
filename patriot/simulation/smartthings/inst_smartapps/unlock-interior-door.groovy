definition(
    name: "unlock-interior-door",
    namespace: "dan1johnson",
	parent: "dan1johnson:PolicyManager",
    author: "Daniel Johnson",
    description: "Unlock the interior door when a switch (i.e., a remote controller) turns off",
    category: "",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png"
)


preferences {
	section ("Device List:") {
    	input "interiorswitch", "capability.switch", required: true, title: "Use this switch:"
        input "interior", "capability.lock", required: true, title: "To control this door:"
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
    subscribe(interiorswitch, 'switch', switchHandler)
}

def switchHandler(evt) {
    if ( evt .value == 'off') {
        parent.verify(app.getLabel(), evt , interior.getDisplayName(), 'unlocked', null) == true ? interior.unlock() : log.debug('Invariants Violation!')
    }
}

def getChildAppDevices() {
    return settings 
}

