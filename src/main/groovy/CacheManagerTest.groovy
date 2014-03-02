/*
* A test wrapper around the base class
*/
import CacheEntry;
import groovy.transform.Canonical
import groovy.transform.ToString
import java.util.logging.Logger;

@Canonical
@groovy.transform.ToString(includeNames = true, includeFields=true)
public class CacheManagerTest //extends CacheManagerAbstract 
{
 	static Logger log = Logger.getLogger(CacheManagerTest.class.getName());

	//CacheManagerBase cmt;
	CacheManagerInterface cmt;
	
	// CacheEntry row - this syntax works even with CacheEntry as an object. 
	// Ok, it's missing some values on purposes
	public CacheManagerTest() 
	{
		super();
		cmt = new CacheManagerBase()
		def ca = new CacheEntry("key":"fred","name":"Flintstone") ;
		def a1 = cmt.put("fred", ca)
	} // end of constructor
	
	
	// run several tests
	public void process()
	{
		def flag = cmt.has("fred")
		println "flag from CacheManagerTest has(fred):"+flag	

		def ans = cmt.get("fred")
		println "ans from CacheManagerTest get(fred):"+ans	

		ans = cmt.get("fredx")
		println "ans from CacheManagerTest get(fredx):"+ans	


		// ============================
		// add new entry tests ...........
		def amap= ["key":"jim","name":"jimbo",expiry:66]
		def added = cmt.put("jim", amap)
		println "added from CacheManagerTest put(jim):"+added	

		// did it Add ?
		ans = cmt.get("jim")
		println "ans from CacheManagerTest get(jim) has:"+ans	


		println "\n\n----------------\n--> now add key+CacheEntry"
		added = cmt.put("eve", new CacheEntry(key:"eve",payload:"horse feathers") )
		println "put CacheEntry from CacheManagerTest put(eve):"+added	
		println "eve looks like this:"+cmt.get("eve");

		println "\n\n----------------\n--> now add CacheEntry sam"
		added = cmt.put(new CacheEntry(key:"sam",payload:"play it again sam!") )
		println "put CacheEntry from CacheManagerTest put(sam):"+added	
		println "sam looks like this:"+cmt.get("sam");

		println "has(sam)="+cmt.has(added);
		println "del(sam)="+cmt.del(added);


		// ==========================
		// update tests
		def updata= ["key":"jim","name":"jnorthr","expiry":26]
		def result = cmt.fix("jim", updata)
		println "result from CacheManagerTest fix(jim) using a map:"+result+" and now contains:"+cmt.get("jim")

		result = cmt.fix("jim", ["key":"jim","payload":"update jim payload",expiry:3])
		println "result from CacheManagerTest cmt.fix(jim)'s payload using a map:"+result+"\n and now contains:"+cmt.get("jim")

		println "is cache ok: ok(jim)="+cmt.ok(result);
		Thread.sleep(6000);
		println "is cache ok after 6 sec.s: ok(jim)="+cmt.ok(result);
		

		println "\n\n----------------\nShow all CacheEntry :"
		cmt.dump();


		// -------------------------------------
		// delete tests
		flag = cmt.del("fred")
		println "\nflag from CacheManagerTest del(fred):"+flag	

		flag = cmt.del("jim")
		println "flag from CacheManagerTest del(jim):"+flag

		// did it delete ?
		ans = cmt.get("jim")
		println "ans from CacheManagerTest get(jim):"+ans	



		println "\nCacheManagerTest now holds:"+cmt.data;


		println "\n---------\nAnother put but this time as a CacheEntry"
		flag = cmt.del("max")
		println "\nflag from CacheManagerTest del(max):"+flag	

		CacheEntry ce = new CacheEntry(["key":"max","name":"MaxWell",expiry:21])
		println "--- so now ce:"+ce;

		added = cmt.put("max", ce)
		println "added from CacheManagerTest put(max):"+added	
		// did it add ?
		ans = cmt.get("max")
		println "did max add ? ans from CacheManagerTest get(max):"+ans	

		ce = ["key":"max","payload":"MaxWell's silver hammer came down on her head. Bang, bang - Maxwell's hammer made sure she was dead."]
		added = cmt.fix("max", ce)
		println "\nadded from CacheManagerTest fix(max):"+added	
		// did it update ?
		ans = cmt.get("max")
		println "did max update ? ans from CacheManagerTest get(max):"+ans	

		println "\n----------\nCacheManagerTest now holds:"+cmt.data;
		println "\nShow all CacheEntry :"
		cmt.dump();
	
	} // end of process


	public static void main(String[]  args)
	{
		println "\n\nHello from CacheManagerTest"
		CacheManagerTest cm = new CacheManagerTest()
		cm.process();
		println "goodbye from CacheManagerTest"	
	}	
}