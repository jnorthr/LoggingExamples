/*
* A test wrapper around the base class
*/
import CacheEntry;
import groovy.transform.Canonical
import groovy.transform.ToString
import java.util.logging.Logger;
import spock.lang.*

@Canonical
@groovy.transform.ToString(includeNames = true, includeFields=true)
public class CacheManagerTestH2 //extends CacheManagerAbstract 
{
 	static Logger log = Logger.getLogger(CacheManagerTestH2.class.getName());

	//CacheManagerBase cmt;
	//CacheManagerInterface cmt;
	CacheManagerH2Mem cmt;

	// CacheEntry row - this syntax works even with CacheEntry as an object. 
	// Ok, it's missing some values on purposes
	public CacheManagerTestH2() 
	{
		super();
		cmt = new CacheManagerH2Mem()
		def ta = """<html><h1>Mary woz ere</h1></html>"""
		def ca = new CacheEntry(key:"Mary",name:"Flintstone.",expiry:123,startTime:345,payload:ta) ;
		def a1 = cmt.put("Mary", ca)
		println "a1 from CacheManagerTestH2 constructor get(Mary):"+a1	
		println "===================================\n"
	} // end of constructor
	
	
	// run several tests
	public void process()
	{
		println "\n==================================="

		def flag = cmt.has("fred")
		println "flag from CacheManagerTestH2 has(fred):"+flag	

		println "==================================="
		def ans = cmt.get("fred")
		println "ans from CacheManagerTestH2 get(fred):"+ans	

		println "==================================="
		ans = cmt.get("fredx")
		println "ans from CacheManagerTestH2 get(fredx):"+ans	

		println "==================================="

		// ============================
		// add new entry tests ...........
		def amap= ["key":"jim","name":"jimbo",expiry:66,payload:"<html></html>"]
		def added = cmt.put("jim", amap)
		println "added from CacheManagerTestH2 put(jim):"+added	

		// did it Add ?
		ans = cmt.get("jim")
		println "ans from CacheManagerTestH2 get(jim) has:"+ans	


		println "\n\n----------------\n--> now add key+CacheEntry"
		added = cmt.put("eve", new CacheEntry(key:"eve",payload:"horse feathers") )
		println "put CacheEntry from CacheManagerTestH2 put(eve):"+added	
		println "eve looks like this:"+cmt.get("eve");

		println "\n\n----------------\n--> now add CacheEntry sam"
		added = cmt.put(new CacheEntry(key:"sam",payload:"play it again sam!") )
		println "put CacheEntry from CacheManagerTestH2 put(sam):"+added	
		println "sam looks like this:"+cmt.get("sam");

		println "has(sam)="+cmt.has(added);
		println "del(sam)="+cmt.del(added);


		// ==========================
		// update tests
		def updata= ["key":"jim","name":"jnorthr","expiry":26,payload:"<html><h1>H Kids</h1></html>"]
		def result = cmt.fix("jim", updata)
		println "result from CacheManagerTestH2 fix(jim) using a map:"+result+" and now contains:"+cmt.get("jim")

		result = cmt.fix("jim", ["key":"jim","payload":"update jim payload",expiry:3])
		println "result from CacheManagerTestH2 cmt.fix(jim)'s payload using a map:"+result+"\n and now contains:"+cmt.get("jim")

		println "is cache ok: ok(jim)="+cmt.ok(result);
		Thread.sleep(6000);
		println "is cache ok after 6 sec.s: ok(jim)="+cmt.ok(result);
		

		println "\n\n----------------\nShow all CacheEntry :"
		cmt.dump();


		// -------------------------------------
		// delete tests
		flag = cmt.del("fred")
		println "\nflag from CacheManagerTestH2 del(fred):"+flag	

		flag = cmt.del("jim")
		println "flag from CacheManagerTestH2 del(jim):"+flag

		// did it delete ?
		ans = cmt.get("jim")
		println "ans from CacheManagerTestH2 get(jim):"+ans	



		println "\nCacheManagerTestH2 now holds:"+cmt.data;


		println "\n---------\nAnother put but this time as a CacheEntry"
		flag = cmt.del("max")
		println "\nflag from CacheManagerTestH2 del(max):"+flag	

		CacheEntry ce = new CacheEntry(["key":"max","name":"MaxWell",expiry:21,payload:"<html>max</html>"])
		println "--- so now ce:"+ce;

		added = cmt.put("max", ce)
		println "added from CacheManagerTestH2 put(max):"+added	
		// did it add ?
		ans = cmt.get("max")
		println "did max add ? ans from CacheManagerTestH2 get(max):"+ans	

		ce = ["key":"max","payload":"MaxWell's silver hammer came down on her head. Bang, bang - Maxwell's hammer made sure she was dead."]
		added = cmt.fix("max", ce)
		println "\nadded from CacheManagerTestH2 fix(max):"+added	
		// did it update ?
		ans = cmt.get("max")
		println "did max update ? ans from CacheManagerTestH2 get(max):"+ans	

		println "\n----------\nCacheManagerTestH2 now holds:"+cmt.data;
		println "\nShow all CacheEntry :"
		cmt.dump();
	
	} // end of process


	public static void main(String[]  args)
	{
		println "\n\n\n---------------------------------------------\nHello from CacheManagerTestH2"
		CacheManagerTestH2 cm = new CacheManagerTestH2()
		cm.process();
		println "goodbye from CacheManagerTestH2"	
	}	
}