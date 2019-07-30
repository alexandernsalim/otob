package otob.web.controller;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import otob.model.constant.path.ProductApiPath;
import otob.web.model.ProductDto;
import otob.model.entity.Product;
import otob.util.mapper.BeanMapper;
import otob.model.response.Response;
import otob.service.ProductService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(ProductApiPath.BASE_PATH)
public class ProductController extends GlobalController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BeanMapper mapper;

    @GetMapping
    public Response<List<ProductDto>> getAllProduct() {
        List<Product> products = productService.getAllProduct();

        return toResponse(mapper.mapAsList(products, ProductDto.class));
    }

    @GetMapping(ProductApiPath.GET_PRODUCT_BY_NAME)
    public Response<List<ProductDto>> getAllProductByName(@PathVariable String productName) {
        List<Product> products = productService.getAllProductByName(productName);

        return toResponse(mapper.mapAsList(products, ProductDto.class));
    }

    @GetMapping(ProductApiPath.GET_PRODUCT_BY_ID)
    public Response<ProductDto> getProductById(@PathVariable Long productId) {
        return toResponse(mapper.map(productService.getProductById(productId), ProductDto.class));
    }

    @PostMapping
    public Response<ProductDto> addProduct(@Valid @RequestBody ProductDto productDto) {
        Product product = mapper.map(productDto, Product.class);

        return toResponse(mapper.map(productService.addProduct(product), ProductDto.class));
    }

    @PostMapping(ProductApiPath.ADD_PRODUCT_FROM_EXCEL)
    public Response<List<ProductDto>> addProductFromExcel(@RequestParam("file") MultipartFile dataFile) throws IOException {
        List<Product> productList = new ArrayList<>();

        XSSFWorkbook workbook = new XSSFWorkbook(dataFile.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for (int i = 2; i < worksheet.getPhysicalNumberOfRows(); i++) {
            XSSFRow row = worksheet.getRow(i);

            Product product = new Product();

            product.setName(row.getCell(1).getStringCellValue());
            product.setDescription(row.getCell(2).getStringCellValue());
            product.setListPrice(row.getCell(3).getNumericCellValue());
            product.setOfferPrice(row.getCell(4).getNumericCellValue());
            product.setStock((int) row.getCell(5).getNumericCellValue());

            productList.add(product);
        }

        List<Product> productListResult = productService.addProductFromExcel(productList);

        return toResponse(mapper.mapAsList(productListResult, ProductDto.class));
    }

    @PutMapping(ProductApiPath.PRODUCTID_PLACEHOLDER)
    public Response<ProductDto> updateById(@PathVariable Long productId, @Valid @RequestBody ProductDto productDto) {
        Product product = mapper.map(productDto, Product.class);

        return toResponse(mapper.map(productService.updateProductById(productId, product), ProductDto.class));
    }

    @DeleteMapping(ProductApiPath.PRODUCTID_PLACEHOLDER)
    public Response<Boolean> deleteById(@PathVariable Long productId) {

        return toResponse(productService.deleteProductById(productId));
    }

}