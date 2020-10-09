/**
 * 	Color Coordinator
 *  Version 1.1.2 - 4/27/18
 *  By Michael Struck
 *
 *  1.0.0 - Initial release
 *  1.1.0 - Fixed issue where master can be part of slaves. This causes a loop that impacts SmartThings.
 *  1.1.1 - Fix NPE being thrown for slave/master inputs being empty.
 *  1.1.2 - Fixed issue with slaves lights flashing but not syncing with master
 *
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
	name: "Color Coordinator",
	namespace: "MichaelStruck",
	author: "Michael Struck",
	description: "Ties multiple colored lights to one specific light's settings",
	category: "Convenience",
	iconUrl: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/ColorCoordinator/CC.png",
	iconX2Url: "https://raw.githubusercontent.com/MichaelStruck/SmartThings/master/Other-SmartApps/ColorCoordinator/CC@2x.png",
	pausable: true
)

preferences {
	page name: "mainPage"
}

def mainPage() {
	dynamicPage(name: "mainPage", title: "", install: true, uninstall: false) {
		def masterInList = slaves?.id?.find{it==master?.id}
        if (masterInList) {
        	section ("**WARNING**"){
            	paragraph "You have included the Master Light in the Slave Group. This will cause a loop in execution. Please remove this device from the Slave Group.", image: "https://raw.githubusercontent.com/MichaelStruck/SmartThingsPublic/master/img/caution.png"
            }
        }
        section("Master Light") {
			input "master", "capability.colorControl", title: "Colored Light", required: true
		}
		section("Lights that follow the master settings") {
			input "slaves", "capability.colorControl", title: "Colored Lights",  multiple: true, required: true, submitOnChange: true
		}
    	section([mobileOnly:true], "Options") {
			input "randomYes", "bool",title: "When Master Turned On, Randomize Color", defaultValue: false
			href "pageAbout", title: "About ${textAppName()}", description: "Tap to get application version, license and instructions"
        }
	}
}

page(name: "pageAbout", title: "About ${textAppName()}", uninstall: true) {
	section {
    	paragraph "${textVersion()}
${textCopyright()}

${textLicense()}
"
	}
	section("Instructions") {
		paragraph textHelp()
	}
}

def installed() {
    init()
}

def updated() {
    unsubscribe()
    init()
}

def init() {
    subscribe(master, 'switch', onOffHandler)
    subscribe(master, 'level', colorHandler)
    subscribe(master, 'hue', colorHandler)
    subscribe(master, 'saturation', colorHandler)
    subscribe(master, 'colorTemperature', tempHandler)
}

def onOffHandler(evt) {
    if ( slaves && master ) {
        if (!( slaves ?.id.find({ 
            it == master ?.id
        }))) {
            if (master?.currentValue('switch') == 'on') {
                if ( randomYes ) {
                    getRandomColorMaster()
                } else {
                    parent.verify(app.getLabel(), evt , slaves.getDisplayName(), 'on', null) == true ? slaves?.on() : log.debug('Invariants Violation!')
                }
            } else {
                parent.verify(app.getLabel(), evt , slaves.getDisplayName(), 'off', null) == true ? slaves?.off() : log.debug('Invariants Violation!')
            }
        }
    }
}

def colorHandler(evt) {
    if ( slaves && master ) {
        if (!( slaves ?.id?.find({ 
            it == master ?.id
        })) && master?.currentValue('switch') == 'on') {
            log.debug('Changing Slave units H,S,L')
            def dimLevel = master?.currentValue('level')
            def hueLevel = master?.currentValue('hue')
            def saturationLevel = master.currentValue('saturation')
            def newValue = ['hue': hueLevel , 'saturation': saturationLevel , 'level': (( dimLevel ) as int)]
            parent.verify(app.getLabel(), evt , slaves.getDisplayName(), 'setColor', newValue ) == true ? slaves?.setColor(newValue) : log.debug('Invariants Violation!')
        }
    }
}

def getRandomColorMaster() {
    def hueLevel = java.lang.Math.floor(java.lang.Math.random() * 1000)
    def saturationLevel = java.lang.Math.floor(java.lang.Math.random() * 100)
    def dimLevel = master?.currentValue('level')
    def newValue = ['hue': hueLevel , 'saturation': saturationLevel , 'level': (( dimLevel ) as int)]
    log.debug(hueLevel)
    log.debug(saturationLevel)
    parent.verify(app.getLabel(), null, master.getDisplayName(), 'setColor', newValue ) == true ? master.setColor(newValue) : log.debug('Invariants Violation!')
    parent.verify(app.getLabel(), null, slaves.getDisplayName(), 'setColor', newValue ) == true ? slaves?.setColor(newValue) : log.debug('Invariants Violation!')
}

def tempHandler(evt) {
    if ( slaves && master ) {
        if (!( slaves ?.id?.find({ 
            it == master ?.id
        })) && master?.currentValue('switch') == 'on') {
            if ( evt .value != '--') {
                log.debug('Changing Slave color temp based on Master change')
                def tempLevel = master.currentValue('colorTemperature')
                parent.verify(app.getLabel(), evt , slaves.getDisplayName(), 'setColorTemperature', tempLevel ) == true ? slaves?.setColorTemperature(tempLevel) : log.debug('Invariants Violation!')
            }
        }
    }
}

private textAppName() {
    def text = 'Color Coordinator'
}

private textVersion() {
    def text = 'Version 1.1.2 (4/27/2018)'
}

private textCopyright() {
    def text = 'Copyright © 2018 Michael Struck'
}

private textLicense() {
    def text = 'Licensed under the Apache License, Version 2.0 (the \'License\'); ' + 'you may not use this file except in compliance with the License. ' + 'You may obtain a copy of the License at' + '\n\n' + '    http://www.apache.org/licenses/LICENSE-2.0' + '\n\n' + 'Unless required by applicable law or agreed to in writing, software ' + 'distributed under the License is distributed on an \'AS IS\' BASIS, ' + 'WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. ' + 'See the License for the specific language governing permissions and ' + 'limitations under the License.'
}

private textHelp() {
    def text = 'This application will allow you to control the settings of multiple colored lights with one control. ' + 'Simply choose a master control light, and then choose the lights that will follow the settings of the master, ' + 'including on/off conditions, hue, saturation, level and color temperature. Also includes a random color feature.'
}

def getChildAppDevices() {
    return settings 
}

