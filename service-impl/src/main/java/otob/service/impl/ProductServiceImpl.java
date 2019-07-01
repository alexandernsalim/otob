package otob.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otob.entity.Product;
import otob.enumerator.ErrorCode;
import otob.exception.CustomException;
import otob.generator.IdGenerator;
import otob.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public Product createProduct(Product _product) {
        if(productRepository.existsByName(_product.getName())){
            return productRepository.save(_product);
        }else{
            try{
                _product.setProductId(idGenerator.getNextId("productid"));

                return productRepository.save(_product);
            }catch(Exception e){
                throw new CustomException(
                    ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                    ErrorCode.INTERNAL_SERVER_ERROR.getMessage()
                );
            }
        }
    }

    public List<Product> batchUpload(List<Product> _product) {
        List<Product> productList = new ArrayList<>();

        for (Product product : _product) {
            if(productRepository.existsByName(product.getName())){
                updateProductByName(product);
            }else {
                try {
                    product.setProductId(idGenerator.getNextId("productid"));
                    productRepository.save(product);
                } catch (Exception e) {
                    throw new CustomException(
                        ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                        ErrorCode.INTERNAL_SERVER_ERROR.getMessage()
                    );
                }
            }

            productList.add(product);
        }

        return productList;
    }

    @Override
    public List<Product> getAllProduct() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()){
            throw new CustomException(
                ErrorCode.PRODUCT_NOT_FOUND.getCode(),
                ErrorCode.PRODUCT_NOT_FOUND.getMessage()
            );
        }

        return products;
    }

    @Override
    public Product getProductById(Long productId) {
        Product product = productRepository.findByProductId(productId);

        if (product == null){
            throw new CustomException(
                ErrorCode.PRODUCT_NOT_FOUND.getCode(),
                ErrorCode.PRODUCT_NOT_FOUND.getMessage()
            );
        }

        return product;
    }

    public List<Product> getAllProductByName(String name){
        if(!productRepository.existsByNameContaining(name)){
            throw new CustomException(
                ErrorCode.PRODUCT_NOT_FOUND.getCode(),
                ErrorCode.PRODUCT_NOT_FOUND.getMessage()
            );
        }

        return productRepository.findAllByNameContaining(name);
    }

    public Product updateProductById(Long productId, Product _product) {
        Product product = productRepository.findByProductId(productId);

        if(product == null){
            throw new CustomException(
                ErrorCode.BAD_REQUEST.getCode(),
                ErrorCode.BAD_REQUEST.getMessage()
            );
        }

        product.setName(_product.getName());
        product.setDescription(_product.getDescription());
        product.setListPrice(_product.getListPrice());
        product.setOfferPrice(_product.getOfferPrice());
        product.setStock(_product.getStock());

        return productRepository.save(product);
    }

    public Product updateProductByName(Product _product) {
        Product product = productRepository.findByName(_product.getName());

        if(product == null){
            throw new CustomException(
                ErrorCode.BAD_REQUEST.getCode(),
                ErrorCode.BAD_REQUEST.getMessage()
            );
        }

        product.setDescription(_product.getDescription());
        product.setListPrice(_product.getListPrice());
        product.setOfferPrice(_product.getOfferPrice());
        product.setStock(_product.getStock());

        return productRepository.save(product);
    }

    public boolean deleteProductById(Long productId) {
        Product product = productRepository.findByProductId(productId);

        if (product == null){
            throw new CustomException(
                ErrorCode.BAD_REQUEST.getCode(),
                ErrorCode.BAD_REQUEST.getMessage()
            );
        }

        productRepository.delete(product);
 
        return true;
    }

}
