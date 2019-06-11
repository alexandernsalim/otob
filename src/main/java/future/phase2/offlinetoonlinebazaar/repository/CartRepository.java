package future.phase2.offlinetoonlinebazaar.repository;

import future.phase2.offlinetoonlinebazaar.model.entity.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart, String>, CartRepositoryCustom {
    Cart findByUserEmail(String userEmail);
    boolean existsByUserEmail(String userEmail);
    boolean deleteByUserEmail(String userEmail);
}
