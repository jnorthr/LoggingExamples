/*
      @GrabResolver('https://oss.sonatype.org/content/groups/public')
@Grapes([
      @Grab(
        group='org.mongodb', 
        module='mongo-java-driver', 
        version='2.6.5'
      ),
@Grab(group='org.asciidoctor', module='asciidoctor-java-integration', version='0.1.4'),  
 @Grab('org.slf4j:slf4j-simple:1.5.11'),
 @Grab('mysql:mysql-connector-java:5.1.12'),
 @GrabConfig(systemClassLoader = true)
])
*/

import org.slf4j.*
import groovy.sql.*
 
// the following are for mongo client
// import com.mongodb.MongoClient;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;

import groovy.text.TemplateEngine;
import groovy.text.Template;

def logger = LoggerFactory.getLogger('sql')

logger.info 'Initialize SQL'

def username = 'jnorthr'; // args[0]  // First argument passed to script.
def password = 'jnorthr'; //args[1]  // Second argument passed to script is password.
//def sql = Sql.newInstance('jdbc:mysql://localhost/test', username, password, 'com.mysql.jdbc.Driver')

//GET DATABASE
Mongo m = new Mongo();
//DB db = m.getDB( "test" );

// see: http://www.redantelope.com/2010/04/using-mongodb-and-groovy/
//TIME EXECUTION
def benchmark = { closure ->
  start = System.currentTimeMillis()
  closure.call()
  now = System.currentTimeMillis()
  now - start
}
 
//PRINT RESULTS
def printResults = { cur ->
    if (cur) {
        while(cur.hasNext()) {
            println cur.next()
        }
    }
}

int aid=0;
try{
    //GET DATABASE
    Mongo mongo = new Mongo();
    DB caelyf = mongo.getDB("caelyf");
    
    DBCollection documents = caelyf.getCollection("documents");
    documents.drop();
    BasicDBObject doc1 = new BasicDBObject();
    aid += 1;
    doc1.put("author_id", aid);
    doc1.put("author", "jnorthr");
    doc1.put("email", "men@work.com");
    doc1.put("age", 56);
    documents.insert(doc1);

    BasicDBObject query = new BasicDBObject();
    query.put("author", "jnorthr");
    DBCursor cursor = documents.find(query);
    // Print out all "author" documents for a specific author name.
    while (cursor.hasNext()) 
    {
        System.out.println(cursor.next());
    }
    
    // The prior query will find all the objects in the collection that have a particular author. 
    // If we wanted to get all of the items in the collection, we would create the cursor without a query as shown below.
    DBCursor cursor2 = documents.find();
    cursor2.each{r-> println r;}
    
    // Updating documents
    // To update an object, we first have to get the object from the collection then we save it back into the collection.
    BasicDBObject findTestItemQuery = new BasicDBObject();
    findTestItemQuery.put("age", 56);
    DBCursor testItemsCursor = documents.find(findTestItemQuery);
    if(testItemsCursor.hasNext()) {
        DBObject testCodeItem = testItemsCursor.next();
        testCodeItem.put("age", 45);
        documents.save(testCodeItem);
    } // end of if
    
    println "... updated age";
     cursor2 = documents.find();
    cursor2.each{r-> println r;}
    
    
    // Deleting Documents
    // Finally, to delete a document or set of documents, we use the remove method of the collection.
    BasicDBObject deleteQuery = new BasicDBObject();
    deleteQuery.put("author", "jnorthr");
    DBCursor cursor3 = documents.find(deleteQuery);
    int ct = 0;
    while (cursor3.hasNext()) {
        DBObject row = cursor3.next();
        documents.remove(row);
        ct+=1;
    }  // end of while
    println "--- removed $ct rows"
} // end of try
catch(Exception x) 
{
    println "failed to insert doctor:"+x;
}
logger.info "Post Caelyf MongoDB connection"   //: $sql"

DB db = m.getDB( "testdb" );
DBCollection custs = db.getCollection("customers");
db.each{d-> println "got db="+d}
custs.each{c -> println "got customers="+c;}

def customers = custs.find() 
customers.each{e -> println "... customer="+e;}

custs.drop();

def tranCnt = 100
def duration = benchmark {
    for (id in 1..tranCnt) {
        def random = new Random()
        def rand = random.nextInt(50) + 25
        def addresses = []
        for (itemId in 0..2) {
            def address = ["address1":itemId+id+" Test Street",
                    "city":"Wilmington",
                    "state":"DE",
                    "zip":"19042"]
            addresses.add address
        }
        def cust = ["party_id":id,
                "firstName":"Alex #"+id,
                "lastName":"Tester",
                "addresses": addresses,
                "phones":["home":"56225587"+rand,
                "work":"30225587"+rand],
                "insert_ts": new Date()] as BasicDBObject
        custs.insert(cust)
    }
}
//CREATE INDEX
custs.createIndex(new BasicDBObject("party_id", 1));
custs.createIndex(new BasicDBObject("addresses.address1", 1));
println "Created $tranCnt transactions in $duration ms ("+duration/(tranCnt/100)+" ms per 100 insert)"

BasicDBObject query = new BasicDBObject();
query.put("party_id", 71);
duration = benchmark {
    cur = custs.find(query)
}
printResults(cur)

try{

db.createCollection("doctors", { capped : true} )
use "doctors"
DBCollection doctor = db.getCollection("doctors");

// Inserting a Doctor with two fields
db.doctor.insert([first:"Charles",last:"Darwin"])
}catch(Exception x) {println "failed to insert doctor:"+x;}
logger.info "Got myself a MongoDB connection"   //: $sql"

Set<String> colls = db.getCollectionNames();

for (String s : colls) {
    System.out.println(s);
}
    def payload2 = '''= My Notes About AsciiDoctor
:icons: font 
jnorthr <doc.writer@asciidoctor.org>
v1.0, 2014-01-01 

== Heading One
*This* is it. My name is {name}. Hi {author}. here is an include statement:<% include '/WEB-INF/includes/header.gtpl' %> 

TIP: Eat Spinach regularly. 
icon:heart[2x] 
'''

println "--- the end ---"