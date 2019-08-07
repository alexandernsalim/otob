package otob.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import otob.model.entity.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

    Product findByProductId(Long productId);

    @Query("{'name': {$regex: ?0, $options: 'i'}}")
    Page<Product> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("{'name': {$regex: ?0, $options: 'i'}}")
    Product findByName(String name);
    boolean existsByNameContaining(String name);
    boolean existsByName(String name);

}
