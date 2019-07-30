package otob.service;

import otob.model.entity.Product;

import java.util.List;

public interface ProductService {
    Product addProduct(Product product);

    List<Product> addProductFromExcel(List<Product> product);

    List<Product> getAllProduct();

    Product getProductById (Long productId);

    List<Product> getAllProductByName(String name);

    Product updateProductById(Long productId, Product product);

    boolean deleteProductById(Long productId);
}
