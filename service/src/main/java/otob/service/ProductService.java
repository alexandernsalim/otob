package otob.service;

import org.springframework.web.multipart.MultipartFile;
import otob.model.entity.Product;
import otob.web.model.PageableProductDto;

import java.util.List;

public interface ProductService {
    Product addProduct(Product product);

    List<Product> addProducts(MultipartFile file);

    PageableProductDto getAllProduct(Integer page, Integer size);

    Product getProductById (Long productId);

    PageableProductDto getAllProductByName(String name, Integer page, Integer size);

    Product updateProductById(Long productId, Product product);

    boolean deleteProductById(Long productId);
}
