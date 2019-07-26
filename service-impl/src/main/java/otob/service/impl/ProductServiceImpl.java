package otob.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otob.entity.Product;
import otob.enumerator.ErrorCode;
import otob.exception.CustomException;
import otob.generator.IdGenerator;
import otob.repository.ProductRepository;
import otob.service.api.ProductService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IdGenerator idGenerator;

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

    @Override
    public Product addProduct(Product product) {
        if(productRepository.existsByName(product.getName())){
            return productRepository.save(product);
        }else{
            try{
                product.setProductId(idGenerator.getNextId("productid"));

                return productRepository.save(product);
            }catch(Exception e){
                throw new CustomException(
                    ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                    ErrorCode.INTERNAL_SERVER_ERROR.getMessage()
                );
            }
        }
    }

    public List<Product> addProductFromExcel(List<Product> products) {
        List<Product> productList = new ArrayList<>();

        for (Product product : products) {
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

    public Product updateProductById(Long productId, Product productReq) {
        Product product = productRepository.findByProductId(productId);

        if(product == null){
            throw new CustomException(
                ErrorCode.BAD_REQUEST.getCode(),
                ErrorCode.BAD_REQUEST.getMessage()
            );
        }

        product.setName(productReq.getName());
        product.setDescription(productReq.getDescription());
        product.setListPrice(productReq.getListPrice());
        product.setOfferPrice(productReq.getOfferPrice());
        product.setStock(productReq.getStock());

        return productRepository.save(product);
    }

    public Product updateProductByName(Product productReq) {
        Product product = productRepository.findByName(productReq.getName());

        if(product == null){
            throw new CustomException(
                ErrorCode.BAD_REQUEST.getCode(),
                ErrorCode.BAD_REQUEST.getMessage()
            );
        }

        product.setDescription(productReq.getDescription());
        product.setListPrice(productReq.getListPrice());
        product.setOfferPrice(productReq.getOfferPrice());
        product.setStock(productReq.getStock());

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
