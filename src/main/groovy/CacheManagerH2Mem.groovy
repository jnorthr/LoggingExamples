/*
@Grapes([
    @Grab(group='com.h2database', module='h2', version='1.3.160'),
    @GrabConfig(systemClassLoader = true)
])
*/
//import com.h2database.*
import org.h2.Driver
import java.sql.DriverManager
import groovy.sql.*
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Logger;

import CacheEntry;
import groovy.transform.Canonical
import groovy.transform.ToString

@Canonical
@groovy.transform.ToString(includeNames = true, includeFields=true)
public class CacheManagerH2Mem //extends CacheManagerAbstract
{
 	static Logger log = Logger.getLogger(CacheManagerH2Mem.class.getName());

    Connection conn; 
    def db;
    def result;
    boolean audit = true
    def say(tx) { if (audit) println tx;}
	
	
	// ================================================
	// Constructor
	public CacheManagerH2Mem() 
	{
		super();
		say "... loading driver"
        org.h2.Driver.load();
        db = Sql.newInstance('jdbc:h2:mem:cache', 'sa', '', 'org.h2.Driver')
        conn = DriverManager.getConnection("jdbc:h2:~/cache");  //, "sa", "");
        try{
            db.execute("drop table if exists cache")   
            db.execute( '''create table if not exists cache(
        id identity,                    //bigint auto_increment primary key,
        key varchar(60) not null , 
        name varchar(120) not null, 
        expiry bigint,
        startTime bigint,	// this cannot be changed after creation or the expiry feature will not work
        payload clob)
''')  
		def m = "... create cache table as H2mem data store"
		log.info m
		say m
        } // end of try
        
        catch(Exception x)
        {
            say "-> had a problem:"+x.message
            log.config "-> had a problem:"+x.message
            System.exit(1);
        }        
		
		def ca =new CacheEntry("key":"fred","name":"Flintstone") ;
		def a1 = this.put("fred", ca)
	} // end of constructor


//  =============================================
//	CacheEntry put(String key, CacheEntry entry)  	
	CacheEntry put(String key, CacheEntry entry) 
	{ 	
		assert entry.key!=null;
		assert entry.key.size()>0;
		assert entry.key.equals(key);
		say "CacheEntry put(String $key, CacheEntry entry)"
		
		// does this key exist ?
		def flag = has(key)
		say "flag="+flag
		
		// no it does not so insert it
		if (!flag)
		{
			log.info "\ndata does not have $key, so add"
			//data[key]=entry; //new CacheEntry(key:data.key, name:data.name);
			try{
	            //db.executeUpdate("insert into cache values(null, '${entry.key}', '${entry.name}', ${entry.expiry}, ${entry.startTime}, '${entry.key}' ) " )   
	            db.executeUpdate("insert into cache values(?, ?, ?, ?, ?, ?)", [null, entry.key, entry.name, entry.expiry, entry.startTime, entry.payload] )  
	            say "db.executeUpdate(inserted :"+entry.key
	        }
    	    catch(Exception x)
        	{
            	say "-> had a problem:"+x.message
            	return null
        	}        
			log.info "data for $key added:"+has(key)
			return get(key);
		} // end of if
		
		say "---> already has key "+key
		return null;	
	} // end of put	
	
	
//	CacheEntry put(CacheEntry entry)  	
	CacheEntry put(CacheEntry entry)  	
	{
		say "CacheEntry put(CacheEntry entry) key:"+entry.key
		return put(entry.key, entry)
	} // end of put	


// -------------------------------------------------------------------------------
//	CacheEntry put(String key, Map map) - returns null if not added 	
	CacheEntry put(String key, Map map) 
	{ 	
		say "CacheEntry put(String $key, Map map)"
		assert map.key!=null;
		assert map.key.size()>0;
		assert map.key.equals(key);
		def flag = has(key)
		if (!flag )
		{
			log.info "\ndata does not have $key, so add"
			def data = put(new CacheEntry(map)); 
			log.info "data for $key added:"+has(key)
			return data
		}
		return null;	
	} // end of put	


	
// -------------------------------------------------------------------------------
//  boolean has(String key) 
	boolean has(String key) 
	{
			say "has(String $key) ?"
			result=null;
			try{
        		result = db.rows('select * from cache where key = :xxx', [xxx:key] )
        		say "found "+result.size()+" rows"
	        }
    	    catch(Exception x)
        	{
            	say "-> has() had a problem:"+x.message
            	log.info "-> has() had a problem:"+x.message
            	return false
        	}        

			return ( result.key[0].equals(key) ) ? true : false;
	} // end of has

//	boolean has(CacheEntry entry) 
	boolean has(CacheEntry entry) 
	{
		return has(entry.key)
	}



// -------------------------------------------------------------------------------
//	CacheEntry get(String key) - returns null if not found
	CacheEntry get(String key) 
	{ 
		def flag = has(key)		
		say "CacheEntry get(String $key)="+flag
		
		if (!flag)
		{
			return null;
		}
		
		else
		{
			CacheEntry ce = null;
			try{
        		result = db.firstRow('select * from cache where key = :xxx', [xxx:key] )
        		say "found "+result.size()+" rows"
        		say "result was :"+result.getClass()+"\n------------------------\n"
        		result.each{ say "--->"+it; }
        		say "-----------------------------\n"
        		
        		if (result.size() > 0)
        		{
        			ce = new CacheEntry();
        			ce.key = result.key;
        			ce.name = result.name;
        			ce.expiry = result.expiry;
        			ce.startTime = result.startTime;
        			ce.payload = result.payload;
        		} // end of if       		
        		
        		return ce;
	        } // end of try
	        
    	    catch(Exception x)
        	{
            	say "-> get($key) had a problem:"+x.message
            	log.info "-> get($key) had a problem:"+x.message            	
            	return null
        	} // end of catch        
		} // end of else	
			 
	} // end of get
	

//	CacheEntry get(CacheEntry entry) 
	CacheEntry get(CacheEntry entry) 
	{
		say "CacheEntry get(CacheEntry entry)="+entry.key
		return get(entry.key);
	} // end of method


// -------------------------------------------------------------------------------
// this logic will return true if this cache entry is unexpired or no expiry time set
	boolean ok(CacheEntry entry) 
	{
		if (entry.expiry==0) return true; 
		return ( entry.startTime + entry.expiry >  ( System.currentTimeMillis() / 1000) ) 
	} // end of ok
	
