package otob.service.impl;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import otob.model.entity.Product;
import otob.model.enumerator.ErrorCode;
import otob.model.exception.CustomException;
import otob.repository.ProductRepository;
import otob.util.generator.IdGenerator;
import otob.web.model.PageableProductDto;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    private Pageable pageable;
    private Integer page;
    private Integer size;
    private Product product1;
    private Product product2;
    private Product productUpdated2;
    private Product excelProduct;
    private List<Product> products;
    private List<Product> productsByName;
    private List<Product> emptyProducts;

    @Before
    public void setUp() {
        initMocks(this);

        page = 0;
        size = 5;
        pageable = PageRequest.of(page, size);

        product1 = Product.builder()
                .productId(1L)
                .productName("Asus")
                .productCondition("Laptop")
                .productListPrice(7500000)
                .productOfferPrice(5000000)
                .productStock(1)
                .build();

        product2 = Product.builder()
                .productId(2L)
                .productName("Xiaomi")
                .productCondition("Handphone")
                .productListPrice(3000000)
                .productOfferPrice(2000000)
                .productStock(2)
                .build();

        productUpdated2 = Product.builder()
                .productId(2L)
                .productName("Xiaomi")
                .productCondition("Handphone")
                .productListPrice(3000000)
                .productOfferPrice(1500000)
                .productStock(2)
                .build();

        excelProduct = Product.builder()
                .productId(3L)
                .productName("Note FE")
                .productCondition("Mismatch Product")
                .productListPrice(8000000)
                .productOfferPrice(4000000)
                .productStock(1)
                .build();

        products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        emptyProducts = new ArrayList<>();
        productsByName = new ArrayList<>();
        productsByName.add(product1);
    }

    @Test
    public void getAllProductTest() {
        when(productRepository.findAll(pageable)).thenReturn(new PageImpl<>(products));

        PageableProductDto result = productServiceImpl.getAllProduct(page, size);

        verify(productRepository).findAll(pageable);
        assertTrue(result.getProducts().size() >= 1);
    }

    @Test
    public void getAllProductEmptyTest() {
        when(productRepository.findAll(pageable)).thenReturn(new PageImpl<>(emptyProducts));

        try {
            productServiceImpl.getAllProduct(page, size);
        } catch (CustomException ex) {
            verify(productRepository).findAll(pageable);
            TestCase.assertEquals(ErrorCode.PRODUCT_NOT_FOUND.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void getProductByIdTest() {
        when(productRepository.findByProductId(1L))
                .thenReturn(product1);

        Product result = productServiceImpl.getProductById(1L);

        verify(productRepository).findByProductId(1L);
        assertEquals(result.getProductName(), product1.getProductName());
    }

    @Test
    public void getProductByIdNotFoundTest() {
        when(productRepository.findByProductId(1L))
                .thenReturn(null);

        try {
            productServiceImpl.getProductById(1L);
        } catch (CustomException ex) {
            verify(productRepository).findByProductId(1L);
            assertEquals(ErrorCode.PRODUCT_NOT_FOUND.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void getAllProductByNameTest() {
        when(productRepository.findAllByProductNameContaining("Asus", pageable))
            .thenReturn(new PageImpl<>(productsByName));

        PageableProductDto result = productServiceImpl.getAllProductByName("Asus", page, size);

        verify(productRepository).findAllByProductNameContaining("Asus", pageable);
        assertTrue(result.getProducts().size() >= 1);
    }

    @Test
    public void getAllProductByNameEmptyTest() {
        when(productRepository.findAllByProductNameContaining("Asus", pageable))
                .thenReturn(new PageImpl<>(emptyProducts));

        try {
            productServiceImpl.getAllProductByName("Asus", page, size);
        } catch (CustomException ex) {
            verify(productRepository).findAllByProductNameContaining("Asus", pageable);
            assertEquals(ErrorCode.PRODUCT_NOT_FOUND.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void addProductExistsTest() {
        when(productRepository.existsByProductName(product2.getProductName()))
                .thenReturn(true);
        when(productRepository.findByProductName(product2.getProductName()))
                .thenReturn(product2);
        when(productRepository.save(product2))
                .thenReturn(product2);

        Product result = productServiceImpl.addProduct(product2);

        verify(productRepository).existsByProductName(product2.getProductName());
        verify(productRepository).findByProductName(product2.getProductName());
        verify(productRepository).save(product2);
        assertEquals(product2.getProductName(), result.getProductName());
    }

    @Test
    public void addProductNotExistsTest() throws Exception {
        when(productRepository.existsByProductName(product1.getProductName()))
                .thenReturn(false);
        when(idGenerator.getNextId("productid"))
                .thenReturn(1L);
        when(productRepository.save(product1))
                .thenReturn(product1);

        Product result = productServiceImpl.addProduct(product1);

        verify(productRepository).existsByProductName(product1.getProductName());
        verify(idGenerator).getNextId("productid");
        verify(productRepository).save(product1);
        assertEquals(product1.getProductName(), result.getProductName());
    }

    @Test
    public void addProductGenerateIdErrorTest() throws Exception {
        when(productRepository.existsByProductName(product1.getProductName()))
                .thenReturn(false);
        when(idGenerator.getNextId("productid"))
                .thenThrow(new Exception());

        try {
            productServiceImpl.addProduct(product1);
        } catch (CustomException ex) {
            verify(productRepository).existsByProductName(product1.getProductName());
            verify(idGenerator).getNextId("productid");
            assertEquals(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void addProductsTest() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", new FileInputStream(new File("/home/alexandernsalim/Projects/FUTURE/init/Simple Product List.xlsx")));

        when(productRepository.existsByProductName(anyString()))
            .thenReturn(false);
        when(idGenerator.getNextId("productid"))
            .thenReturn(any());

        List<Product> result = productServiceImpl.addProducts(file);

        verify(productRepository, times(5)).existsByProductName(anyString());
        verify(idGenerator, times(5)).getNextId("productid");
        verify(productRepository, times(5)).save(any());
        assertTrue(result.size() >= 1);
    }

    @Test
    public void addProductsSameTest() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", new FileInputStream(new File("/home/alexandernsalim/Projects/FUTURE/init/Same Product Test.xlsx")));

        when(productRepository.existsByProductName(anyString()))
            .thenReturn(true);
        when(productRepository.findByProductName("Note FE"))
            .thenReturn(excelProduct);
        when(productRepository.save(excelProduct))
            .thenReturn(excelProduct);

        List<Product> result = productServiceImpl.addProducts(file);

        verify(productRepository).existsByProductName(anyString());
        verify(productRepository).findByProductName("Note FE");
        verify(productRepository).save(excelProduct);
        assertTrue(result.size() >= 1);
    }

    @Test
    public void addProductsFileFormatWrongTest() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", new FileInputStream(new File("/home/alexandernsalim/Projects/FUTURE/init/Wrong Format Test.xlsx")));

        try {
            productServiceImpl.addProducts(file);
        } catch (CustomException ex) {
            assertEquals(ErrorCode.EXCEL_FORMAT_ERROR.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void addProductsGenerateIdFailTest() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", new FileInputStream(new File("/home/alexandernsalim/Projects/FUTURE/init/Same Product Test.xlsx")));

        when(productRepository.existsByProductName(anyString()))
                .thenReturn(false);
        when(idGenerator.getNextId("productid"))
                .thenThrow(new Exception());

        try {
            productServiceImpl.addProducts(file);
        } catch (CustomException ex) {
            verify(productRepository).existsByProductName(anyString());
            verify(idGenerator).getNextId("productid");
            assertEquals(ErrorCode.GENERATE_ID_FAIL.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void updateProductByIdTest() {
        when(productRepository.findByProductId(2L))
                .thenReturn(product2);
        when(productRepository.save(product2))
                .thenReturn(productUpdated2);

        Product result = productServiceImpl.updateProductById(2L, product2);

        verify(productRepository).findByProductId(2L);
        verify(productRepository).save(product2);
        assertEquals(productUpdated2.getProductName(), result.getProductName());
        assertEquals(productUpdated2.getProductListPrice(), result.getProductListPrice());
    }

    @Test
    public void updateProductByIdNotFoundTest() {
        when(productRepository.findByProductId(3L))
                .thenReturn(null);

        try {
            productServiceImpl.updateProductById(3L, any());
        } catch (CustomException ex) {
            verify(productRepository).findByProductId(3L);
            assertEquals(ErrorCode.BAD_REQUEST.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void updateProductByNameTest() {
        when(productRepository.findByProductName("Xiaomi"))
                .thenReturn(product2);
        when(productRepository.save(product2))
                .thenReturn(productUpdated2);

        Product result = productServiceImpl.updateProductByName(product2);

        verify(productRepository).findByProductName("Xiaomi");
        verify(productRepository).save(product2);
        assertEquals(productUpdated2.getProductName(), result.getProductName());
        assertEquals(productUpdated2.getProductListPrice(), result.getProductListPrice());
    }

    @Test
    public void updateProductByNameNotFoundTest() {
        when(productRepository.findByProductName("Xiaomi"))
                .thenReturn(null);

        try {
            productServiceImpl.updateProductByName(product2);
        } catch (CustomException ex) {
            verify(productRepository).findByProductName("Xiaomi");
            assertEquals(ErrorCode.BAD_REQUEST.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void deleteProductByIdTest() {
        when(productRepository.findByProductId(1L))
                .thenReturn(product1);

        boolean result = productServiceImpl.deleteProductById(1L);

        verify(productRepository).findByProductId(1L);
        verify(productRepository).delete(product1);
        assertEquals(true, result);
    }

    @Test
    public void deleteProductByIdNotFoundTest() {
        when(productRepository.findByProductId(1L))
                .thenReturn(null);
        try {
            productServiceImpl.deleteProductById(1L);
        } catch (CustomException ex) {
            verify(productRepository).findByProductId(1L);
            assertEquals(ErrorCode.BAD_REQUEST.getMessage(), ex.getMessage());
        }
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(idGenerator);
        verifyNoMoreInteractions(productRepository);
    }

}
