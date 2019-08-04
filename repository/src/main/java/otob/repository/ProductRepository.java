package otob.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import otob.model.entity.Product;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {


    Product findByProductId(Long productId);
    List<Product> findAllByNameContaining(String name);
    Product findByName(String name);
    boolean existsByNameContaining(String name);
    boolean existsByName(String name);

}
