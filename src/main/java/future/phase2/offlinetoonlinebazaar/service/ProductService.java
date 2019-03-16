package future.phase2.offlinetoonlinebazaar.service;

import future.phase2.offlinetoonlinebazaar.model.entity.Product;

import java.util.List;

public interface ProductService {
    Product createProduct(Product product);

    List<Product> bacthUpload(List<Product> product);

    List<Product> getAllProduct();

    Product getProductById (Long productId);

    List<Product> getAllProductByName(String name);

    Product updateProductById(Long productId, Product product);

    boolean deleteProductById(Long productId);
}
