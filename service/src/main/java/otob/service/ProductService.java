package otob.service;

import org.springframework.web.multipart.MultipartFile;
import otob.model.entity.Product;

import java.util.List;

public interface ProductService {
    Product addProduct(Product product);

    List<Product> addProducts(MultipartFile file);

    List<Product> getAllProduct();

    Product getProductById (Long productId);

    List<Product> getAllProductByName(String name, int page, int size);

    Product updateProductById(Long productId, Product product);

    boolean deleteProductById(Long productId);
}
