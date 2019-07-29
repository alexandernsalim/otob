package otob.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
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
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mvc;
    private ObjectMapper objectMapper;
    private List<Product> products;
    private List<ProductDto> productsResponse;

    @Before
    public void setUp() {
        initMocks(this);

        mvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();

        Product product1 = Product.builder()
                .productId(1L)
                .name("Redmi Note 7")
                .description("4/64 GB")
                .listPrice(2700000)
                .offerPrice(2000000)
                .stock(1)
                .build();

        Product product2 = Product.builder()
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

        ProductDto productRes1 = ProductDto.builder()
                .productId(1)
                .name("Redmi Note 7")
                .description("4/64 GB")
                .listPrice(2700000)
                .offerPrice(2000000)
                .stock(1)
                .build();

        ProductDto productRes2 = ProductDto.builder()
                .productId(2)
                .name("Acer Aspire E5")
                .description("i5")
                .listPrice(6000000)
                .offerPrice(4500000)
                .stock(1)
                .build();

        productsResponse = new ArrayList<>();
        productsResponse.add(productRes1);
        productsResponse.add(productRes2);

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

    @After
    public void tearDown() {

    }

}
