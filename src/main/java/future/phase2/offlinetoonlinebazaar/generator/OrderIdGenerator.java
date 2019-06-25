package future.phase2.offlinetoonlinebazaar.generator;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class OrderIdGenerator {

    public String generate(String checkoutDate) throws Exception{
        Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(checkoutDate);
        Timestamp timestamp = new Timestamp(date.getTime());
        String ordId = "ORD" + timestamp.getTime();

        return ordId;
    }

}
