package otob.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import otob.model.entity.Product;

public interface ProductRepository extends MongoRepository<Product, String> {


    Product findByProductId(Long productId);
    Page<Product> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
    Product findByName(String name);
    boolean existsByNameContaining(String name);
    boolean existsByName(String name);

}
