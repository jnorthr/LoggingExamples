import CacheEntry;
import java.util.logging.Logger;
import groovy.transform.Canonical
import groovy.transform.ToString
import groovy.transform.TupleConstructor

@Canonical
@groovy.transform.ToString(includeNames = true, includeFields=true)
@TupleConstructor
public abstract class CacheManagerAbstract implements CacheManagerInterface
{
 	static Logger log = Logger.getLogger(CacheManagerAbstract.class.getName());

	//Map of unique key:value pairs
	Map<String,CacheEntry> data=[:];

// -------------------------------------------------------------------------------
// other non-interface methods
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
//  boolean has(String key) 
	boolean has(String key) {return ( data[key] ) ? true : false}

//	boolean has(CacheEntry entry) 
	boolean has(CacheEntry entry) {return ( data[entry.key] ) ? true : false}


// -------------------------------------------------------------------------------
//	boolean del(String key) 
	boolean del(String key) 
	{
		def flag = has(key)
		if (flag)
		{
			log.info "\ndata has $key to remove"
			data.remove(key); 
			// ck if data w/key still exists=failed or not exists=good, so reverse answer & return that 
			def xx = has(key) ? false : true;
			log.info "data del $key removed ?"+xx;
			return xx;
		}
		return flag;
	} // end of method


//	boolean del(CacheEntry entry) 
	boolean del(CacheEntry entry) 
	{
		return del(entry.key)
	} // end of method


// -------------------------------------------------------------------------------
//	CacheEntry get(String key) - returns null if not found
	CacheEntry get(String key) { return ( has(key) ) ? this.data[key] : null}

//	CacheEntry get(CacheEntry entry) 
	CacheEntry get(CacheEntry entry) 
	{
			return get(entry.key);
	} // end of method


// -------------------------------------------------------------------------------
//	CacheEntry put(String key, Map map) - returns null if not added 	
	CacheEntry put(String key, Map map) 
	{ 	
		assert map.key!=null;
		assert map.key.size()>0;
		assert map.key.equals(key);
		def flag = has(key)
		if (!flag )
		{
			log.info "\ndata does not have $key, so add"
			data[key] = new CacheEntry(map);  //key:data.key, name:data.name) ;
			log.info "data for $key added:"+has(key)
			return data[key]
		}
		return null;	
	} // end of put	


//	CacheEntry put(String key, CacheEntry entry)  	
	CacheEntry put(String key, CacheEntry entry) 
	{ 	
		assert entry.key!=null;
		assert entry.key.size()>0;
		assert entry.key.equals(key);
		def flag = has(key)
		if (!flag)
		{
			log.info "\ndata does not have $key, so add"
			data[key]=entry; //new CacheEntry(key:data.key, name:data.name);
			log.info "data for $key added:"+has(key)
			return data[key]
		}
		return null;	
	} // end of put	
	

//	CacheEntry put(CacheEntry entry)  	
	CacheEntry put(CacheEntry entry)  	
	{
		return put(entry.key, entry)
	} // end of put	



// -------------------------------------------------------------------------------
//	CacheEntry fix(String key, Map map) 	
	CacheEntry fix(String key, Map map) 
	{
		assert map.key.equals(key);
		def flag = has(key)
		if (flag)
		{
			log.info "\ndata has $key to update from map"
			map.each{ k, v-> 
				log.info "... k=$k and v=<$v>" 
				data[key][k]=v
			}
			
			// data = new CacheEntry(data); 
			// data.remove(key) <- if row is a map
			//row << data
			
			log.info "data $key updated:"+get(key)
			return data[key]
		} // end of if
		
		return null;
	} // end of fix


//	CacheEntry fix(String key, CacheEntry entry) 	
	CacheEntry fix(String key, CacheEntry entry) 
	{
		assert entry.key.equals(key);
		def flag = has(key)
		if (flag)
		{
			log.info "\ndata has $key to update"
			data[key] = entry;    //new CacheEntry(data); //data.remove(key)
			//data << entry
			log.info "data $key updated:"+get(key)
			return data[key]
		} // end of if
		
		return null;
	} // end of fix


	CacheEntry fix(CacheEntry entry) 
	{
		return fix(entry.key, entry)
	} // end of fix



// -------------------------------------------------------------------------------
	def dump()
	{		
		data.eachWithIndex{ it, i-> //eachIndex() always takes 2 params
  			log.info "[$i] = ${it.key}=" + it.value +'; '
		}

		return data.toString();
	} // end of dump


} // end of class