// File: LogSlf4j.groovy 
// Add dependencies for Slf4j API and Logback 
/*
Inject Logging Using Annotations
With Groovy 1.8 we can inject a log field into our classes with a simple annotation. 
In our class we can invoke method on the log field, just as we would do if we wrote 
the code to inject the log field ourselves. How many times have we written code like 
this Logger log = LoggerFactory.getLogger(<class>) at the top of our classes to use 
for example the Slf4j API? Since Groovy 1.8 we only have to add the@Slf4j annotation 
to our class and get the same result. AND each invocation of a log method is 
encapsulated in a check to see if the log level is enabled.
*/

@Grapes([
    @Grab(group='org.slf4j', module='slf4j-api', version='1.6.1'), 
    @Grab(group='ch.qos.logback', module='logback-classic', version='0.9.28')
])

import org.slf4j.* 
import groovy.util.logging.Slf4j

// Use @Slf4j 
@Slf4j
//@Log4j - log 4 java -  needs diff.grapes
//@Commons - apache commons logging -  needs diff.grapes 
class HelloWorldSlf4j
{
    // annotation to inject log field into the class.
    def execute() 
    {    log.debug 'Execute HelloWorld.' 
         log.info 'Simple sample to show log field is injected.'
    }
} // end of class

def helloWorld = new HelloWorldSlf4j() 
helloWorld.execute()