package future.phase2.offlinetoonlinebazaar.service.Impl;

import future.phase2.offlinetoonlinebazaar.exception.ResourceNotFoundException;
import future.phase2.offlinetoonlinebazaar.helper.IdGenerator;
import future.phase2.offlinetoonlinebazaar.model.entity.Product;
import future.phase2.offlinetoonlinebazaar.model.enumerator.ErrorCode;
import future.phase2.offlinetoonlinebazaar.repository.ProductRepository;
import future.phase2.offlinetoonlinebazaar.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        try {
            _product.setProductId(idGenerator.getNextId("productid"));
            productRepository.save(_product);
            return _product;
        }catch(Exception e){
            throw new ResourceNotFoundException(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMessage());
        }
    }


    public List<Product> bacthUpload(List<Product> _product) {
        List<Product> productList = new ArrayList<>();

        for (Product product: _product) {
            if(productRepository.existsByName(product.getName())){
                updateProductByName(product);
            }else {
                try {
                    product.setProductId(idGenerator.getNextId("productid"));
                    productRepository.save(product);
                } catch (Exception e) {
                    throw new ResourceNotFoundException(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMessage());
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
            throw new ResourceNotFoundException(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMessage());
        }
        return products;
    }

    @Override
    public Product getProductById(Long productId) {
        Product product = productRepository.findByProductId(productId);

        if (product == null){
            throw new ResourceNotFoundException(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMessage());
        }
        return product;
    }

    public List<Product> getAllProductByName(String name){
        if(!productRepository.existsByNameContaining(name)){
            throw new ResourceNotFoundException(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMessage());
        }
        return productRepository.findAllByNameContaining(name);
    }

    public Product updateProductById(Long productId, Product _product) {
        Product product = productRepository.findByProductId(productId);

        if(product == null){
            throw new ResourceNotFoundException(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMessage());
        }else{
            product.setName(_product.getName());
            product.setDescription(_product.getDescription());
            product.setListPrice(_product.getListPrice());
            product.setOfferPrice(_product.getOfferPrice());
            product.setStock(_product.getStock());

            productRepository.save(product);
        }

        return product;
    }

    public boolean deleteProductById(Long productId) {
        Product product = productRepository.findByProductId(productId);

        if (product == null){
            throw new ResourceNotFoundException(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMessage());
        }
        productRepository.deleteByProductId(productId);

        return true;
    }

    public Product updateProductByName(Product _product) {
        Product product = productRepository.findByName(_product.getName());

        product.setDescription(_product.getDescription());
        product.setListPrice(_product.getListPrice());
        product.setOfferPrice(_product.getOfferPrice());
        product.setStock(_product.getStock());

        productRepository.save(product);

        return product;
    }

}