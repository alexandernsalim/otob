package future.phase2.offlinetoonlinebazaar.repository;

import future.phase2.offlinetoonlinebazaar.model.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    Product findByProductId(Long productId);

    List<Product> findAllByNameContaining(String name);

    Product findByName(String name);

    boolean existsByNameContaining(String name);

    boolean existsByName(String name);

    boolean deleteByProductId(Long productId);
}
