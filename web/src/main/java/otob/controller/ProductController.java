package otob.controller;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import otob.dto.ProductDto;
import otob.entity.Product;
import otob.mapper.BeanMapper;
import otob.response.Response;
import otob.service.impl.ProductService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController extends GlobalController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BeanMapper mapper;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    @PostMapping("/")
    public Response<ProductDto> create(@Valid @RequestBody ProductDto productDto){
        Product product = mapper.map(productDto, Product.class);

        return toResponse(mapper.map(productService.createProduct(product), ProductDto.class));
    }

    @PostMapping("/import")
    public Response<List<ProductDto>> batchUpload(@RequestParam("file") MultipartFile dataFile) throws IOException {
        List<Product> productList = new ArrayList<Product>();

        XSSFWorkbook workbook = new XSSFWorkbook(dataFile.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for(int i=2;i<worksheet.getPhysicalNumberOfRows() ;i++) {
            XSSFRow row = worksheet.getRow(i);

            Product product = new Product();

            product.setName(row.getCell(1).getStringCellValue());
            product.setDescription(row.getCell(2).getStringCellValue());
            product.setListPrice(row.getCell(3).getNumericCellValue());
            product.setOfferPrice(row.getCell(4).getNumericCellValue());
            product.setStock((int)row.getCell(5).getNumericCellValue());

            productList.add(product);
        }

        List<Product> productListResult = productService.batchUpload(productList);

        return toResponse(mapper.mapAsList(productListResult, ProductDto.class));
    }

    @GetMapping("/")
    public Response<List<ProductDto>> getAll(){
        List<Product> products = productService.getAllProduct();

        return toResponse(mapper.mapAsList(products, ProductDto.class));
    }

    @GetMapping("/getById/{productId}")
    public Response<ProductDto> getById(@PathVariable Long productId){
        return toResponse(mapper.map(productService.getProductById(productId), ProductDto.class));
    }

    @GetMapping("/getByName/{name}")
    public Response<List<ProductDto>> getByName(@PathVariable String name){
        List<Product> products = productService.getAllProductByName(name);

        return toResponse(mapper.mapAsList(products, ProductDto.class));
    }

    @PutMapping("/updateById/{productId}")
    public Response<ProductDto> updateById(@PathVariable Long productId, @Valid @RequestBody ProductDto productDto) {
        Product product = mapper.map(productDto, Product.class);

        return toResponse(mapper.map(productService.updateProductById(productId, product), ProductDto.class));
    }

    @DeleteMapping("/deleteById/{productId}")
    public Response<Boolean> deleteById(@PathVariable Long productId) {
        return toResponse(productService.deleteProductById(productId));
    }

}
