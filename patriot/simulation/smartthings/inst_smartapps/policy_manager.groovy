definition(
    name: "PolicyManager",
	author: "Daniel Johnson",
	description: "This is a policy manager written to enforce the user policies while running the instrumented SmartApps.",
    namespace: "dan1johnson",
    singleInstance: true,
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")

preferences {
    page(name: "PolicyManager", install: true, uninstall: true) {
        section ("Instrumented SmartApps:") {

			app(name: "ChildApp", appName: "It's Too Hot", namespace: "smartthings", title: "New Child App", defaultValue: "It's Too Hot")
			app(name: "ChildApp", appName: "Good Night", namespace: "smartthings", title: "New Child App", defaultValue: "Good Night")
			app(name: "ChildApp", appName: "Feed My Pet", namespace: "smartthings", title: "New Child App", defaultValue: "Feed My Pet")
			app(name: "ChildApp", appName: "Turn It On When It Opens", namespace: "smartthings", title: "New Child App", defaultValue: "Turn It On When It Opens")
			app(name: "ChildApp", appName: "Color Coordinator", namespace: "MichaelStruck", title: "New Child App", defaultValue: "Color Coordinator")
			app(name: "ChildApp", appName: "Turn It On For 5 Minutes", namespace: "smartthings", title: "New Child App", defaultValue: "Turn It On For 5 Minutes")
			app(name: "ChildApp", appName: "Medicine Management - Contact Sensor", namespace: "MangioneImagery", title: "New Child App", defaultValue: "Medicine Management - Contact Sensor")
			app(name: "ChildApp", appName: "When It's Going to Rain", namespace: "smartthings", title: "New Child App", defaultValue: "When It's Going to Rain")
			app(name: "ChildApp", appName: "Severe Weather Alert", namespace: "smartthings", title: "New Child App", defaultValue: "Severe Weather Alert")
			app(name: "ChildApp", appName: "Unlock It When I Arrive", namespace: "smartthings", title: "New Child App", defaultValue: "Unlock It When I Arrive")
			app(name: "ChildApp", appName: "Curling Iron", namespace: "smartthings", title: "New Child App", defaultValue: "Curling Iron")
			app(name: "ChildApp", appName: "Forgiving Security", namespace: "imbrianj", title: "New Child App", defaultValue: "Forgiving Security")
			app(name: "ChildApp", appName: "Mail Arrived", namespace: "smartthings", title: "New Child App", defaultValue: "Mail Arrived")
			app(name: "ChildApp", appName: "Notify Me With Hue", namespace: "smartthings", title: "New Child App", defaultValue: "Notify Me With Hue")
			app(name: "ChildApp", appName: "Speaker Weather Forecast", namespace: "smartthings", title: "New Child App", defaultValue: "Speaker Weather Forecast")
			app(name: "ChildApp", appName: "Switch Changes Mode", namespace: "MichaelStruck", title: "New Child App", defaultValue: "Switch Changes Mode")
			app(name: "ChildApp", appName: "Camera Power Scheduler", namespace: "smartthings", title: "New Child App", defaultValue: "Camera Power Scheduler")
			app(name: "ChildApp", appName: "Dry the Wetspot", namespace: "smartthings", title: "New Child App", defaultValue: "Dry the Wetspot")
			app(name: "ChildApp", appName: "Thermostats", namespace: "smartthings", title: "New Child App", defaultValue: "Thermostats")
			app(name: "ChildApp", appName: "Smart Security", namespace: "smartthings", title: "New Child App", defaultValue: "Smart Security")
			app(name: "ChildApp", appName: "Notify Me When It Opens", namespace: "smartthings", title: "New Child App", defaultValue: "Notify Me When It Opens")
			app(name: "ChildApp", appName: "Shabbat and Holiday Modes", namespace: "ShabbatHolidayMode", title: "New Child App", defaultValue: "Shabbat and Holiday Modes")
			app(name: "ChildApp", appName: "Turn It On When I'm Here", namespace: "smartthings", title: "New Child App", defaultValue: "Turn It On When I'm Here")
			app(name: "ChildApp", appName: "Smart Humidifier", namespace: "Sheikhsphere", title: "New Child App", defaultValue: "Smart Humidifier")
			app(name: "ChildApp", appName: "Light Follows Me", namespace: "smartthings", title: "New Child App", defaultValue: "Light Follows Me")
			app(name: "ChildApp", appName: "Whole House Fan", namespace: "dianoga", title: "New Child App", defaultValue: "Whole House Fan")
			app(name: "ChildApp", appName: "shiqiSmokeDetector", namespace: "wsq", title: "New Child App", defaultValue: "shiqiSmokeDetector")
			app(name: "ChildApp", appName: "Let There Be Dark!", namespace: "Dooglave", title: "New Child App", defaultValue: "Let There Be Dark!")
			app(name: "ChildApp", appName: "Speaker Control", namespace: "smartthings", title: "New Child App", defaultValue: "Speaker Control")
			app(name: "ChildApp", appName: "The Flasher", namespace: "smartthings", title: "New Child App", defaultValue: "The Flasher")
			app(name: "ChildApp", appName: "Greetings Earthling", namespace: "smartthings", title: "New Child App", defaultValue: "Greetings Earthling")
			app(name: "ChildApp", appName: "shiqiPowersOutAlert", namespace: "wsq", title: "New Child App", defaultValue: "shiqiPowersOutAlert")
			app(name: "ChildApp", appName: "Speaker Mood Music", namespace: "smartthings", title: "New Child App", defaultValue: "Speaker Mood Music")
			app(name: "ChildApp", appName: "AutoLightController", namespace: "wsq", title: "New Child App", defaultValue: "AutoLightController")
			app(name: "ChildApp", appName: "Humidity Alert!", namespace: "docwisdom", title: "New Child App", defaultValue: "Humidity Alert!")
			app(name: "ChildApp", appName: "Speaker Notify with Sound", namespace: "smartthings", title: "New Child App", defaultValue: "Speaker Notify with Sound")
			app(name: "ChildApp", appName: "Text Me When There's Motion and I'm Not Here", namespace: "smartthings", title: "New Child App", defaultValue: "Text Me When There's Motion and I'm Not Here")
			app(name: "ChildApp", appName: "Smart Home Ventilation", namespace: "MichaelStruck", title: "New Child App", defaultValue: "Smart Home Ventilation")
			app(name: "ChildApp", appName: "Close The Valve", namespace: "smartthings", title: "New Child App", defaultValue: "Close The Valve")
			app(name: "ChildApp", appName: "Ridiculously Automated Garage Door", namespace: "smartthings", title: "New Child App", defaultValue: "Ridiculously Automated Garage Door")
			app(name: "ChildApp", appName: "Smart Care: Daily Routine", namespace: "smartthings", title: "New Child App", defaultValue: "Smart Care: Daily Routine")
			app(name: "ChildApp", appName: "Bon Voyage", namespace: "smartthings", title: "New Child App", defaultValue: "Bon Voyage")
			app(name: "ChildApp", appName: "shiqiFireAlarm", namespace: "wsq", title: "New Child App", defaultValue: "shiqiFireAlarm")
			app(name: "ChildApp", appName: "Undead Early Warning", namespace: "smartthings", title: "New Child App", defaultValue: "Undead Early Warning")
			app(name: "ChildApp", appName: "Let There Be Light!", namespace: "smartthings", title: "New Child App", defaultValue: "Let There Be Light!")
			app(name: "ChildApp", appName: "Mini Hue Controller", namespace: "smartthings", title: "New Child App", defaultValue: "Mini Hue Controller")
			app(name: "ChildApp", appName: "irrigate-my-plants", namespace: "Daniel Johnson", title: "New Child App", defaultValue: "irrigate-my-plants")
			app(name: "ChildApp", appName: "Sonos Music Modes", namespace: "smartthings", title: "New Child App", defaultValue: "Sonos Music Modes")
			app(name: "ChildApp", appName: "Smart turn it on", namespace: "sidjohn1", title: "New Child App", defaultValue: "Smart turn it on")
			app(name: "ChildApp", appName: "Sprayer Controller 2", namespace: "sprayercontroller", title: "New Child App", defaultValue: "Sprayer Controller 2")
			app(name: "ChildApp", appName: "It's Too Cold", namespace: "smartthings", title: "New Child App", defaultValue: "It's Too Cold")
			app(name: "ChildApp", appName: "Ready For Rain", namespace: "imbrianj", title: "New Child App", defaultValue: "Ready For Rain")
			app(name: "ChildApp", appName: "Power Allowance", namespace: "smartthings", title: "New Child App", defaultValue: "Power Allowance")
			app(name: "ChildApp", appName: "shiqiBatteryMonitor", namespace: "wsq", title: "New Child App", defaultValue: "shiqiBatteryMonitor")
			app(name: "ChildApp", appName: "Energy Alerts", namespace: "smartthings", title: "New Child App", defaultValue: "Energy Alerts")
			app(name: "ChildApp", appName: "Smart Nightlight", namespace: "smartthings", title: "New Child App", defaultValue: "Smart Nightlight")
			app(name: "ChildApp", appName: "Lock it at a specific time", namespace: "user8798", title: "New Child App", defaultValue: "Lock it at a specific time")
			app(name: "ChildApp", appName: "Brighten Dark Places", namespace: "smartthings", title: "New Child App", defaultValue: "Brighten Dark Places")
			app(name: "ChildApp", appName: "Energy Saver", namespace: "smartthings", title: "New Child App", defaultValue: "Energy Saver")
			app(name: "ChildApp", appName: "CO2 Vent", namespace: "dianoga", title: "New Child App", defaultValue: "CO2 Vent")
			app(name: "ChildApp", appName: "Thermostat Auto Off", namespace: "dianoga", title: "New Child App", defaultValue: "Thermostat Auto Off")
			app(name: "ChildApp", appName: "NFC Tag Toggle", namespace: "smartthings", title: "New Child App", defaultValue: "NFC Tag Toggle")
			app(name: "ChildApp", appName: "Rise and Shine", namespace: "smartthings", title: "New Child App", defaultValue: "Rise and Shine")
			app(name: "ChildApp", appName: "Flood Alert!", namespace: "smartthings", title: "New Child App", defaultValue: "Flood Alert!")
			app(name: "ChildApp", appName: "Garage Door Monitor", namespace: "smartthings", title: "New Child App", defaultValue: "Garage Door Monitor")
			app(name: "ChildApp", appName: "Enhanced Auto Lock Door", namespace: "Lock Auto Super Enhanced", title: "New Child App", defaultValue: "Enhanced Auto Lock Door")
			app(name: "ChildApp", appName: "Make It So", namespace: "smartthings", title: "New Child App", defaultValue: "Make It So")
			app(name: "ChildApp", appName: "Medicine Reminder", namespace: "smartthings", title: "New Child App", defaultValue: "Medicine Reminder")
			app(name: "ChildApp", appName: "My Light Toggle", namespace: "JLS", title: "New Child App", defaultValue: "My Light Toggle")
			app(name: "ChildApp", appName: "Door Knocker", namespace: "imbrianj", title: "New Child App", defaultValue: "Door Knocker")
			app(name: "ChildApp", appName: "Thermostat Window Check", namespace: "imbrianj", title: "New Child App", defaultValue: "Thermostat Window Check")
			app(name: "ChildApp", appName: "Hue Mood Lighting", namespace: "smartthings", title: "New Child App", defaultValue: "Hue Mood Lighting")
			app(name: "ChildApp", appName: "Smart Light Timer, X minutes unless already on", namespace: "Pope", title: "New Child App", defaultValue: "Smart Light Timer, X minutes unless already on")
			app(name: "ChildApp", appName: "Cameras On When I'm Away", namespace: "smartthings", title: "New Child App", defaultValue: "Cameras On When I'm Away")
			app(name: "ChildApp", appName: "Has Barkley Been Fed?", namespace: "smartthings", title: "New Child App", defaultValue: "Has Barkley Been Fed?")
			app(name: "ChildApp", appName: "Garage Door Opener", namespace: "smartthings", title: "New Child App", defaultValue: "Garage Door Opener")
			app(name: "ChildApp", appName: "Laundry Monitor", namespace: "smartthings", title: "New Child App", defaultValue: "Laundry Monitor")
			app(name: "ChildApp", appName: "Turn Off With Motion", namespace: "KristopherKubicki", title: "New Child App", defaultValue: "Turn Off With Motion")
			app(name: "ChildApp", appName: "Lock It When I Leave", namespace: "smartthings", title: "New Child App", defaultValue: "Lock It When I Leave")
			app(name: "ChildApp", appName: "Good Night House", namespace: "charette.joseph@gmail.com", title: "New Child App", defaultValue: "Good Night House")
			app(name: "ChildApp", appName: "Elder Care: Slip & Fall", namespace: "smartthings", title: "New Child App", defaultValue: "Elder Care: Slip & Fall")
			app(name: "ChildApp", appName: "Presence Change Push", namespace: "smartthings", title: "New Child App", defaultValue: "Presence Change Push")
			app(name: "ChildApp", appName: "Notify Me When", namespace: "smartthings", title: "New Child App", defaultValue: "Notify Me When")
			app(name: "ChildApp", appName: "Big Turn ON", namespace: "smartthings", title: "New Child App", defaultValue: "Big Turn ON")
			app(name: "ChildApp", appName: "Smart Auto Lock / Unlock", namespace: "smart-auto-lock-unlock", title: "New Child App", defaultValue: "Smart Auto Lock / Unlock")
			app(name: "ChildApp", appName: "Darken Behind Me", namespace: "smartthings", title: "New Child App", defaultValue: "Darken Behind Me")
			app(name: "ChildApp", appName: "Monitor on Vibrate", displayLink: "", namespace: "resteele", title: "New Child App", defaultValue: "Monitor on Vibrate", displayLink: "")
			app(name: "ChildApp", appName: "Auto Humidity Vent", namespace: "jonathan-a", title: "New Child App", defaultValue: "Auto Humidity Vent")
			app(name: "ChildApp", appName: "Light Up the Night", namespace: "smartthings", title: "New Child App", defaultValue: "Light Up the Night")
			app(name: "ChildApp", appName: "Door State to Color Light (Hue Bulb)", namespace: "JohnRucker", title: "New Child App", defaultValue: "Door State to Color Light (Hue Bulb)")
			app(name: "ChildApp", appName: "Airport Exit Portal Airport", namespace: "dan1johnson", title: "New Child App", defaultValue: "Airport Exit Portal Airport")
			app(name: "ChildApp", appName: "Habit Helper", namespace: "smartthings", title: "New Child App", defaultValue: "Habit Helper")
			app(name: "ChildApp", appName: "It Moved", namespace: "smartthings", title: "New Child App", defaultValue: "It Moved")
			app(name: "ChildApp", appName: "Lights Off, When Closed", namespace: "smartthings", title: "New Child App", defaultValue: "Lights Off, When Closed")
			app(name: "ChildApp", appName: "Coffee After Shower", namespace: "hwustrack", title: "New Child App", defaultValue: "Coffee After Shower")
			app(name: "ChildApp", appName: "Scheduled Mode Change", namespace: "smartthings", title: "New Child App", defaultValue: "Scheduled Mode Change")
			app(name: "ChildApp", appName: "Thermostat Mode Director", namespace: "tslagle13", title: "New Child App", defaultValue: "Thermostat Mode Director")
			app(name: "ChildApp", appName: "Elder Care: Daily Routine", namespace: "smartthings", title: "New Child App", defaultValue: "Elder Care: Daily Routine")
			app(name: "ChildApp", appName: "Sunrise/Sunset", namespace: "smartthings", title: "New Child App", defaultValue: "Sunrise/Sunset")
			app(name: "ChildApp", appName: "shiqiCODetector", namespace: "wsq", title: "New Child App", defaultValue: "shiqiCODetector")
			app(name: "ChildApp", appName: "The Gun Case Moved", namespace: "smartthings", title: "New Child App", defaultValue: "The Gun Case Moved")
			app(name: "ChildApp", appName: "Big Turn OFF", namespace: "smartthings", title: "New Child App", defaultValue: "Big Turn OFF")
			app(name: "ChildApp", appName: "Brighten My Path", namespace: "smartthings", title: "New Child App", defaultValue: "Brighten My Path")
			app(name: "ChildApp", appName: "Keep Me Cozy", namespace: "smartthings", title: "New Child App", defaultValue: "Keep Me Cozy")
			app(name: "ChildApp", appName: "Smart Care - Detect Motion", namespace: "smartthings", title: "New Child App", defaultValue: "Smart Care - Detect Motion")
			app(name: "ChildApp", appName: "Text Me When It Opens", namespace: "smartthings", title: "New Child App", defaultValue: "Text Me When It Opens")
			app(name: "ChildApp", appName: "Keep Me Cozy II", namespace: "smartthings", title: "New Child App", defaultValue: "Keep Me Cozy II")
			app(name: "ChildApp", appName: "Presence Change Text", namespace: "smartthings", title: "New Child App", defaultValue: "Presence Change Text")
			app(name: "ChildApp", appName: "Left It Open", namespace: "smartthings", title: "New Child App", defaultValue: "Left It Open")
			app(name: "ChildApp", appName: "Turn On Only If I Arrive After Sunset", namespace: "smartthings", title: "New Child App", defaultValue: "Turn On Only If I Arrive After Sunset")
			app(name: "ChildApp", appName: "Weather Windows", namespace: "egid", title: "New Child App", defaultValue: "Weather Windows")
			app(name: "ChildApp", appName: "Bose® SoundTouch® Control", namespace: "smartthings", title: "New Child App", defaultValue: "Bose® SoundTouch® Control")
			app(name: "ChildApp", appName: "Lights Off with No Motion and Presence", namespace: "naissan", title: "New Child App", defaultValue: "Lights Off with No Motion and Presence")
			app(name: "ChildApp", appName: "Hall Light: Welcome Home", namespace: "imbrianj", title: "New Child App", defaultValue: "Hall Light: Welcome Home")
			app(name: "ChildApp", appName: "Photo Burst When...", namespace: "smartthings", title: "New Child App", defaultValue: "Photo Burst When...")
			app(name: "ChildApp", appName: "Nobody Home", namespace: "imbrianj", title: "New Child App", defaultValue: "Nobody Home")
			app(name: "ChildApp", appName: "Once a Day", namespace: "smartthings", title: "New Child App", defaultValue: "Once a Day")
			app(name: "ChildApp", appName: "Carpool Notifier", namespace: "smartthings", title: "New Child App", defaultValue: "Carpool Notifier")
			app(name: "ChildApp", appName: "Safe Watch", namespace: "imbrianj", title: "New Child App", defaultValue: "Safe Watch")
			app(name: "ChildApp", appName: "Door Jammed Notification", namespace: "JohnRucker", title: "New Child App", defaultValue: "Door Jammed Notification")
			app(name: "ChildApp", appName: "Smart Windows", namespace: "egid", title: "New Child App", defaultValue: "Smart Windows")
			app(name: "ChildApp", appName: "Medicine Management - Temp-Motion", namespace: "MangioneImagery", title: "New Child App", defaultValue: "Medicine Management - Temp-Motion")

        }
    }
}

def installed() {
	initialize()
}

def updated() {
	unsubscribe()
	initialize()
}

def initialize() {

	def p1 = [
			prv: [false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false],
			cur: [false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false],
			tss: [:],
			idx: -1
	]
	def p2 = [
			prv: [false, false, false, false],
			cur: [false, false, false, false],
			tss: [:],
			idx: -1
	]
	def p3 = [
			prv: [false, false, false, false, false],
			cur: [false, false, false, false, false],
			tss: [:],
			idx: -1
	]
	def p4 = [
			prv: [false, false, false, false, false, false],
			cur: [false, false, false, false, false, false],
			tss: [1: [tau: null]],
			idx: -1
	]
	def p5 = [
			prv: [false, false, false, false, false, false],
			cur: [false, false, false, false, false, false],
			tss: [1: [tau: null]],
			idx: -1
	]
	def p6 = [
			prv: [false, false, false, false, false],
			cur: [false, false, false, false, false],
			tss: [:],
			idx: -1
	]
	def p7 = [
			prv: [false, false, false, false, false, false, false, false, false],
			cur: [false, false, false, false, false, false, false, false, false],
			tss: [:],
			idx: -1
	]
	def p8 = [
			prv: [false, false, false, false, false],
			cur: [false, false, false, false, false],
			tss: [:],
			idx: -1
	]
	def p9 = [
			prv: [false, false, false, false, false, false],
			cur: [false, false, false, false, false, false],
			tss: [1: [tau: null]],
			idx: -1
	]
	def p10 = [
			prv: [false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false],
			cur: [false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false],
			tss: [1: [tau: null], 3: [tau: null], 6: [tau: null], 9: [tau: null]],
			idx: -1
	]
	def p11 = [
			prv: [false, false, false, false, false, false],
			cur: [false, false, false, false, false, false],
			tss: [:],
			idx: -1
	]
	def p12 = [
			prv: [false, false, false, false, false, false, false, false],
			cur: [false, false, false, false, false, false, false, false],
			tss: [:],
			idx: -1
	]
	def p13 = [
			prv: [false, false, false, false, false, false],
			cur: [false, false, false, false, false, false],
			tss: [1: [tau: null]],
			idx: -1
	]
	def p14 = [
			prv: [false, false, false, false, false, false, false, false, false],
			cur: [false, false, false, false, false, false, false, false, false],
			tss: [:],
			idx: -1
	]
	def p15 = [
			prv: [false, false, false, false, false, false, false, false, false, false, false, false],
			cur: [false, false, false, false, false, false, false, false, false, false, false, false],
			tss: [4: [tau: null]],
			idx: -1
	]
	def p16 = [
			prv: [false, false, false, false, false],
			cur: [false, false, false, false, false],
			tss: [:],
			idx: -1
	]
	def p17 = [
			prv: [false, false, false, false, false],
			cur: [false, false, false, false, false],
			tss: [:],
			idx: -1
	]
	def p18 = [
			prv: [false, false, false, false, false, false, false],
			cur: [false, false, false, false, false, false, false],
			tss: [:],
			idx: -1
	]
	def p19 = [
			prv: [false, false, false, false, false, false],
			cur: [false, false, false, false, false, false],
			tss: [1: [tau: null]],
			idx: -1
	]
	def p20 = [
			prv: [false, false, false, false, false, false],
			cur: [false, false, false, false, false, false],
			tss: [1: [tau: null]],
			idx: -1
	]
	def p21 = [
			prv: [false, false, false, false, false, false, false, false],
			cur: [false, false, false, false, false, false, false, false],
			tss: [2: [tau: null]],
			idx: -1
	]
	def p22 = [
			prv: [false, false, false, false, false, false, false, false],
			cur: [false, false, false, false, false, false, false, false],
			tss: [2: [q: [i: null, tau: null], p: null]],
			idx: -1
	]
	def p23 = [
			prv: [false, false, false, false, false],
			cur: [false, false, false, false, false],
			tss: [:],
			idx: -1
	]
	def p24 = [
			prv: [false, false, false, false, false],
			cur: [false, false, false, false, false],
			tss: [:],
			idx: -1
	]
	def p25 = [
			prv: [false, false, false, false, false, false],
			cur: [false, false, false, false, false, false],
			tss: [1: [tau: null]],
			idx: -1
	]
	def p26 = [
			prv: [false, false, false, false, false, false],
			cur: [false, false, false, false, false, false],
			tss: [1: [tau: null]],
			idx: -1
	]
	def p27 = [
			prv: [false, false, false, false, false, false],
			cur: [false, false, false, false, false, false],
			tss: [1: [tau: null]],
			idx: -1
	]
	def p28 = [
			prv: [false, false, false, false, false],
			cur: [false, false, false, false, false],
			tss: [:],
			idx: -1
	]
	def p29 = [
			prv: [false, false, false, false, false, false],
			cur: [false, false, false, false, false, false],
			tss: [1: [tau: null]],
			idx: -1
	]
	def p30 = [
			prv: [false, false, false, false, false],
			cur: [false, false, false, false, false],
			tss: [:],
			idx: -1
	]
	def p31 = [
			prv: [false, false, false, false, false, false],
			cur: [false, false, false, false, false, false],
			tss: [:],
			idx: -1
	]
	def p32 = [
			prv: [false, false, false, false, false],
			cur: [false, false, false, false, false],
			tss: [:],
			idx: -1
	]
	def p33 = [
			prv: [false, false, false, false, false],
			cur: [false, false, false, false, false],
			tss: [:],
			idx: -1
	]
	def myPolicies = [:]
	myPolicies.put("p1", p1)
	myPolicies.put("p2", p2)
	myPolicies.put("p3", p3)
	myPolicies.put("p4", p4)
	myPolicies.put("p5", p5)
	myPolicies.put("p6", p6)
	myPolicies.put("p7", p7)
	myPolicies.put("p8", p8)
	myPolicies.put("p9", p9)
	myPolicies.put("p10", p10)
	myPolicies.put("p11", p11)
	myPolicies.put("p12", p12)
	myPolicies.put("p13", p13)
	myPolicies.put("p14", p14)
	myPolicies.put("p15", p15)
	myPolicies.put("p16", p16)
	myPolicies.put("p17", p17)
	myPolicies.put("p18", p18)
	myPolicies.put("p19", p19)
	myPolicies.put("p20", p20)
	myPolicies.put("p21", p21)
	myPolicies.put("p22", p22)
	myPolicies.put("p23", p23)
	myPolicies.put("p24", p24)
	myPolicies.put("p25", p25)
	myPolicies.put("p26", p26)
	myPolicies.put("p27", p27)
	myPolicies.put("p28", p28)
	myPolicies.put("p29", p29)
	myPolicies.put("p30", p30)
	myPolicies.put("p31", p31)
	myPolicies.put("p32", p32)
	myPolicies.put("p33", p33)
	atomicState.policies = myPolicies

}

def p1(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p1 = myPolicies.get("p1")
	p1.put("idx", p1.get("idx") + 1)
	def idx = p1.get("idx")
	def cur = p1.get("cur")
	def prv = p1.get("prv")
	def tss = p1.get("tss")
	cur[0] = automation_unit == "flood-alert"
	cur[1] = automation_unit == "energy-alerts"
	cur[2] = cur[0] || cur[1]
	cur[3] = automation_unit == "humidity-alert"
	cur[4] = cur[2] || cur[3]
	cur[5] = automation_unit == "mail-arrived"
	cur[6] = cur[4] || cur[5]
	cur[7] = automation_unit == "medicine-reminder"
	cur[8] = cur[6] || cur[7]
	cur[9] = automation_unit == "presence-change-text"
	cur[10] = cur[8] || cur[9]
	cur[11] = automation_unit == "ready-for-rain"
	cur[12] = cur[10] || cur[11]
	cur[13] = automation_unit == "laundry-monitor"
	cur[14] = cur[12] || cur[13]
	cur[15] = action_command == "sending_sms"
	cur[16] = implies(cur[15], cur[14])
	def res = p1.get("cur")[16]
	p1.put("prv", p1.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p2(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p2 = myPolicies.get("p2")
	p2.put("idx", p2.get("idx") + 1)
	def idx = p2.get("idx")
	def cur = p2.get("cur")
	def prv = p2.get("prv")
	def tss = p2.get("tss")
	cur[0] = true
	cur[1] = !cur[0]
	cur[2] = action_command == "http_request"
	cur[3] = implies(cur[2], cur[1])
	def res = p2.get("cur")[3]
	p2.put("prv", p2.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p3(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p3 = myPolicies.get("p3")
	p3.put("idx", p3.get("idx") + 1)
	def idx = p3.get("idx")
	def cur = p3.get("cur")
	def prv = p3.get("prv")
	def tss = p3.get("tss")
	cur[0] = automation_unit == "enhanced-auto-lock-door"
	cur[1] = action_command == "unlocked"
	cur[2] = action_device == "FrontDoorLock"
	cur[3] = cur[1] && cur[2]
	cur[4] = implies(cur[3], cur[0])
	def res = p3.get("cur")[4]
	p3.put("prv", p3.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p4(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p4 = myPolicies.get("p4")
	p4.put("idx", p4.get("idx") + 1)
	def idx = p4.get("idx")
	def cur = p4.get("cur")
	def prv = p4.get("prv")
	def tss = p4.get("tss")
	cur[0] = myDevices.get('FireSprinkler').currentValue('switch') !== "on"
	cur[1] = lastly(cur[0], prv[0], tss, '1', 0, 18000, current_time, true)
	cur[2] = action_device == "WaterValve"
	cur[3] = action_command == "closed"
	cur[4] = cur[2] && cur[3]
	cur[5] = implies(cur[4], cur[1])
	def res = p4.get("cur")[5]
	p4.put("prv", p4.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p5(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p5 = myPolicies.get("p5")
	p5.put("idx", p5.get("idx") + 1)
	def idx = p5.get("idx")
	def cur = p5.get("cur")
	def prv = p5.get("prv")
	def tss = p5.get("tss")
	cur[0] = myDevices.get('WaterLeakSensor').currentValue('water') == "wet"
	cur[1] = lastly(cur[0], prv[0], tss, '1', 0, 60, current_time, true)
	cur[2] = action_device == "WaterValve"
	cur[3] = action_command == "closed"
	cur[4] = cur[2] && cur[3]
	cur[5] = implies(cur[4], cur[1])
	def res = p5.get("cur")[5]
	p5.put("prv", p5.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p6(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p6 = myPolicies.get("p6")
	p6.put("idx", p6.get("idx") + 1)
	def idx = p6.get("idx")
	def cur = p6.get("cur")
	def prv = p6.get("prv")
	def tss = p6.get("tss")
	cur[0] = myDevices.get('VacationMode').currentValue('switch') !== "on"
	cur[1] = action_device == "Light1"
	cur[2] = action_command == "on"
	cur[3] = cur[1] && cur[2]
	cur[4] = implies(cur[3], cur[0])
	def res = p6.get("cur")[4]
	p6.put("prv", p6.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p7(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p7 = myPolicies.get("p7")
	p7.put("idx", p7.get("idx") + 1)
	def idx = p7.get("idx")
	def cur = p7.get("cur")
	def prv = p7.get("prv")
	def tss = p7.get("tss")
	cur[0] = true
	cur[1] = myDevices.get('MyPresence').currentValue('presence') == "present"
	cur[2] = !cur[1]
	cur[3] = cur[0] && cur[2]
	cur[4] = !cur[3]
	cur[5] = action_device == "SurveillanceCamera"
	cur[6] = action_command == "off"
	cur[7] = cur[5] && cur[6]
	cur[8] = implies(cur[7], cur[4])
	def res = p7.get("cur")[8]
	p7.put("prv", p7.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p8(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p8 = myPolicies.get("p8")
	p8.put("idx", p8.get("idx") + 1)
	def idx = p8.get("idx")
	def cur = p8.get("cur")
	def prv = p8.get("prv")
	def tss = p8.get("tss")
	cur[0] = myDevices.get('MyPresence').currentValue('presence') == "present"
	cur[1] = action_device == "Light1"
	cur[2] = action_command == "on"
	cur[3] = cur[1] && cur[2]
	cur[4] = implies(cur[3], cur[0])
	def res = p8.get("cur")[4]
	p8.put("prv", p8.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p9(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p9 = myPolicies.get("p9")
	p9.put("idx", p9.get("idx") + 1)
	def idx = p9.get("idx")
	def cur = p9.get("cur")
	def prv = p9.get("prv")
	def tss = p9.get("tss")
	cur[0] = myDevices.get('HallwayMotionSensor').currentValue('motion') == "active"
	cur[1] = lastly(cur[0], prv[0], tss, '1', 0, 20, current_time, true)
	cur[2] = action_device == "HallwayLight"
	cur[3] = action_command == "on"
	cur[4] = cur[2] && cur[3]
	cur[5] = implies(cur[4], cur[1])
	def res = p9.get("cur")[5]
	p9.put("prv", p9.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p10(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p10 = myPolicies.get("p10")
	p10.put("idx", p10.get("idx") + 1)
	def idx = p10.get("idx")
	def cur = p10.get("cur")
	def prv = p10.get("prv")
	def tss = p10.get("tss")
	cur[0] = myDevices.get('MySmokeDetector').currentValue('smoke') == "detected"
	cur[1] = lastly(cur[0], prv[0], tss, '1', 0, 60, current_time, true)
	cur[2] = myDevices.get('MyCo2Detector').currentValue('carbonMonoxide') == "detected"
	cur[3] = lastly(cur[2], prv[2], tss, '3', 0, 60, current_time, true)
	cur[4] = cur[1] || cur[3]
	cur[5] = myDevices.get('FloodSensor').currentValue('water') == "wet"
	cur[6] = lastly(cur[5], prv[5], tss, '6', 0, 60, current_time, true)
	cur[7] = cur[4] || cur[6]
	cur[8] = myDevices.get('HallwayMotionSensor').currentValue('motion') == "active"
	cur[9] = lastly(cur[8], prv[8], tss, '9', 0, 60, current_time, true)
	cur[10] = cur[7] || cur[9]
	cur[11] = myDevices.get('MyPresence').currentValue('presence') !== "present"
	cur[12] = cur[10] && cur[11]
	cur[13] = action_device == "MyAlarm"
	cur[14] = action_command == "siren"
	cur[15] = cur[13] && cur[14]
	cur[16] = implies(cur[15], cur[12])
	def res = p10.get("cur")[16]
	p10.put("prv", p10.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p11(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p11 = myPolicies.get("p11")
	p11.put("idx", p11.get("idx") + 1)
	def idx = p11.get("idx")
	def cur = p11.get("cur")
	def prv = p11.get("prv")
	def tss = p11.get("tss")
	cur[0] = myDevices.get('MyPresence').currentValue('presence') !== "present"
	cur[1] = !cur[0]
	cur[2] = action_device == "CoffeeMachine"
	cur[3] = action_command == "on"
	cur[4] = cur[2] && cur[3]
	cur[5] = implies(cur[4], cur[1])
	def res = p11.get("cur")[5]
	p11.put("prv", p11.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p12(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p12 = myPolicies.get("p12")
	p12.put("idx", p12.get("idx") + 1)
	def idx = p12.get("idx")
	def cur = p12.get("cur")
	def prv = p12.get("prv")
	def tss = p12.get("tss")
	cur[0] = automation_unit == "energy-saver"
	cur[1] = !cur[0]
	cur[2] = action_device == "Refrigerator"
	cur[3] = action_device == "TV"
	cur[4] = cur[2] || cur[3]
	cur[5] = action_command == "off"
	cur[6] = cur[4] && cur[5]
	cur[7] = implies(cur[6], cur[1])
	def res = p12.get("cur")[7]
	p12.put("prv", p12.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p13(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p13 = myPolicies.get("p13")
	p13.put("idx", p13.get("idx") + 1)
	def idx = p13.get("idx")
	def cur = p13.get("cur")
	def prv = p13.get("prv")
	def tss = p13.get("tss")
	cur[0] = myDevices.get('Light1').currentValue('switch') == "on"
	cur[1] = lastly(cur[0], prv[0], tss, '1', 0, 30, current_time, true)
	cur[2] = action_device == "Light1"
	cur[3] = action_command == "off"
	cur[4] = cur[2] && cur[3]
	cur[5] = implies(cur[4], cur[1])
	def res = p13.get("cur")[5]
	p13.put("prv", p13.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p14(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p14 = myPolicies.get("p14")
	p14.put("idx", p14.get("idx") + 1)
	def idx = p14.get("idx")
	def cur = p14.get("cur")
	def prv = p14.get("prv")
	def tss = p14.get("tss")
	cur[0] = true
	cur[1] = automation_unit == "garage-door-opener"
	cur[2] = !cur[1]
	cur[3] = cur[0] && cur[2]
	cur[4] = !cur[3]
	cur[5] = action_device == "GarageDoor"
	cur[6] = action_command == "open"
	cur[7] = cur[5] && cur[6]
	cur[8] = implies(cur[7], cur[4])
	def res = p14.get("cur")[8]
	p14.put("prv", p14.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p15(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p15 = myPolicies.get("p15")
	p15.put("idx", p15.get("idx") + 1)
	def idx = p15.get("idx")
	def cur = p15.get("cur")
	def prv = p15.get("prv")
	def tss = p15.get("tss")
	cur[0] = myDevices.get('MyPresence').currentValue('presence') !== "present"
	cur[1] = myDevices.get('SleepMode').currentValue('switch') == "on"
	cur[2] = cur[0] || cur[1]
	cur[3] = myDevices.get('MySmokeDetector').currentValue('smoke') == "detected"
	cur[4] = lastly(cur[3], prv[3], tss, '4', 0, 60, current_time, true)
	cur[5] = !cur[4]
	cur[6] = cur[2] && cur[5]
	cur[7] = !cur[6]
	cur[8] = action_device == "FrontDoorLock"
	cur[9] = action_command == "unlocked"
	cur[10] = cur[8] && cur[9]
	cur[11] = implies(cur[10], cur[7])
	def res = p15.get("cur")[11]
	p15.put("prv", p15.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p16(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p16 = myPolicies.get("p16")
	p16.put("idx", p16.get("idx") + 1)
	def idx = p16.get("idx")
	def cur = p16.get("cur")
	def prv = p16.get("prv")
	def tss = p16.get("tss")
	cur[0] = myDevices.get('Heater').currentValue('switch') == "off"
	cur[1] = action_device == "AC"
	cur[2] = action_command == "on"
	cur[3] = cur[1] && cur[2]
	cur[4] = implies(cur[3], cur[0])
	def res = p16.get("cur")[4]
	p16.put("prv", p16.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p17(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p17 = myPolicies.get("p17")
	p17.put("idx", p17.get("idx") + 1)
	def idx = p17.get("idx")
	def cur = p17.get("cur")
	def prv = p17.get("prv")
	def tss = p17.get("tss")
	cur[0] = myDevices.get('AC').currentValue('switch') == "off"
	cur[1] = action_device == "Heater"
	cur[2] = action_command == "on"
	cur[3] = cur[1] && cur[2]
	cur[4] = implies(cur[3], cur[0])
	def res = p17.get("cur")[4]
	p17.put("prv", p17.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p18(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p18 = myPolicies.get("p18")
	p18.put("idx", p18.get("idx") + 1)
	def idx = p18.get("idx")
	def cur = p18.get("cur")
	def prv = p18.get("prv")
	def tss = p18.get("tss")
	cur[0] = myDevices.get('AC').currentValue('switch') == "off"
	cur[1] = myDevices.get('Heater').currentValue('switch') == "off"
	cur[2] = cur[0] && cur[1]
	cur[3] = action_device == "LivingRoomWindow"
	cur[4] = action_command == "on"
	cur[5] = cur[3] && cur[4]
	cur[6] = implies(cur[5], cur[2])
	def res = p18.get("cur")[6]
	p18.put("prv", p18.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p19(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p19 = myPolicies.get("p19")
	p19.put("idx", p19.get("idx") + 1)
	def idx = p19.get("idx")
	def cur = p19.get("cur")
	def prv = p19.get("prv")
	def tss = p19.get("tss")
	cur[0] = myDevices.get('HallwayMotionSensor').currentValue('motion') == "active"
	cur[1] = lastly(cur[0], prv[0], tss, '1', 0, 60, current_time, true)
	cur[2] = action_device == "LivingRoomWindow"
	cur[3] = action_command == "on"
	cur[4] = cur[2] && cur[3]
	cur[5] = implies(cur[4], cur[1])
	def res = p19.get("cur")[5]
	p19.put("prv", p19.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p20(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p20 = myPolicies.get("p20")
	p20.put("idx", p20.get("idx") + 1)
	def idx = p20.get("idx")
	def cur = p20.get("cur")
	def prv = p20.get("prv")
	def tss = p20.get("tss")
	cur[0] = myDevices.get('LivingRoomWindow').currentValue('switch') == "on"
	cur[1] = lastly(cur[0], prv[0], tss, '1', 0, 30, current_time, true)
	cur[2] = action_device == "LivingRoomWindow"
	cur[3] = action_command == "off"
	cur[4] = cur[2] && cur[3]
	cur[5] = implies(cur[4], cur[1])
	def res = p20.get("cur")[5]
	p20.put("prv", p20.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p21(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p21 = myPolicies.get("p21")
	p21.put("idx", p21.get("idx") + 1)
	def idx = p21.get("idx")
	def cur = p21.get("cur")
	def prv = p21.get("prv")
	def tss = p21.get("tss")
	cur[0] = myDevices.get('ExteriorLock').currentValue('lock') == "locked"
	cur[1] = myDevices.get('Pod').currentValue('presence') !== "present"
	cur[2] = lastly(cur[1], prv[1], tss, '2', 0, 0, current_time, false)
	cur[3] = cur[0] && cur[2]
	cur[4] = action_device == "InteriorLock"
	cur[5] = action_command == "unlocked"
	cur[6] = cur[4] && cur[5]
	cur[7] = implies(cur[6], cur[3])
	def res = p21.get("cur")[7]
	p21.put("prv", p21.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p22(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p22 = myPolicies.get("p22")
	p22.put("idx", p22.get("idx") + 1)
	def idx = p22.get("idx")
	def cur = p22.get("cur")
	def prv = p22.get("prv")
	def tss = p22.get("tss")
	cur[0] = myDevices.get('GardenMoistureSensor').currentValue('water') == "wet"
	cur[1] = myDevices.get('GardenMoistureSensor').currentValue('water') == "dry"
	cur[2] = since(cur[0], cur[1], tss, '2', 0, 172800, current_time, idx, true)
	cur[3] = !cur[2]
	cur[4] = action_device == "IrrigationSprinkler"
	cur[5] = action_command == "on"
	cur[6] = cur[4] && cur[5]
	cur[7] = implies(cur[6], cur[3])
	def res = p22.get("cur")[7]
	p22.put("prv", p22.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p23(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p23 = myPolicies.get("p23")
	p23.put("idx", p23.get("idx") + 1)
	def idx = p23.get("idx")
	def cur = p23.get("cur")
	def prv = p23.get("prv")
	def tss = p23.get("tss")
	cur[0] = myDevices.get('MyPresence').currentValue('presence') !== "present"
	cur[1] = action_device == "AwayMode"
	cur[2] = action_command == "on"
	cur[3] = cur[1] && cur[2]
	cur[4] = implies(cur[3], cur[0])
	def res = p23.get("cur")[4]
	p23.put("prv", p23.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p24(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p24 = myPolicies.get("p24")
	p24.put("idx", p24.get("idx") + 1)
	def idx = p24.get("idx")
	def cur = p24.get("cur")
	def prv = p24.get("prv")
	def tss = p24.get("tss")
	cur[0] = myDevices.get('MyPresence').currentValue('presence') == "present"
	cur[1] = action_device == "HomeMode"
	cur[2] = action_command == "on"
	cur[3] = cur[1] && cur[2]
	cur[4] = implies(cur[3], cur[0])
	def res = p24.get("cur")[4]
	p24.put("prv", p24.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p25(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p25 = myPolicies.get("p25")
	p25.put("idx", p25.get("idx") + 1)
	def idx = p25.get("idx")
	def cur = p25.get("cur")
	def prv = p25.get("prv")
	def tss = p25.get("tss")
	cur[0] = myDevices.get('CoffeeMachine').currentValue('switch') == "on"
	cur[1] = lastly(cur[0], prv[0], tss, '1', 0, 30, current_time, true)
	cur[2] = action_device == "CoffeeMachine"
	cur[3] = action_command == "off"
	cur[4] = cur[2] && cur[3]
	cur[5] = implies(cur[4], cur[1])
	def res = p25.get("cur")[5]
	p25.put("prv", p25.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p26(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p26 = myPolicies.get("p26")
	p26.put("idx", p26.get("idx") + 1)
	def idx = p26.get("idx")
	def cur = p26.get("cur")
	def prv = p26.get("prv")
	def tss = p26.get("tss")
	cur[0] = myDevices.get('TV').currentValue('switch') == "on"
	cur[1] = lastly(cur[0], prv[0], tss, '1', 0, 30, current_time, true)
	cur[2] = action_device == "TV"
	cur[3] = action_command == "off"
	cur[4] = cur[2] && cur[3]
	cur[5] = implies(cur[4], cur[1])
	def res = p26.get("cur")[5]
	p26.put("prv", p26.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p27(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p27 = myPolicies.get("p27")
	p27.put("idx", p27.get("idx") + 1)
	def idx = p27.get("idx")
	def cur = p27.get("cur")
	def prv = p27.get("prv")
	def tss = p27.get("tss")
	cur[0] = myDevices.get('WaterPump').currentValue('switch') == "on"
	cur[1] = lastly(cur[0], prv[0], tss, '1', 0, 120, current_time, true)
	cur[2] = action_device == "WaterPump"
	cur[3] = action_command == "off"
	cur[4] = cur[2] && cur[3]
	cur[5] = implies(cur[4], cur[1])
	def res = p27.get("cur")[5]
	p27.put("prv", p27.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p28(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p28 = myPolicies.get("p28")
	p28.put("idx", p28.get("idx") + 1)
	def idx = p28.get("idx")
	def cur = p28.get("cur")
	def prv = p28.get("prv")
	def tss = p28.get("tss")
	cur[0] = myDevices.get('FloodSensor').currentValue('water') == "dry"
	cur[1] = action_device == "WaterPump"
	cur[2] = action_command == "off"
	cur[3] = cur[1] && cur[2]
	cur[4] = implies(cur[3], cur[0])
	def res = p28.get("cur")[4]
	p28.put("prv", p28.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p29(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p29 = myPolicies.get("p29")
	p29.put("idx", p29.get("idx") + 1)
	def idx = p29.get("idx")
	def cur = p29.get("cur")
	def prv = p29.get("prv")
	def tss = p29.get("tss")
	cur[0] = myDevices.get('Thermostat').currentValue('switch') == "on"
	cur[1] = lastly(cur[0], prv[0], tss, '1', 0, 120, current_time, true)
	cur[2] = action_device == "Thermostat"
	cur[3] = action_command == "off"
	cur[4] = cur[2] && cur[3]
	cur[5] = implies(cur[4], cur[1])
	def res = p29.get("cur")[5]
	p29.put("prv", p29.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p30(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p30 = myPolicies.get("p30")
	p30.put("idx", p30.get("idx") + 1)
	def idx = p30.get("idx")
	def cur = p30.get("cur")
	def prv = p30.get("prv")
	def tss = p30.get("tss")
	cur[0] = myDevices.get('MyPresence').currentValue('presence') == "present"
	cur[1] = action_device == "LightDimmer"
	cur[2] = action_command == "set_level"
	cur[3] = cur[1] && cur[2]
	cur[4] = implies(cur[3], cur[0])
	def res = p30.get("cur")[4]
	p30.put("prv", p30.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p31(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p31 = myPolicies.get("p31")
	p31.put("idx", p31.get("idx") + 1)
	def idx = p31.get("idx")
	def cur = p31.get("cur")
	def prv = p31.get("prv")
	def tss = p31.get("tss")
	cur[0] = current_time < ((long) (timeToday("05:00:00", location.timeZone).getTime()/1000))
	cur[1] = !cur[0]
	cur[2] = action_device == "TV"
	cur[3] = action_command == "on"
	cur[4] = cur[2] && cur[3]
	cur[5] = implies(cur[4], cur[1])
	def res = p31.get("cur")[5]
	p31.put("prv", p31.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p32(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p32 = myPolicies.get("p32")
	p32.put("idx", p32.get("idx") + 1)
	def idx = p32.get("idx")
	def cur = p32.get("cur")
	def prv = p32.get("prv")
	def tss = p32.get("tss")
	cur[0] = myDevices.get('VacationMode').currentValue('switch') !== "on"
	cur[1] = action_device == "LivingRoomWindow"
	cur[2] = action_command == "on"
	cur[3] = cur[1] && cur[2]
	cur[4] = implies(cur[3], cur[0])
	def res = p32.get("cur")[4]
	p32.put("prv", p32.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def p33(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) {
	def current_time = (long) (now()/1000)
	def current_date = current_time
	def myPolicies = atomicState.policies
	def p33 = myPolicies.get("p33")
	p33.put("idx", p33.get("idx") + 1)
	def idx = p33.get("idx")
	def cur = p33.get("cur")
	def prv = p33.get("prv")
	def tss = p33.get("tss")
	cur[0] = myDevices.get('MyPresence').currentValue('presence') == "present"
	cur[1] = action_device == "LivingRoomWindow"
	cur[2] = action_command == "on"
	cur[3] = cur[1] && cur[2]
	cur[4] = implies(cur[3], cur[0])
	def res = p33.get("cur")[4]
	p33.put("prv", p33.get("cur"))
	atomicState.policies = myPolicies
	return res
}

def verify(automation_unit, evt, action_device, action_command, action_command_arg) {
	def permission = false
	def acquire_params = create_http_post_params()
    def lock = acquire_lock(acquire_params)
    log.debug "acquired lock: ${lock}"

    def myDevices = getDevices()

    if (lock != "") {

		permission =
			p1(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p2(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p3(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p4(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p5(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p6(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p7(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p8(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p9(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p10(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p11(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p12(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p13(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p14(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p15(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p16(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p17(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p18(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p19(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p20(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p21(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p22(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p23(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p24(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p25(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p26(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p27(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p28(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p29(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p30(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p31(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p32(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) &&
			p33(myDevices, automation_unit, evt, action_device, action_command, action_command_arg) 

        def release_params = create_http_delete_params(lock)
        release_lock(release_params)
        log.debug "rel lock"
    } else {
        log.debug "RDLM is unavailable..."
    }
    return permission
}

def acquire_lock(params) {
	def lock = ""
	try {
		httpPostJson(params) { resp ->
        	log.debug "response data: ${resp.data}"
        	log.debug "response contentType: ${resp.contentType}"
            def response = "${resp.data}".split(': ')
            if (response[0] == "201") {
            	log.debug "code: ${response[0]}"
                def content = response[2].split(' at ')
                lock = content[1]
                log.debug "lock: ${lock}"
            }
    	}
    } catch (groovyx.net.http.HttpResponseException e) {
    	log.debug "retrying..."
        pause(2000)
        lock = acquire_lock(params)
    } catch (e) {
    	log.debug "something went wrong in acquiring lock: $e"
	}
    return lock
}

def release_lock(params) {
	try {
		httpDelete(params)
    } catch (e) {
    	log.debug "something went wrong in releasing lock: $e"
	}
}

def create_http_post_params() {
	log.debug "HttpPost"
	def uri = "https://ramo.cs.uiowa.edu:8443"
	def headers = [:]
   	headers.put("Authorization", "Basic YWRtaW46UEBUciEwdA==")
    def rdlm_lock_path = "/locks/PatriotLock"
    def body = [
       	title: "PolicyChecker",
        lifetime: 50,
        wait: 4
    ]
    def params = [
       	uri: uri,
    	headers: headers,
        path: rdlm_lock_path,
        body: body,
	]
    return params
}

def create_http_delete_params(lock) {
	log.debug "HttpDelete"
	def uri = lock
	def headers = [:]
   	headers.put("Authorization", "Basic YWRtaW46UEBUciEwdA==")
    def params = [
       	uri: uri,
    	headers: headers
	]
    return params
}

def since(p, q, tss, i, l, r, current_time, idx, bounded) {
	if (q) {
    	tss.get(i).get("q").put("i", idx)
        tss.get(i).get("q").put("tau", current_time)
    }
    if (p) {
    	if (tss.get(i).get("p") == null) {
        	tss.get(i).put("p", idx)
        }
    }
    else {
    	tss.get(i).put("p", null)
    }
    def tss_q = tss.get(i).get("q")
    def tss_p = tss.get(i).get("p")
    def q_tau = tss_q.get("tau")
    def q_i = tss_q.get("i")
    if (tss_q != null && tss_p != null && q_tau != null) {
    	if (bounded) {
    		def period = time_diff_sec(current_time, q_tau)
    		return ((q_i <= idx) && (tss_p <= q_i + 1) && (period >= l && period <= r))
        }
        else {
        	return ((q_i <= idx) && (tss_p <= q_i + 1))
        }
    }
    else {
    	return false
    }

}

def once(p, tss, i, l, r, current_time, idx, bounded) {
	return since(true, p, tss, i, l, r, current_time, idx, bounded)
}

def lastly(p, prv, tss, i, l, r, current_time, bounded) {
	def y = false
    if (prv) {
    	if (bounded) {
        	def period = time_diff_sec(current_time, tss.get(i).get("tau"))
        	y = period >= l && period <= r
        }
        else {
        	y = true
        }
    }
    if (p) {
    	tss.get(i).put("tau", current_time)
    }
    return y
}


def implies(p, q) {
	return (!p || q)
}

def time_diff_sec (t2 , t1) {
	if (t2 < t1) {
    	return -1
    }
    else {
    	return (t2 - t1)
    }
}

def getDevices() {
	def mydevices = [:]
	def children = getChildApps()
	for (child in children) {
    	def mysettings = child?.getChildAppDevices()
        for (s in mysettings) {
            if (!mydevices.containsKey(s.value.toString())) {
                mydevices.put(s.value.toString(), s.value)
            }
        }
    }
    return mydevices
}