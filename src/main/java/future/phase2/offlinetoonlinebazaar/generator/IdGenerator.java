package future.phase2.offlinetoonlinebazaar.generator;

import com.mongodb.*;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class IdGenerator {
    private final String ORD_PRE = "ORD";

    public Long getNextId(String name) throws Exception {
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

    public String generateOrderId(String checkoutDate) throws Exception {
        Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(checkoutDate);
        Timestamp timestamp = new Timestamp(date.getTime());
        String ordId = ORD_PRE + timestamp.getTime();

        return ordId;
    }

}
