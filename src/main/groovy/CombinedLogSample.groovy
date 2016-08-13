// taken from MrHaki at http://mrhaki.blogspot.fr/2011/04/groovy-goodness-inject-logging-using.html
// File: CombinedLogSample.groovy
// Add dependencies for Slf4j API (and using Logback - failed as it needs config file:File: src/main/resources/logback.groovy)
// logback is config'd by a groovy script, see: http://mrhaki.blogspot.fr/2010/09/grassroots-groovy-configure-logback.html or
// http://vazexqi.github.io/posts/2013/02/24/groovy-slf4j.html to logback.xml to an output file, so mkdir config @ projectDir like /src
// then put logback.xml into it with 
/*
<configuration>                                                                                                                                                                                                                               
                                                                                                                                                                                                                                              
  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">                                                                                                                                                                        
    <encoder>                                                                                                                                                                                                                                 
      <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>                                                                                                                                                             
    </encoder>                                                                                                                                                                                                                                
  </appender>                                                                                                                                                                                                                                 
                                                                                                                                                                                                                                              
                                                                                                                                                                                                                                              
  <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">                                                                                                                                                              
    <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">                                                                                                                                                                
      <!-- Daily rollover -->                                                                                                                                                                                                                 
      <fileNamePattern>log/MyExample.%d{yyyy-MM-dd}.log</fileNamePattern>                                                                                                                                                                     
                                                                                                                                                                                                                                              
      <!-- Keep 7 days' worth of history -->                                                                                                                                                                                                  
      <maxHistory>7</maxHistory>                                                                                                                                                                                                              
    </rollingPolicy>                                                                                                                                                                                                                          
                                                                                                                                                                                                                                              
    <encoder>                                                                                                                                                                                                                                 
      <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>                                                                                                                                                             
    </encoder>                                                                                                                                                                                                                                
  </appender>                                                                                                                                                                                                                                 
                                                                                                                                                                                                                                              
  <!-- Configure so that it outputs to both console and log file -->                                                                                                                                                                          
  <root level="DEBUG">                                                                                                                                                                                                                        
    <appender-ref ref="FILE" />                                                                                                                                                                                                               
    <appender-ref ref="STDOUT" />                                                                                                                                                                                                             
  </root>                                                                                                                                                                                                                                     
</configuration> 

-------------------------------

Besides an annotation for the Slf4j API other logging frameworks are supported with annotations:
Logging framework        Annotation
java.util.logging        @Log - only .config .fine .finer .info methods
Log4j                    @Log4j - only works if .config file present otherwise no-op
Apache Commons Logging   @Commons
Slf4j API                @Slf4j - needs a backing log tool, see: http://logback.qos.ch/manual/groovy.html
@Grapes([
    @Grab(group='org.slf4j', module='slf4j-api', version='1.6.1'),
    @Grab(group='ch.qos.logback', module='logback-classic', version='0.9.28') // failed on @Slf4j
    //@Grab('log4j:log4j:1.2.17'),
    //@GrabConfig( systemClassLoader=true )
])
*/
import org.slf4j.*
import groovy.util.logging.Slf4j
//import groovy.util.logging.Log
//import groovy.util.logging.Commons  // only for @Commons logging
//import groovy.util.logging.Log4j
//import org.apache.log4j.*
 
// Use annotation to inject log field into the class.
@Slf4j
class CombinedLog {
    def execute() {
        log.debug 'Execute HelloWorld.' // No such debug in @Log
        //log.fine  'Execute HelloWorld.' // Only in @Log as .config .fine .finer .info but only if settings allow > .info
        log.info 'Simple sample to show log field is injected.'
    }
}
 
def helloWorld = new CombinedLog()
println "--- Hello from CombinedLogSample ---"
helloWorld.execute()
println "--- the end ---"