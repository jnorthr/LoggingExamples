// demoGroovyLogTransformation.groovy

// see: http://marxsoftware.blogspot.fr/2011/05/easy-groovy-logger-injection-and-log.html
//
// Grab SLF4J, Log4j, and Apache Commons Logging dependencies using @Grab
//

// No need to "grab" java.util.logging: it's part of the JDK!

/*
 * Specifying 'slf4j-simple' rather than 'slf4j-api' to avoid the error
 * "Failed to load class "org.slf4j.impl.StaticLoggerBinder" that is caused by
 * specifying no or more than one of the actual logging binding libraries to
 * be used (see http://www.slf4j.org/codes.html#StaticLoggerBinder). One should
 * be selected from 'slf4j-nop', 'slf4j-simple', 'slf4j-log4j12.jar',
 * 'slf4j-jdk14', or 'logback-classic'. An example of specifying the SLF4J
 * dependency via @Grab is available at
 * http://mvnrepository.com/artifact/org.slf4j/slf4j-api/1.6.1.
 */
@Grab(group='org.slf4j', module='slf4j-simple', version='1.6.1')

/*
 * An example of specifying the Log4j dependency via @Grab is at
 * http://mvnrepository.com/artifact/log4j/log4j/1.2.16.
 */
@Grab(group='log4j', module='log4j', version='1.2.16')

/*
 * An example of specifying the Apache Commons Logging dependency via @Grab is at
 * http://mvnrepository.com/artifact/commons-logging/commons-logging-api/1.1.
 */
@Grab(group='commons-logging', module='commons-logging-api', version='1.1')


import groovy.util.logging.Slf4j
import groovy.util.logging.Log
import org.apache.log4j.Level
import groovy.util.logging.Commons
import groovy.util.logging.Log4j

//
// Run the tests...
//
/*
The above four Groovy classes are very similar, but each one takes advantage of a different Groovy 1.8 logger injection. 
The next Groovy code listing shows a script that makes use of these classes and demonstrates the workings of the 
respective injected loggers. 
*/
def javaUtilLogger = new JavaUtilLoggerClass()
def log4jLogger = new Log4jLoggerClass()
def slf4jLogger = new Slf4jLoggerClass()
def commonsLogger = new ApacheCommonsLoggerClass()

// --------------------------------
// dependent classes follow
@Log
class JavaUtilLoggerClass
{
   public JavaUtilLoggerClass()
   {
      println "\njava.util.logging (${log.name}: ${log.class}):"
      log.info "${this.printAndReturnValue(1)}"
      log.finer "${this.printAndReturnValue(2)}"
   }

   public String printAndReturnValue(int newValue)
   {
      println "JDK: Print method invoked for ${newValue}"
      return "JDK: ${newValue}"
   }
}

//-----------------------------

@Slf4j
class Slf4jLoggerClass
{
   public Slf4jLoggerClass()
   {
      println "\nSLF4J Logging (${log.name}: ${log.class}):"
      log.info "${this.printAndReturnValue(1)}"
      log.debug "${this.printAndReturnValue(2)}"
   }

   public String printAndReturnValue(int newValue)
   {
      println "SLF4J: Print method invoked for ${newValue}"
      return "SLF4J: ${newValue}"
   }
}

//----------------------------------

@Log4j
class Log4jLoggerClass
{
   Log4jLoggerClass()
   {
      // It is necessary to set logging level here because default is FATAL and
      // we are not using a Log4j external configuration file in this example
      log.setLevel(Level.INFO)
      println "\nLog4j Logging (${log.name}: ${log.class}):"
      log.info "${this.printAndReturnValue(1)}"
      log.debug "${this.printAndReturnValue(2)}"
   }

   public String printAndReturnValue(int newValue)
   {
      println "Log4j: Print method invoked for ${newValue}"
      return "Log4j: ${newValue}"
   }
}

//--------------------------------------

@Commons
class ApacheCommonsLoggerClass
{
   public ApacheCommonsLoggerClass()
   {
      println "\nApache Commons Logging (${log.name}: ${log.class}):"
      log.info "${this.printAndReturnValue(1)}"
      log.debug "${this.printAndReturnValue(2)}"
   }

   public String printAndReturnValue(int newValue)
   {
      println "Commons: Print method invoked for ${newValue}"
      return "Commons: ${newValue}"
   }
}