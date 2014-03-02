import java.util.logging.Logger;
/**
* A simple Java Hello World program, in the tradition of
* Kernighan and Ritchie.
*/
public class HelloWorld2 {
 private static Logger theLogger =
	 Logger.getLogger(HelloWorld2.class.getName());

 public static void main(String[] args) {
     HelloWorld2 hello = new HelloWorld2("Hello world2! - uses standard java.util.logging.Logger");
     hello.sayHello();
 }

 private String theMessage;

 public HelloWorld2(String message) {
     theMessage = message;
 }

 public void sayHello() {
     // use the 'least important' type of message, one at
     // the 'finest' level.

    // not seen : 
     //theLogger.off("Hello2 off logging!");  
     theLogger.finest("Hello2 finest logging!");  
     theLogger.finer("Hello2 finer logging!");  
     theLogger.fine("Hello2 fine logging!");  
     theLogger.config("Hello2 config logging!");  
     theLogger.info("Hello2 info logging !");  
     theLogger.warning("Hello2 warning logging !");  
     theLogger.severe("Hello2 severe logging !");  
     //theLogger.all("Hello2 all logging!");  

	//What's going on here? The answer lies in the notion of logging levels. 
	// see: http://www.onjava.com/pub/a/onjava/2002/06/19/log.html?page=1
	//
    System.err.println(theMessage);
 }

  public void saySo()
  {
	log.info("Hello2 @Commons info logging!"); 
  }
}