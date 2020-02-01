package otob.web.controller;

import net.bytebuddy.implementation.bind.annotation.Empty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import otob.model.constant.path.ProductApiPath;
import otob.model.entity.Product;
import otob.model.response.Response;
import otob.service.ProductService;
import otob.util.mapper.BeanMapper;
import otob.web.model.PageableProductDto;
import otob.web.model.ProductDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(ProductApiPath.BASE_PATH)
public class ProductController extends GlobalController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BeanMapper mapper;

    @GetMapping
    public Response<PageableProductDto> getAllProduct(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size
    ) {
        page = (page == null) ? 0 : page-1;
        size = (size == null) ? 5 : size;

        return toResponse(productService.getAllProduct(page, size));
    }

    @GetMapping(ProductApiPath.GET_PRODUCT_BY_NAME)
    public Response<PageableProductDto> getAllProductByName(
        @PathVariable String productName,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size
    ) {
        page = (page == null) ? 0 : page-1;
        size = (size == null) ? 5 : size;

        return toResponse(productService.getAllProductByName(productName, page, size));
    }

    @GetMapping(ProductApiPath.GET_PRODUCT_BY_ID)
    public Response<ProductDto> getProductById(@PathVariable String productId) {
        return toResponse(mapper.map(productService.getProductById(productId), ProductDto.class));
    }

    @PostMapping
    public Response<ProductDto> addProduct(@Valid @RequestBody ProductDto productDto) {
        Product product = mapper.map(productDto, Product.class);

        return toResponse(mapper.map(productService.addProduct(product), ProductDto.class));
    }

    @PostMapping(ProductApiPath.ADD_PRODUCT_FROM_EXCEL)
    public Response<List<ProductDto>> addProducts(@RequestParam("file") MultipartFile file) {

        return toResponse(mapper.mapAsList(productService.addProducts(file), ProductDto.class));
    }

    @PutMapping(ProductApiPath.PRODUCTID_PLACEHOLDER)
    public Response<ProductDto> updateById(@PathVariable String productId, @Valid @RequestBody ProductDto productDto) {
        Product product = mapper.map(productDto, Product.class);

        return toResponse(mapper.map(productService.updateProductById(productId, product), ProductDto.class));
    }

    @DeleteMapping(ProductApiPath.PRODUCTID_PLACEHOLDER)
    public Response<Boolean> deleteById(@PathVariable String productId) {

        return toResponse(productService.deleteProductById(productId));
    }

}
