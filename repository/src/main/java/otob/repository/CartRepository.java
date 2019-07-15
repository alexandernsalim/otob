package otob.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import otob.entity.Cart;

public interface CartRepository extends MongoRepository<Cart, String>, CartRepositoryCustom {
    Cart findByUserEmail(String userEmail);
    Boolean existsByUserEmail(String userEmail);
    long deleteByUserEmail(String userEmail);
}
