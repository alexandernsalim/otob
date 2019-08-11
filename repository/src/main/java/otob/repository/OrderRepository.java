package otob.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import otob.model.entity.Order;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {

    Boolean existsByOrderId(String ordId);
    Order findByOrderId(String ordId);
    Page<Order> findAllByUserEmail(String userEmail, Pageable pageable);
    Page<Order> findAllByOrdStatus(String status, Pageable pageable);

}
