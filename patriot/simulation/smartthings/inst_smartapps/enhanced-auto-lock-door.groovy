definition(
    name: "Enhanced Auto Lock Door",
    namespace: "Lock Auto Super Enhanced",
    author: "Arnaud",
    description: "Automatically locks a specific door after X minutes when closed  and unlocks it when open after X seconds.",
    category: "Safety & Security",
    iconUrl: "http://www.gharexpert.com/mid/4142010105208.jpg",
    iconX2Url: "http://www.gharexpert.com/mid/4142010105208.jpg",
    pausable: true
)

preferences{
    page name: "mainPage", install: true, uninstall: true
}

def mainPage() {
    dynamicPage(name: "mainPage") {
        section("Select the door lock:") {
            input "lock1", "capability.lock", required: true
        }
        section("Select the door contact sensor:") {
            input "contact", "capability.contactSensor", required: true
        }
        section("Automatically lock the door when closed...") {
            input "minutesLater", "number", title: "Delay (in minutes):", required: true
        }
        section("Automatically unlock the door when open...") {
            input "secondsLater", "number", title: "Delay (in seconds):", required: true
        }
        if (location.contactBookEnabled || phoneNumber) {
            section("Notifications") {
                input("recipients", "contact", title: "Send notifications to", required: false) {
                    input "phoneNumber", "phone", title: "Warn with text message (optional)", description: "Phone Number", required: false
                }
            }
        }
        section([mobileOnly:true]) {
            label title: "Assign a name", required: false
            mode title: "Set for specific mode(s)"
        }
    }
}

def installed() {
    initialize()
}

def updated() {
    unsubscribe()
    unschedule()
    initialize()
}

def initialize() {
    log.debug("Settings: $settings")
    subscribe(lock1, 'lock', doorHandler, ['filterEvents': false])
    subscribe(lock1, 'unlock', doorHandler, ['filterEvents': false])
    subscribe(contact, 'contact.open', doorHandler)
    subscribe(contact, 'contact.closed', doorHandler)
}

def lockDoor() {
    log.debug('Locking the door.')
    parent.verify(app.getLabel(), null, lock1.getDisplayName(), 'locked', null) == true ? lock1.lock() : log.debug('Invariants Violation!')
    if ( location .contactBookEnabled) {
        if ( recipients ) {
            log.debug('Sending Push Notification...')
            parent.verify(app.getLabel(), null, null, 'sending_notification', "$lock1 locked after $contact was closed for $minutesLater minutes!") == true ? sendNotificationToContacts("$lock1 locked after $contact was closed for $minutesLater minutes!", recipients) : log.debug('Invariants Violation!')
        }
    }
    if ( phoneNumber ) {
        log.debug('Sending text message...')
        parent.verify(app.getLabel(), null, null, 'sending_sms', phoneNumber ) == true ? sendSms(phoneNumber, "$lock1 locked after $contact was closed for $minutesLater minutes!") : log.debug('Invariants Violation!')
    }
}

def unlockDoor() {
    log.debug('Unlocking the door.')
    parent.verify(app.getLabel(), null, lock1.getDisplayName(), 'unlocked', null) == true ? lock1.unlock() : log.debug('Invariants Violation!')
    if ( location .contactBookEnabled) {
        if ( recipients ) {
            log.debug('Sending Push Notification...')
            parent.verify(app.getLabel(), null, null, 'sending_notification', "$lock1 unlocked after $contact was opened for $secondsLater seconds!") == true ? sendNotificationToContacts("$lock1 unlocked after $contact was opened for $secondsLater seconds!", recipients) : log.debug('Invariants Violation!')
        }
    }
    if ( phoneNumber ) {
        log.debug('Sending text message...')
        parent.verify(app.getLabel(), null, null, 'sending_sms', phoneNumber ) == true ? sendSms(phoneNumber, "$lock1 unlocked after $contact was opened for $secondsLater seconds!") : log.debug('Invariants Violation!')
    }
}

def doorHandler(evt) {
    if (contact.latestValue('contact') == 'open' && evt .value == 'locked') {
        runIn(secondsLater, unlockDoor)
    } else {
        if (contact.latestValue('contact') == 'open' && evt .value == 'unlocked') {
            unschedule(unlockDoor)
        } else {
            if (contact.latestValue('contact') == 'closed' && evt .value == 'locked') {
                unschedule(lockDoor)
            } else {
                if (contact.latestValue('contact') == 'closed' && evt .value == 'unlocked') {
                    runIn( minutesLater * 60, lockDoor)
                } else {
                    if (lock1.latestValue('lock') == 'unlocked' && evt .value == 'open') {
                        unschedule(lockDoor)
                    } else {
                        if (lock1.latestValue('lock') == 'unlocked' && evt .value == 'closed') {
                            runIn( minutesLater * 60, lockDoor)
                        } else {
                            log.debug('Unlocking the door.')
                            parent.verify(app.getLabel(), evt , lock1.getDisplayName(), 'unlocked', null) == true ? lock1.unlock() : log.debug('Invariants Violation!')
                            if ( location .contactBookEnabled) {
                                if ( recipients ) {
                                    log.debug('Sending Push Notification...')
                                    parent.verify(app.getLabel(), evt , null, 'sending_notification', "$lock1 unlocked after $contact was opened or closed when $lock1 was locked!") == true ? sendNotificationToContacts("$lock1 unlocked after $contact was opened or closed when $lock1 was locked!", recipients) : log.debug('Invariants Violation!')
                                }
                            }
                            if ( phoneNumber ) {
                                log.debug('Sending text message...')
                                parent.verify(app.getLabel(), evt , null, 'sending_sms', phoneNumber ) == true ? sendSms(phoneNumber, "$lock1 unlocked after $contact was opened or closed when $lock1 was locked!") : log.debug('Invariants Violation!')
                            }
                        }
                    }
                }
            }
        }
    }
}

def getChildAppDevices() {
    return settings 
}

