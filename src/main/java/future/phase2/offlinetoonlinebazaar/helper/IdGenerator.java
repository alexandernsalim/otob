package future.phase2.offlinetoonlinebazaar.helper;

import com.mongodb.*;
import org.springframework.stereotype.Service;

@Service
public class IdGenerator {

    public Long getNextId(String name) throws Exception{
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB db = mongoClient.getDB("offline-to-online");
        DBCollection collection = db.getCollection("counters");
        BasicDBObject find = new BasicDBObject();
        BasicDBObject update = new BasicDBObject();

        find.put("_id", name);
        update.put("$inc", new BasicDBObject("seq", 1));

        DBObject object = collection.findAndModify(find, update);
        Long nextId = (Long) object.get("seq");

        return nextId;
    }

}
