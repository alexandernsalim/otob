package otob.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import otob.model.entity.Order;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String>, OrderRepositoryCustom {

    Boolean existsByOrdId(String ordId);
    Order findByOrdId(String ordId);
    Page<Order> findAllByUserEmail(String userEmail, Pageable pageable);
    Page<Order> findAllByOrdStatus(String status, Pageable pageable);

}
