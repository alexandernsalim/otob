package future.phase2.offlinetoonlinebazaar.service;

import future.phase2.offlinetoonlinebazaar.model.dto.ProductDto;
import future.phase2.offlinetoonlinebazaar.model.entity.Product;

public interface ProductService {
    ProductDto createProduct(Product product, String image);

    ProductDto viewProduct();

    ProductDto viewProductById(Long productId);

    ProductDto updateProduct(Long productId);

    boolean deleteProduct(Long productId);
}
