// File: LogSlf4j.groovy
// Add dependencies for Slf4j API and Logback
/*
@Grapes([
   @Grab(group='org.slf4j', module='slf4j-api', version='1.7.21'),
   @Grab(group='ch.qos.logback', module='logback-classic', version='1.1.7')
])
*/
import groovy.util.logging.Slf4j

@Slf4j
class User {
    def method() {
        log.debug 'Running method'
        log.info 'INFO level'
    }
}

def u = new User()
u.method()
