// File: LogSlf4j.groovy
// Mr Haki's log: http://mrhaki.blogspot.fr/2011/04/groovy-goodness-inject-logging-using.html
// another tut is: https://wiki.base22.com/display/btg/How+to+setup+SLF4J+and+LOGBack+in+a+web+app+-+fast
// plus an intro to logback is: http://logback.qos.ch/manual/introduction.html

// need to run gradle build so that log4j sees the logback.xml file
// Add dependencies for Slf4j API and Logback

// actually added gradle.build dependency so dont need this grab

/*
@Grapes([
    @Grab(group='org.slf4j', module='slf4j-api', version='1.6.1'),
    @Grab(group='ch.qos.logback', module='logback-classic', version='0.9.28'),
    @GrabConfig( systemClassLoader=true )
])


--> results look like:
:runMrHaki
17:17:21.314 [main] DEBUG HelloWorldSlf4j - Execute HelloWorld.
17:17:21.340 [main] INFO  HelloWorldSlf4j - Simple sample to show log field is injected.
17:17:21.356 [main] ERROR HelloWorldSlf4j - a severe msg
17:17:21.364 [main] WARN  HelloWorldSlf4j - a warning msg
log.getName()=HelloWorldSlf4j

*/

import org.slf4j.*
import groovy.util.logging.Slf4j

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

// Use annotation to inject log field into the class.
@Slf4j
class HelloWorldSlf4j {
    def execute() {
        log.debug 'Execute HelloWorld.'
        log.info 'Simple sample to show log field is injected.'
	log.error 'a severe msg'
	log.warn 'a warning msg'
	//log.config 'a config msg will not work, neither will .severe'
	println "log.getName()="+log.getName()
		
	//logger.myLogger.info "an info message from logger.myLogger.info"
  	//logger["com.foo.bar"].info "an info message from logger['com.foo.bar'].info"

	// try other samples
	Logger logger = LoggerFactory.getLogger("MrHakiLogSlf4j.groovy");
    	logger.debug("HelloWorldSlf4j.");
    }
}

def helloWorld = new HelloWorldSlf4j()
helloWorld.execute()