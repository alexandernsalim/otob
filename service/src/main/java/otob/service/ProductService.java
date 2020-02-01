package otob.service;

import org.springframework.web.multipart.MultipartFile;
import otob.model.entity.Product;
import otob.web.model.PageableProductDto;

import java.util.List;

public interface ProductService {

    PageableProductDto getAllProduct(Integer page, Integer size);
    Product getProductById (String productId);
    PageableProductDto getAllProductByName(String name, Integer page, Integer size);

    Product addProduct(Product product);
    List<Product> addProducts(MultipartFile file);
    Product updateProductById(String productId, Product product);
    boolean deleteProductById(String productId);

}
