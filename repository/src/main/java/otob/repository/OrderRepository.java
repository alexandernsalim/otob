package otob.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import otob.entity.Order;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {

    Boolean existsByOrderId(String ordId);
    Order findByOrderId(String ordId);
    List<Order> findAllByUserEmail(String userEmail);

}