	boolean ok(String key)
	{
		if (has(key))
		{
			log.info "data has $key to ok"
			return ok( get(key) ); 
		}
		else
		{
			return false;
		} // end of else
			
	} // end of ok
	
	
// -------------------------------------------------------------------------------
//	boolean del(String key) 
	boolean del(String key) 
	{
		def flag = has(key)
		if (flag)
		{
			log.info "\ndata has $key to remove"
			try{
				result = db.executeUpdate('delete from cache where key = :xxx', [xxx:key]);
        		say "removed "+result+" rows\n------------------------\n"   
	        } // end of try
	        
    	    catch(Exception x)
        	{
            	say "-> del($key) had a problem:"+x.message
            	log.info "-> del($key) had a problem:"+x.message            	
        	} // end of catch   			// ck if data w/key still exists=failed or not exists=good, so reverse answer & return that 

			flag = has(key) ? false : true;
			log.info "data del $key removed ?"+xx;
		} // end of if
		
		return flag;
	} // end of method


//	boolean del(CacheEntry entry) 
	boolean del(CacheEntry entry) 
	{
		return del(entry.key)
	} // end of method

	
// -------------------------------------------------------------------------------

//	CacheEntry fix(String key, Map map) 	
	CacheEntry fix(String key, Map map) 
	{
		say "fix(String $key, Map ${map}) ?"
		assert map.key.equals(key);
		def flag = has(key)
		say "fix(String $key, Map ${map}) ?"+flag
		if (flag)
		{
			log.info "\ndata has $key to update from map"
 		    CacheEntry ce = get(key)
			map.each{ k, v-> 
				log.info "... k=$k and v=<$v>" 
				String lc = k.toString().toLowerCase()
				switch(lc)
				{
					case 'expiry' : ce.expiry = v; break;
					case 'name' : ce.name = v;  break;
					case 'payload' : ce.payload = v;  break;
					case 'starttime' : log.info "... startTime is read-only - cannot change!"; 					
				}
			} // end of each
			
			return fix(key,ce);
		} // end of if
		
		return null;
	} // end of fix


//	CacheEntry fix(String key, CacheEntry entry) 	
	CacheEntry fix(String key, CacheEntry entry) 
	{
		assert entry.key.equals(key);
		say "fix(String $key, CacheEntry ${entry}) ?"
		def flag = has(key)
		if (flag)
		{
			log.info "\ntable has $key to update"
			try{
				def result = db.executeUpdate('update cache set name=:a1, expiry=:a2, payload=:a3 where key = :xxx', [ xxx:key, a1:entry.name, a2:entry.expiry, a3:entry.payload ]);
        		say "updated "+result+" rows\n------------------------\n"   
	        } // end of try
	        
    	    catch(Exception x)
        	{
            	say "-> fix($key) had a problem:"+x.message
            	log.info "-> fix($key) had a problem:"+x.message            	
        	} // end of catch   			
        	
			def ce = get(key)
			log.info "data $key updated:"+ce
			return ce
		} // end of if
		
    	say "-> fix($key) could not find row to update"
        log.info "-> fix($key) could not find row to update"            	
		return null;
	} // end of fix


