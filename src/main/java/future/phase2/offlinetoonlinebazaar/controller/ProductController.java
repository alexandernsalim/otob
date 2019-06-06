package future.phase2.offlinetoonlinebazaar.controller;

import future.phase2.offlinetoonlinebazaar.model.dto.ProductDto;
import future.phase2.offlinetoonlinebazaar.model.entity.Product;
import future.phase2.offlinetoonlinebazaar.model.response.Response;
import future.phase2.offlinetoonlinebazaar.service.ProductService;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product")
public class ProductController extends GlobalController {
    @Autowired
    private ProductService productService;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    @PostMapping("/create")
    public Response<ProductDto> create(@Valid @RequestBody ProductDto productDto){
        Product product = convertToEntity(productDto);
        return toResponse(convertToDto(productService.createProduct(product)));
    }

    @PostMapping("/import")
    public Response<List<ProductDto>> batchUpload(@RequestParam("file") MultipartFile dataFile) throws IOException {
        List<Product> productList = new ArrayList<Product>();

        XSSFWorkbook workbook = new XSSFWorkbook(dataFile.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {
            XSSFRow row = worksheet.getRow(i);

            Product product = new Product();

            product.setName(row.getCell(1).getStringCellValue());
            product.setDescription(row.getCell(2).getStringCellValue());
            product.setListPrice(row.getCell(3).getNumericCellValue());
            product.setOfferPrice(row.getCell(4).getNumericCellValue());
            product.setStock((int)row.getCell(5).getNumericCellValue());

            productList.add(product);
        }

        List<Product> productListResult = productService.bacthUpload(productList);

        return toResponse(
                productListResult.stream()
                        .map(post -> convertToDto(post))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/getAll")
    public Response<List<ProductDto>> getAll(){
        List<Product> products = productService.getAllProduct();
        return toResponse(
                products.stream()
                        .map(post -> convertToDto(post))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/getById/{productId}")
    public Response<ProductDto> getById(@PathVariable Long productId){
        return toResponse(convertToDto(productService.getProductById(productId)));
    }

    @GetMapping("/getByName/{name}")
    public Response<List<ProductDto>> getByName(@PathVariable String name){
        List<Product> products = productService.getAllProductByName(name);
        return toResponse(
                products.stream()
                        .map(post -> convertToDto(post))
                        .collect(Collectors.toList())
        );
    }

    @PutMapping("/updateById/{productId}")
    public Response<ProductDto> updateById(@PathVariable Long productId, @Valid @RequestBody ProductDto productDto) {
        Product product = convertToEntity(productDto);
        return toResponse(convertToDto(productService.updateProductById(productId, product)));
    }

    @DeleteMapping("/deleteById/{productId}")
    public Response<Boolean> deleteById(@PathVariable Long productId) {
        return toResponse(productService.deleteProductById(productId));
    }





    /*======================== Converter ======================*/
    private ProductDto convertToDto(Product product){
        MapperFacade mapper = mapperFactory.getMapperFacade();
        ProductDto productDto = mapper.map(product, ProductDto.class);

        return productDto;
    }

    private Product convertToEntity(ProductDto productDto){
        MapperFacade mapper = mapperFactory.getMapperFacade();
        Product product = mapper.map(productDto, Product.class);

        return product;
    }
}
