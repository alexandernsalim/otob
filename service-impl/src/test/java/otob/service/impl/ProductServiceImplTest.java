package otob.service.impl;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import otob.model.entity.Product;
import otob.model.enumerator.ErrorCode;
import otob.model.exception.CustomException;
import otob.util.generator.IdGenerator;
import otob.repository.ProductRepository;

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

    private Product product1;
    private Product product2;
    private Product productUpdated2;
    private List<Product> products;
    private List<Product> productsByName;
    private List<Product> emptyProducts;

    @Before
    public void setUp() {
        initMocks(this);

        product1 = Product.builder()
                .productId(1L)
                .name("Asus")
                .description("Laptop")
                .listPrice(7500000)
                .offerPrice(5000000)
                .stock(1)
                .build();

        product2 = Product.builder()
                .productId(2L)
                .name("Xiaomi")
                .description("Handphone")
                .listPrice(3000000)
                .offerPrice(2000000)
                .stock(2)
                .build();

        productUpdated2 = Product.builder()
                .productId(2L)
                .name("Xiaomi")
                .description("Handphone")
                .listPrice(3000000)
                .offerPrice(1500000)
                .stock(2)
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
        when(productRepository.findAll())
                .thenReturn(products);

        List<Product> result = productServiceImpl.getAllProduct();

        verify(productRepository).findAll();
        assertTrue(result.size() >= 1);
    }

    @Test
    public void getAllProductNotFoundTest() {
        when(productRepository.findAll())
                .thenReturn(emptyProducts);

        try {
            productServiceImpl.getAllProduct();
        } catch (CustomException ex) {
            verify(productRepository).findAll();
            TestCase.assertEquals(ErrorCode.PRODUCT_NOT_FOUND.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void getProductByIdTest() {
        when(productRepository.findByProductId(1L))
                .thenReturn(product1);

        Product result = productServiceImpl.getProductById(1L);

        verify(productRepository).findByProductId(1L);
        assertEquals(result.getName(), product1.getName());
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
        when(productRepository.existsByNameContaining("Asus"))
                .thenReturn(true);
        when(productRepository.findAllByNameContaining("Asus"))
                .thenReturn(productsByName);

        List<Product> result = productServiceImpl.getAllProductByName("Asus");

        verify(productRepository).existsByNameContaining("Asus");
        verify(productRepository).findAllByNameContaining("Asus");
        assertTrue(result.size() >= 1);
    }

    @Test
    public void getAllProductByNameNotExistsTest() {
        when(productRepository.existsByNameContaining("Asus"))
                .thenReturn(false);

        try {
            productServiceImpl.getAllProductByName("Asus");
        } catch (CustomException ex) {
            verify(productRepository).existsByNameContaining("Asus");
            assertEquals(ErrorCode.PRODUCT_NOT_FOUND.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void addProductTest() throws Exception {
        when(productRepository.existsByName(product1.getName()))
                .thenReturn(false);
        when(idGenerator.getNextId("productid"))
                .thenReturn(1L);
        when(productRepository.save(product1))
                .thenReturn(product1);

        Product result = productServiceImpl.addProduct(product1);

        verify(productRepository).existsByName(product1.getName());
        verify(idGenerator).getNextId("productid");
        verify(productRepository).save(product1);
        assertEquals(product1.getName(), result.getName());
    }

    @Test
    public void addProductExistsTest() throws Exception {
        when(productRepository.existsByName(product1.getName()))
                .thenReturn(true);
        when(productRepository.save(product1))
                .thenReturn(product1);

        Product result = productServiceImpl.addProduct(product1);

        verify(productRepository).existsByName(product1.getName());
        verify(productRepository).save(product1);
        assertEquals(product1.getName(), result.getName());
    }

    @Test
    public void addProductGenerateIdErrorTest() throws Exception {
        when(productRepository.existsByName(product1.getName()))
                .thenReturn(false);
        when(idGenerator.getNextId("productid"))
                .thenThrow(new Exception());

        try {
            productServiceImpl.addProduct(product1);
        } catch (CustomException ex) {
            verify(productRepository).existsByName(product1.getName());
            verify(idGenerator).getNextId("productid");
            assertEquals(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), ex.getMessage());
        }
    }

//    @Test
//    public void addProductFromExcelTest() throws Exception {
//        when(productRepository.existsByName(product1.getName()))
//                .thenReturn(false);
//        when(productRepository.existsByName(product2.getName()))
//                .thenReturn(false);
//        when(idGenerator.getNextId("productid"))
//                .thenReturn(1L, 2L);
//        when(productRepository.save(product1))
//                .thenReturn(product1);
//        when(productRepository.save(product2))
//                .thenReturn(product2);
//
//        List<Product> result = productServiceImpl.addProducts(products);
//
//        verify(productRepository).existsByName(product1.getName());
//        verify(productRepository).existsByName(product2.getName());
//        verify(idGenerator, times(2)).getNextId("productid");
//        verify(productRepository).save(product1);
//        verify(productRepository).save(product2);
//        assertEquals(2, result.size());
//    }

//    @Test
//    public void addProductFromExcelExistsTest() throws Exception {
//        when(productRepository.existsByName(product1.getName()))
//                .thenReturn(true);
//        when(productRepository.findByName(product1.getName()))
//                .thenReturn(product1);
//        when(productRepository.save(product1))
//                .thenReturn(product1);
//        when(productRepository.existsByName(product2.getName()))
//                .thenReturn(false);
//        when(idGenerator.getNextId("productid"))
//                .thenReturn(2L);
//        when(productRepository.save(product2))
//                .thenReturn(product2);
//
//        List<Product> result = productServiceImpl.addProducts(products);
//
//        verify(productRepository).existsByName(product1.getName());
//        verify(productRepository).findByName(product1.getName());
//        verify(productRepository).save(product1);
//        verify(productRepository).existsByName(product2.getName());
//        verify(idGenerator).getNextId("productid");
//        verify(productRepository).save(product2);
//        assertEquals(2, result.size());
//    }
//
//    @Test
//    public void addProductFromExcelGenerateIdErrorTest() throws Exception {
//        when(productRepository.existsByName(product1.getName()))
//                .thenReturn(false);
//        when(idGenerator.getNextId("productid"))
//                .thenThrow(new Exception());
//
//        try {
//            productServiceImpl.addProducts(products);
//        } catch (CustomException ex) {
//            verify(productRepository).existsByName(product1.getName());
//            verify(idGenerator).getNextId("productid");
//            assertEquals(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), ex.getMessage());
//        }
//    }

    @Test
    public void updateProductByIdTest() {
        when(productRepository.findByProductId(2L))
                .thenReturn(product2);
        when(productRepository.save(product2))
                .thenReturn(productUpdated2);

        Product result = productServiceImpl.updateProductById(2L, product2);

        verify(productRepository).findByProductId(2L);
        verify(productRepository).save(product2);
        assertEquals(productUpdated2.getName(), result.getName());
        assertEquals(productUpdated2.getListPrice(), result.getListPrice());
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
        when(productRepository.findByName("Xiaomi"))
                .thenReturn(product2);
        when(productRepository.save(product2))
                .thenReturn(productUpdated2);

        Product result = productServiceImpl.updateProductByName(product2);

        verify(productRepository).findByName("Xiaomi");
        verify(productRepository).save(product2);
        assertEquals(productUpdated2.getName(), result.getName());
        assertEquals(productUpdated2.getListPrice(), result.getListPrice());
    }

    @Test
    public void updateProductByNameNotFoundTest() {
        when(productRepository.findByName("Xiaomi"))
                .thenReturn(null);

        try {
            productServiceImpl.updateProductByName(product2);
        } catch (CustomException ex) {
            verify(productRepository).findByName("Xiaomi");
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
