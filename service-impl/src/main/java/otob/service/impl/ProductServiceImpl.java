package otob.service.impl;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import otob.model.entity.Product;
import otob.model.enumerator.ErrorCode;
import otob.model.exception.CustomException;
import otob.repository.ProductRepository;
import otob.service.ProductService;
import otob.util.generator.IdGenerator;

import java.io.IOException;
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

    public List<Product> getAllProductByName(String name, int page, int size){
        if(!productRepository.existsByNameContaining(name)){
            throw new CustomException(
                ErrorCode.PRODUCT_NOT_FOUND.getCode(),
                ErrorCode.PRODUCT_NOT_FOUND.getMessage()
            );
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> pages = productRepository.findAllByNameContainingIgnoreCase(name, pageable);
        List<Product> products = pages.getContent();

        return products;
    }

    @Override
    public Product addProduct(Product product) {
        if(productRepository.existsByName(product.getName())){
            return updateProductByName(product);
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

    public List<Product> addProducts(MultipartFile file) {
        try {
            List<Product> products = new ArrayList<>();

            XSSFWorkbook workBook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet workSheet = workBook.getSheetAt(0);

            for (int i = 1; i < workSheet.getPhysicalNumberOfRows(); i++) {
                XSSFRow row = workSheet.getRow(i);

                if(row.getCell(2).getCellType() != 0 ||
                   row.getCell(3).getCellType() != 0 ||
                   row.getCell(4).getCellType() != 0 ){
                    throw new CustomException(
                        ErrorCode.EXCEL_FORMAT_ERROR.getCode(),
                        ErrorCode.EXCEL_FORMAT_ERROR.getMessage()
                    );
                }

                String productName = row.getCell(0).getStringCellValue();
                String productDescription = row.getCell(1).getStringCellValue();
                double productListPrice = row.getCell(2).getNumericCellValue();
                double productOfferPrice = row.getCell(3).getNumericCellValue();
                int productStock = (int) row.getCell(4).getNumericCellValue();

                Product product = Product.builder()
                    .name(productName)
                    .description(productDescription)
                    .listPrice(productListPrice)
                    .offerPrice(productOfferPrice)
                    .stock(productStock)
                    .build();

                products.add(product);
            }

            for(Product product : products) {
                if(productRepository.existsByName(product.getName())){
                    updateProductByName(product);
                }else{
                    product.setProductId(idGenerator.getNextId("productid"));
                    productRepository.save(product);
                }
            }

            return products;
        } catch (IOException ex) {
            throw new CustomException(
                ErrorCode.BAD_REQUEST.getCode(),
                ErrorCode.BAD_REQUEST.getMessage()
            );
        } catch (CustomException ex) {
            throw new CustomException(
                ErrorCode.EXCEL_FORMAT_ERROR.getCode(),
                ErrorCode.EXCEL_FORMAT_ERROR.getMessage()
            );
        } catch (Exception ex) {
            throw new CustomException(
                ErrorCode.GENERATE_ID_FAIL.getCode(),
                ErrorCode.GENERATE_ID_FAIL.getMessage()
            );
        }
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
