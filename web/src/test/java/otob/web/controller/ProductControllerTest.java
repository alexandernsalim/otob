package otob.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import otob.model.constant.path.ProductApiPath;
import otob.model.entity.Product;
import otob.model.exception.GlobalExceptionHandler;
import otob.service.ProductService;
import otob.web.model.ProductDto;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mvc;
    private ObjectMapper objectMapper;
    private Product product1;
    private Product product2;
    private Product product1Updated;
    private List<Product> products;

    @Before
    public void setUp() {
        initMocks(this);

        mvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();

        product1 = Product.builder()
                .productId(1L)
                .name("Redmi Note 7")
                .description("4/64 GB")
                .listPrice(2700000)
                .offerPrice(2000000)
                .stock(1)
                .build();

        product1Updated = Product.builder()
            .productId(1L)
            .name("Redmi Note 7")
            .description("4/64 GB")
            .listPrice(2000000)
            .offerPrice(1800000)
            .stock(1)
            .build();

        product2 = Product.builder()
                .productId(2L)
                .name("Acer Aspire E5")
                .description("i5")
                .listPrice(6000000)
                .offerPrice(4500000)
                .stock(1)
                .build();

        products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

    }

    @Test
    public void getAllProductTest() throws Exception {
        when(productService.getAllProduct())
            .thenReturn(products);

        mvc.perform(
            get(ProductApiPath.BASE_PATH)
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").isArray())
        .andExpect(jsonPath("$.data", hasSize(2)));

        verify(productService).getAllProduct();
    }

    @Test
    public void getAllProductByNameTest() throws Exception {
        products.remove(1);

        when(productService.getAllProductByName("Redmi"))
            .thenReturn(products);

        mvc.perform(
            get(ProductApiPath.BASE_PATH + ProductApiPath.GET_PRODUCT_BY_NAME, "Redmi")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data", hasSize(1)));

        verify(productService).getAllProductByName("Redmi");
    }

    @Test
    public void getProductByIdTest() throws Exception {
        when(productService.getProductById(1L))
            .thenReturn(product1);

        when(productService.getAllProductByName("Redmi"))
            .thenReturn(products);

        mvc.perform(
            get(ProductApiPath.BASE_PATH + ProductApiPath.GET_PRODUCT_BY_ID, 1L)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(product1));

        verify(productService).getProductById(1L);
    }

    @Test
    public void addProductTest() throws Exception {
        when(productService.addProduct(product1))
            .thenReturn(product1);

        mvc.perform(
            post(ProductApiPath.BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(product1));

        verify(productService).addProduct(product1);
    }

    @Test
    public void addProductFromExcelTest() throws Exception {
//        String content = "";
//
//        MockMultipartFile file = new MockMultipartFile("products", "products.xlsx", null, content.getBytes());
//
//        when(productService.addProducts(products))
//            .thenReturn(products);
//
//        mvc.perform(
//            MockMvcRequestBuilders.multipart(ProductApiPath.BASE_PATH)
//                .file(file)
//        )
    }

    @Test
    public void updateByIdTest() throws Exception {
        when(productService.updateProductById(product1.getProductId(), product1Updated))
            .thenReturn(product1Updated);

        mvc.perform(
            put(ProductApiPath.BASE_PATH + ProductApiPath.PRODUCTID_PLACEHOLDER, product1.getProductId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1Updated))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(product1Updated));

        verify(productService).updateProductById(product1.getProductId(), product1Updated);
    }

    @Test
    public void deleteByIdTest() throws Exception {
        when(productService.deleteProductById(product1.getProductId()))
            .thenReturn(true);

        mvc.perform(
            delete(ProductApiPath.BASE_PATH + ProductApiPath.PRODUCTID_PLACEHOLDER, product1.getProductId())
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(true));

        verify(productService).deleteProductById(product1.getProductId());
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(productService);
    }

}
