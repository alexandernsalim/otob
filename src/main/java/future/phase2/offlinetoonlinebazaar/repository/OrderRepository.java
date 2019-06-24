package future.phase2.offlinetoonlinebazaar.repository;

import future.phase2.offlinetoonlinebazaar.model.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {

    Boolean existsByOrdId(String ordId);
    List<Order> findAllByUserEmail(String userEmail);
    Order findByOrdId(String ordId);

}
