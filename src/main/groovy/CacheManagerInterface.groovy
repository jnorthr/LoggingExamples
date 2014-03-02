import CacheEntry;
public interface CacheManagerInterface
{
	boolean has(String key) 
	boolean has(CacheEntry entry) 
	
	boolean del(CacheEntry entry) 
	boolean del(String key) 
	
	CacheEntry get(String key) 
	CacheEntry get(CacheEntry entry) 
	
	CacheEntry put(String key, Map map)  	
	CacheEntry put(String key, CacheEntry entry)  	
	CacheEntry put(CacheEntry entry)  	

	CacheEntry fix(String key, Map map) 	
	CacheEntry fix(String key, CacheEntry entry) 	
	CacheEntry fix(CacheEntry entry) 	
}