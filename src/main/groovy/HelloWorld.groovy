
/* --------------
@Grapes([
    @Grab(group='org.slf4j', module='slf4j-api', version='1.7.2'),
    @GrabConfig( systemClassLoader=true )
])
----------
*/

import org.slf4j.*
import groovy.util.logging.Slf4j
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Use annotation to inject log field into the class.
@Slf4j
public class HelloWorld {
  public static void main(String[] args) 
  {
	println "HelloWorld.main()"
    	Logger logger = LoggerFactory.getLogger(HelloWorld.class);
    	logger.info("this is an .info msg from Hello World");
  } // end of main
} // end of class