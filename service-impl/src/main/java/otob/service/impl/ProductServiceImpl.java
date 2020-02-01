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
import otob.util.mapper.BeanMapper;
import otob.web.model.PageableProductDto;
import otob.web.model.ProductDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public PageableProductDto getAllProduct(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> pages = productRepository.findAll(pageable);
        List<Product> products = pages.getContent();

        return generateResult(pages, products);
    }

    @Override
    public Product getProductById(String productId) {
        Product product = productRepository.findByProductId(productId);

        if (product == null){
            throw new CustomException(
                ErrorCode.PRODUCT_NOT_FOUND.getCode(),
                ErrorCode.PRODUCT_NOT_FOUND.getMessage()
            );
        }

        return product;
    }

    @Override
    public PageableProductDto getAllProductByName(String name, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> pages = productRepository.findAllByNameContaining(name, pageable);
        List<Product> products = pages.getContent();

        return generateResult(pages, products);
    }

    @Override
    public Product addProduct(Product product) {
        if(productRepository.existsByProductId(product.getProductId())){
            return updateProductById(product.getProductId(), product);
        }else{
            return productRepository.save(product);
        }
    }

    public List<Product> addProducts(MultipartFile file) {
        try {
            List<Product> products = new ArrayList<>();

            XSSFWorkbook workBook = new XSSFWorkbook(file.getInputStream());

            for(int i = 0; i < workBook.getNumberOfSheets(); i++){
                XSSFSheet workSheet = workBook.getSheetAt(i);

                for (int j = 1; j < workSheet.getPhysicalNumberOfRows(); j++) {
                    XSSFRow row = workSheet.getRow(j);

                    if(row.getCell(1).getCellType() != 0 || row.getCell(2).getCellType() != 0 ){
                        throw new CustomException(
                                ErrorCode.EXCEL_FORMAT_ERROR.getCode(),
                                ErrorCode.EXCEL_FORMAT_ERROR.getMessage()
                        );
                    }

                    String productId = row.getCell(0).getStringCellValue();
                    String productName = row.getCell(4).getStringCellValue();
                    String productCategory = row.getCell(5).getStringCellValue();
                    String productReturnReason = row.getCell(6).getStringCellValue();
                    String productCondition = row.getCell(7).getStringCellValue();
                    double productListPrice = row.getCell(1).getNumericCellValue();
                    double productOfferPrice = row.getCell(2).getNumericCellValue();
                    String productGrade = row.getCell(8).getStringCellValue();

                    Product product = Product.builder()
                            .productId(productId)
                            .name(productName)
                            .category(productCategory)
                            .returnReason(productReturnReason)
                            .condition(productCondition)
                            .listPrice(productListPrice)
                            .offerPrice(productOfferPrice)
                            .grade(productGrade)
                            .stock(1)
                            .build();

                    products.add(product);
                }
            }

            for(Product product : products) {
                if(productRepository.existsByProductId(product.getProductId())){
                    updateProductById(product.getProductId(), product);
                }else{
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

    public Product updateProductById(String productId, Product productReq) {
        Product product = productRepository.findByProductId(productId);

        if(product == null){
            throw new CustomException(
                ErrorCode.BAD_REQUEST.getCode(),
                ErrorCode.BAD_REQUEST.getMessage()
            );
        }

        product.setName(productReq.getName());
        product.setCategory(productReq.getCategory());
        product.setReturnReason(productReq.getReturnReason());
        product.setCondition(productReq.getCondition());
        product.setListPrice(productReq.getListPrice());
        product.setOfferPrice(productReq.getOfferPrice());
        product.setGrade(productReq.getGrade());
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

        product.setCategory(productReq.getCategory());
        product.setReturnReason(productReq.getReturnReason());
        product.setCondition(productReq.getCondition());
        product.setListPrice(productReq.getListPrice());
        product.setOfferPrice(productReq.getOfferPrice());
        product.setGrade(productReq.getGrade());
        product.setStock(productReq.getStock());

        return productRepository.save(product);
    }

    public boolean deleteProductById(String productId) {
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

    private void checkEmptiness(List<Product> products) throws CustomException{
        if(products.isEmpty()) {
            throw new CustomException(
                ErrorCode.PRODUCT_NOT_FOUND.getCode(),
                ErrorCode.PRODUCT_NOT_FOUND.getMessage()
            );
        }
    }

    private PageableProductDto generateResult(Page<Product> pages, List<Product> products) {
        checkEmptiness(products);

        List<ProductDto> productsResult = BeanMapper.mapAsList(products, ProductDto.class);
        PageableProductDto result = PageableProductDto.builder()
                .totalPage(pages.getTotalPages())
                .products(productsResult)
                .build();

        return result;
    }

}