	CacheEntry fix(CacheEntry entry) 
	{
		return fix(entry.key, entry)
	} // end of fix


// -------------------------------------------------------------------------------
	def dump()
	{		
			try{
        		def rows = db.rows('select * from cache' )
        		say "found "+rows.size()+" rows"
        		rows.each{ row -> println "   "+row; }
	        } // end of try
	        
    	    catch(Exception x)
        	{
            	say "-> dump() had a problem:"+x.message
            	log.info "-> dump) had a problem:"+x.message            	
        	} // end of catch        

	} // end of dump

	
    // -------------------------------------------------------------------------------
	// main method
	public static void main(String[]  args)
	{
		println "\n\n=============================================================\nHello from CacheManagerH2Mem"
		CacheManagerH2Mem cm = new CacheManagerH2Mem()

		println "-----------"
		def flag = cm.has("fred")
		println "flag from CacheManagerH2Mem has(fred):"+flag	

		println "-----------"
		CacheEntry ans = cm.get("fred")
		println "ans from CacheManagerH2Mem get(fred):"+ans	
		println "ok ? "+cm.ok(ans)
		ans.expiry = 1;
		def t4 = cm.fix(ans)
		println "t4=${t4}\n----------------------" 
		Map somemap = ["key":"fred",expiry:66,"name":"freddy martin","startTime":1234567,"payload":"""<html></html>"""]
		def t5 = cm.fix("fred",somemap)
		println "-------\n use map to update row:"+t5	

		println "\n----------\nCacheManagerH2Mem Content";
		println "\nShow all CacheEntry :"
		cm.dump();
		
		println "-----------\n\n"

		ans = cm.get("fredx")
		println "ans from CacheManagerH2Mem get(fredx):"+ans	

		println "\n----------\nCacheManagerH2Mem Content";
		println "\nShow all CacheEntry :"
		cm.dump();

		// ============================
		// add new entry tests ...........
		println "-----------"
		def amap= ["key":"jim","name":"jimbo",expiry:66]
		def added = cm.put("jim", amap)
		println "added from CacheManagerH2Mem put(jim):"+added	
		println "ok ? "+cm.ok(added)

		// did it Add ?
		ans = cm.get("jim")
		println "ans from CacheManagerH2Mem get(jim) has:"+ans+"\n and ok="+cm.ok(ans)	
		println "ok ? "+cm.ok(ans)


		println "\n\n----------------\n--> now add key+CacheEntry for eve"
		added = cm.put("eve", new CacheEntry(key:"eve",payload:"horse feathers") )
		println "put CacheEntry from CacheManagerTest put(eve):"+added	
		println "eve looks like this:"+cm.get("eve");
		println "ok ? "+cm.ok(added)


		println "\n\n----------------\n--> now add CacheEntry sam"
		added = cm.put(new CacheEntry(key:"sam",payload:"play it again sam!") )
		println "put CacheEntry from CacheManagerTest put(sam):"+added	
		println "sam looks like this:"+cm.get("sam");

		println "has(sam)="+cm.has(added);
		println "get(sam)="+cm.get(added);
		
		println "\n---------------------\nF i x   T e s t s"
		def result = cm.fix("sam", new CacheEntry(["key":"sam","payload":"update sam payload",name:"and dave",expiry:3]))
		println "after fix(${result}): get(sam)="+cm.get(result);
		
		println "is cache ok: ok(sam)="+cm.ok(result);
		Thread.sleep(2000);
		println "is cache ok after 2 sec.s: ok(sam)="+cm.ok(result);
		Thread.sleep(4000);
		println "is cache ok after 4 sec.s: ok(sam)="+cm.ok(result);
		
		result.expiry=0;
		cm.fix(result);
		println "is cache ok: ok(sam)="+cm.ok(result);
		
		if (!cm.ok(result))
		{
			println "del(sam)="+cm.del(result);
		} // end of if

		println "has(sam)="+cm.has(result);


		// -------------------------------------
		// delete tests
		println "\n---------------------\nDelete Tests"
		flag = cm.del("fred")
		println "\nflag from CacheManagerH2Mem del(fred):"+flag	

		flag = cm.del(new CacheEntry(key:"eve") );
		println "\nflag from CacheManagerH2Mem del(eve):"+flag	

		flag = cm.del("jim")
		println "flag from CacheManagerH2Mem del(jim):"+flag

		// did it delete ?
		ans = cm.get("jim")
		println "ans from CacheManagerH2Mem get(jim):"+ans	

		flag = cm.del("sam")
		println "flag from CacheManagerH2Mem del(sam):"+flag


		CacheEntry ca = new CacheEntry(key:"mark",name:"Markus Barkus",expiry:14,payload:"""<html><ul><li><a href="cic/1">Wages</a></li><br /><li><a href="cic/2">Salary</a></li><br /><li><a href="cic/12">Interest</a></li><br /><li><a href="cic/13">Dividends</a></li><br /><li><a href="cic/14">Child Allowance</a></li><br /></ul></html>""")
		cm.put(ca);
		println "\n----------\nCacheManagerH2Mem Content";
		println "\nShow all CacheEntry :"
		cm.dump();

		ca.expiry=22;
		cm.fix(ca);
		println "\n----------\nCacheManagerH2Mem Content";
		println "\nShow all CacheEntry :"
		cm.dump();
		
		ca.expiry=44;
		ca.payload="""<html></html>"""
		cm.fix("mark",ca);
		println "\n----------\nCacheManagerH2Mem Content";
		println "\nShow all CacheEntry :"
		cm.dump();
		
		cm.del(ca)
		println "\n----------\nCacheManagerH2Mem Content";
		println "\nShow all CacheEntry :"
		cm.dump();
		
		println "goodbye from CacheManagerH2Mem"	
	}	
}