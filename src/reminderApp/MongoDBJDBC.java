package reminderApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class MongoDBJDBC {
	private static MongoClient mongoClient;

	private MongoCollection<Document> connMongoDB() {
		// 连接到 mongodb 服务
		mongoClient = new MongoClient("localhost", 27017);

		// 连接到数据库
		MongoDatabase mongoDatabase = mongoClient.getDatabase("reminder");
		System.out.println("Connect to database successfully");

		MongoCollection<Document> collection = mongoDatabase.getCollection("reminderDocument");
		return collection;
	}


	public void addDocument(EventListModel eventList) {
		Document document = new Document("eventType", eventList.getEventType())
				.append("eventTime", eventList.getEventTime()).append("eventContent", eventList.getEventContent());
		List<Document> documents = new ArrayList<Document>();
		documents.add(document);
		MongoCollection<Document> collection=this.connMongoDB();
		collection.insertMany(documents);
		mongoClient.close();
	}
	
	public List<Map<String,String>> queryDocument() {
		MongoCollection<Document> collection=this.connMongoDB();
		FindIterable<Document> findIterable = collection.find();  
        MongoCursor<Document> mongoCursor = findIterable.iterator();  
        List<Map<String,String>> list=new ArrayList<Map<String,String>>();
        while(mongoCursor.hasNext()){
        	Document d=mongoCursor.next();
        	Map <String,String> map=new HashMap<String,String>();
        	map.put("eventType",d.get("eventType").toString()) ;
        	map.put("eventTime",d.get("eventTime").toString()) ;
        	map.put("eventContent",d.get("eventContent").toString()) ;
        	list.add(map);
        }
        mongoClient.close();
        return list;
	}
	public void updateDocument(String field,String oldValue,String newValue) {
		MongoCollection<Document> collection=this.connMongoDB();
		collection.updateMany(Filters.eq(field, oldValue), new Document("$set",new Document(field,newValue)));  
		mongoClient.close();
	}
	public void deleteDocument(String field,String fieldValue) {
		MongoCollection<Document> collection=this.connMongoDB();
		collection.deleteMany(Filters.eq(field, fieldValue));
		mongoClient.close();
	}
}
