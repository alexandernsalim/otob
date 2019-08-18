package otob.util.generator;

import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otob.model.properties.MongoProperties;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class IdGenerator {

    @Autowired
    private MongoProperties mongoProperties;

    private MongoClient mongoClient;
    private DB db;
    private DBCollection collection;
    private BasicDBObject find;
    private BasicDBObject update;

    private final String ORD_PRE = "ORD";

    public Long getNextId(String name) throws Exception {
        mongoClient = new MongoClient(mongoProperties.getHost(), mongoProperties.getPort());
        db = mongoClient.getDB(mongoProperties.getDatabase());
        collection = db.getCollection("counters");
        find = new BasicDBObject();
        update = new BasicDBObject();

        find.put("_id", name);
        update.put("$inc", new BasicDBObject("seq", 1));

        DBObject object = collection.findAndModify(find, update);
        Long nextId = (Long) object.get("seq");

        return nextId;
    }

    public String generateOrderId(String checkoutDate) throws Exception {
        Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(checkoutDate);
        Timestamp timestamp = new Timestamp(date.getTime());
        String ordId = ORD_PRE + timestamp.getTime();

        return ordId;
    }

}
