// see: http://tutorials.jenkov.com/java-logging/levels.html  but java levels r not the same as Log4j
//@Grab('log4j:log4j:1.2.17')
import org.apache.log4j.*
import groovy.util.logging.*  

@Log4j
class LogSample {
    String txt = "batman";
    String robin = "Robin";

    void tell(String kind, String msg)  {
        log."${kind}" "${msg}"
    } // end of method
    
    
    void warn(String name)  {
        log.warn "${name} ${txt}"
    } // end of method
    
    void create(String name)  {

        // ...
        warn("holy shit"); // call a method already set to call the WARN logging level
        log.info "what an info wanker ${txt}"
        tell("error", "this is really a hard error")
        log.fatal "this is a killer! My dick is throbbin' ${robin}! It's fatal !"
        log.debug "Ouch! I have bugs in my pants! - Debug me, Batman!"
        log.trace "There is a TRACE of nuts in my pants ! "
    }
    
    public static void main(String[] args)
    {
        LogSample ir = new LogSample();
        ir.log.level = Level.ALL
        ir.create("jnorthr");
        println "--- the end ---"
    }
}