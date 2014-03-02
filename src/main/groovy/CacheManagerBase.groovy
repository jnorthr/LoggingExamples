/*
* implement a base class of abstract logic that uses a map as a data store
*/
import CacheEntry;
import groovy.transform.Canonical
import groovy.transform.ToString

@Canonical
@groovy.transform.ToString(includeNames = true, includeFields=true)
public class CacheManagerBase extends CacheManagerAbstract
{
	
	// Constructor
	public CacheManagerBase() 
	{
		super();
		def ca =new CacheEntry("key":"fred","name":"Flintstone") ;
		def a1 = this.put("fred", ca)
	} // end of constructor


	// main method
	public static void main(String[]  args)
	{
		println "\n\nHello from CacheManagerBase"
		CacheManagerBase cmb = new CacheManagerBase()

		def flag = cmb.has("fred")
		println "flag from CacheManagerBase has(fred):"+flag	

		def ans = cmb.get("fred")
		println "ans from CacheManagerBase get(fred):"+ans	

		ans = cmb.get("fredx")
		println "ans from CacheManagerBase get(fredx):"+ans	


		// ============================
		// add new entry tests ...........
		def amap= ["key":"jim","name":"jimbo",expiry:66]
		def added = cmb.put("jim", amap)
		println "added from CacheManagerBase put(jim):"+added	

		// did it Add ?
		ans = cmb.get("jim")
		println "ans from CacheManagerBase get(jim) has:"+ans	


		println "\n\n----------------\n--> now add key+CacheEntry"
		added = cmb.put("eve", new CacheEntry(key:"eve",payload:"horse feathers") )
		println "put CacheEntry from CacheManagerTest put(eve):"+added	
		println "eve looks like this:"+cmb.get("eve");

		println "\n\n----------------\n--> now add CacheEntry sam"
		added = cmb.put(new CacheEntry(key:"sam",payload:"play it again sam!") )
		println "put CacheEntry from CacheManagerTest put(sam):"+added	
		println "sam looks like this:"+cmb.get("sam");

		println "has(sam)="+cmb.has(added);
		println "get(sam)="+cmb.get(added);
		def result = cmb.fix("sam", ["key":"sam","payload":"update sam payload",name:"and dave",expiry:3])
		println "after fix(${result}): get(sam)="+cmb.get(added);
		
		println "is cache ok: ok(sam)="+cmb.ok(added);
		Thread.sleep(6000);
		println "is cache ok after 6 sec.s: ok(sam)="+cmb.ok(added);
		
		if (!cmb.ok(added))
		{
			println "del(sam)="+cmb.del(added);
		} // end of if
		println "has(sam)="+cmb.has(added);
		
		// -------------------------------------
		// delete tests
		flag = cmb.del("fred")
		println "\nflag from CacheManagerBase del(fred):"+flag	

		flag = cmb.del("jim")
		println "flag from CacheManagerBase del(jim):"+flag

		// did it delete ?
		ans = cmb.get("jim")
		println "ans from CacheManagerBase get(jim):"+ans	



		println "\n----------\nCacheManagerBase now holds:"+cmb.data;
		println "\nShow all CacheEntry :"
		cmb.dump();

		println "goodbye from CacheManagerBase"	
	}	
}