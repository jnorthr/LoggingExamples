import groovy.transform.Canonical
import groovy.transform.ToString
import groovy.transform.TupleConstructor
// http://groovy.codehaus.org/gapi/groovy/transform/TupleConstructor.html

@Canonical
@groovy.transform.ToString(includeNames = true, includeFields=true)
@TupleConstructor
public class CacheEntry
{
	String key="";
	String name="";
	
	// both expiry and startTime are in roughly whole seconds
	long expiry = 0;
	long startTime = System.currentTimeMillis() / 1000;	
	def payload = null;
	//Date created = new  Date();
	//String remarks = "";
} // end of class
