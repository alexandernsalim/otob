package otob.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import otob.constant.Status;
import otob.dto.CheckoutDto;
import otob.entity.Cart;
import otob.entity.CartItem;
import otob.entity.Order;
import otob.entity.Product;
import otob.enumerator.ErrorCode;
import otob.exception.CustomException;
import otob.generator.IdGenerator;
import otob.repository.CartRepository;
import otob.service.api.OrderService;
import otob.service.api.ProductService;
import otob.service.api.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @Mock
    private OrderService orderService;

    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private CartServiceImpl cartServiceImpl;

    private String userEmail;
    private int qty;
    private CartItem item;
    private List<CartItem> cartItems;
    private Cart cart;
    private Cart updatedCart;
    private Product product;
    private String orderId;
    private DateFormat dateFormat;
    private String orderDate;
    private Order order;
    private List<String> outOfStockProducts;
    private CheckoutDto checkoutResult;

    @Before
    public void setUp() {
        initMocks(this);

        userEmail = "alexandernsalim@gmail.com";
        qty = 1;

        item = CartItem.builder()
                .productId(1L)
                .productName("Redmi 7")
                .productPrice(1000000)
                .qty(1)
                .build();

        cartItems = new ArrayList<>();
        cartItems.add(item);

        cart = Cart.builder()
                .userEmail(userEmail)
                .build();

        updatedCart = Cart.builder()
                .userEmail(userEmail)
                .cartItems(cartItems)
                .build();

        product = Product.builder()
                .productId(1L)
                .name("Redmi 7")
                .description("Smartphone 2/16 GB")
                .listPrice(1500000)
                .offerPrice(1000000)
                .stock(4)
                .build();

        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        orderDate = dateFormat.format(new Date());
        orderId = "ORD1235648790";

        order = Order.builder()
                .orderId(orderId)
                .userEmail(userEmail)
                .ordDate(orderDate)
                .ordItems(cartItems)
                .totItem(1)
                .totPrice(1000000L)
                .ordStatus(Status.ORD_WAIT)
                .build();

        outOfStockProducts = new ArrayList<>();

        checkoutResult = CheckoutDto.builder()
                .order(order)
                .outOfStockProducts(outOfStockProducts)
                .build();
    }

    @Test
    public void createUserCartTest() {
        when(cartRepository.save(cart))
                .thenReturn(cart);

        Cart result = cartServiceImpl.createUserCart(userEmail);

        verify(cartRepository).save(cart);
        assertEquals(userEmail, result.getUserEmail());
    }

    @Test
    public void getUserCartTest() {
        when(userService.checkUser(userEmail))
                .thenReturn(true);
        when(cartRepository.findByUserEmail(userEmail))
                .thenReturn(cart);

        Cart result = cartServiceImpl.getUserCart(userEmail);

        verify(userService).checkUser(userEmail);
        verify(cartRepository).findByUserEmail(userEmail);
        assertEquals(userEmail, result.getUserEmail());
    }

    @Test
    public void getUserCartNotFoundTest() {
        when(userService.checkUser(userEmail))
                .thenReturn(false);

        try {
            cartServiceImpl.getUserCart(userEmail);
        } catch (CustomException ex) {
            verify(userService).checkUser(userEmail);
            assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void addItemToCartTest() {
        when(userService.checkUser(userEmail))
                .thenReturn(true);
        when(cartRepository.existsByUserEmail(userEmail))
                .thenReturn(true);
        when(productService.getProductById(1L))
                .thenReturn(product);
        when(cartRepository.addToCart(userEmail, qty, product))
                .thenReturn(updatedCart);

        Cart result = cartServiceImpl.addItemToCart(userEmail, 1L, 1);

        verify(userService).checkUser(userEmail);
        verify(cartRepository).existsByUserEmail(userEmail);
        verify(productService).getProductById(1L);
        verify(cartRepository).addToCart(userEmail, qty, product);
        assertTrue(result.getCartItems().size() >= 1);
        assertEquals(product.getName(), result.getCartItems().get(0).getProductName());
    }

    @Test
    public void addItemToCartUserNotFoundTest() {
        when(userService.checkUser(userEmail))
                .thenReturn(false);

        try {
            cartServiceImpl.addItemToCart(userEmail, 1L, 1);
        } catch (CustomException ex) {
            verify(userService).checkUser(userEmail);
            assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void addItemToCartCartNotExistTest() {
        when(userService.checkUser(userEmail))
                .thenReturn(true);
        when(cartRepository.existsByUserEmail(userEmail))
                .thenReturn(false);
        when(cartRepository.save(cart))
                .thenReturn(cart);
        when(productService.getProductById(1L))
                .thenReturn(product);
        when(cartRepository.addToCart(userEmail, qty, product))
                .thenReturn(updatedCart);

        Cart result = cartServiceImpl.addItemToCart(userEmail, 1L, 1);

        verify(userService).checkUser(userEmail);
        verify(cartRepository).existsByUserEmail(userEmail);
        verify(cartRepository).save(cart);
        verify(productService).getProductById(1L);
        verify(cartRepository).addToCart(userEmail, qty, product);
        assertTrue(result.getCartItems().size() >= 1);
        assertEquals(product.getName(), result.getCartItems().get(0).getProductName());

    }

    @Test
    public void updateItemQtyTest() {
        updatedCart.getCartItems().get(0).setQty(2);

        when(userService.checkUser(userEmail))
                .thenReturn(true);
        when(productService.getProductById(1L))
                .thenReturn(product);
        when(cartRepository.updateQty(userEmail, 1, product))
                .thenReturn(updatedCart);

        Cart result = cartServiceImpl.updateItemQty(userEmail, 1L, 1);

        verify(userService).checkUser(userEmail);
        verify(productService).getProductById(1L);
        verify(cartRepository).updateQty(userEmail, 1, product);
        assertEquals(2, result.getCartItems().get(0).getQty());
    }

    @Test
    public void updateItemQtyUserNotExistsTest() {
        when(userService.checkUser(userEmail))
                .thenReturn(false);

        try {
            cartServiceImpl.updateItemQty(userEmail, 1L, 1);
        } catch (CustomException ex) {
            verify(userService).checkUser(userEmail);
            assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void removeItemFromCartTest() {
        updatedCart.getCartItems().remove(0);

        when(userService.checkUser(userEmail))
                .thenReturn(true);
        when(cartRepository.removeFromCart(userEmail, 1L))
                .thenReturn(updatedCart);

        Cart result = cartServiceImpl.removeItemFromCart(userEmail, 1L);

        verify(userService).checkUser(userEmail);
        verify(cartRepository).removeFromCart(userEmail, 1L);
        assertTrue(result.getCartItems().isEmpty());
    }

    @Test
    public void removeItemFromCartUserNotExistTest() {
        when(userService.checkUser(userEmail))
                .thenReturn(false);

        try {
            cartServiceImpl.removeItemFromCart(userEmail, 1L);
        } catch (CustomException ex) {
            verify(userService).checkUser(userEmail);
            assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void removeUserCartTest() {
        when(cartRepository.deleteByUserEmail(userEmail))
                .thenReturn(1L);

        Boolean bool = cartServiceImpl.removeUserCart(userEmail);

        verify(cartRepository).deleteByUserEmail(userEmail);
        assertTrue(bool);
    }

    @Test
    public void removeUserCartFailTest() {
        when(cartRepository.deleteByUserEmail(userEmail))
                .thenReturn(0L);

        Boolean bool = cartServiceImpl.removeUserCart(userEmail);

        verify(cartRepository).deleteByUserEmail(userEmail);
        assertTrue(!bool);
    }

    @Test
    public void checkoutTest() throws Exception {
        when(userService.checkUser(userEmail))
                .thenReturn(true);
        when(cartRepository.findByUserEmail(userEmail))
                .thenReturn(updatedCart);
        when(productService.getProductById(product.getProductId()))
                .thenReturn(product);

        when(idGenerator.generateOrderId(orderDate))
                .thenReturn(orderId);

        CheckoutDto result = cartServiceImpl.checkout(userEmail);

        System.out.println(result.toString());

        verify(userService, times(2)).checkUser(userEmail);
        verify(cartRepository).findByUserEmail(userEmail);
        verify(productService).getProductById(product.getProductId());
        verify(productService).updateProductById(1L, product);
        verify(cartRepository).removeFromCart(userEmail, 1L);
        verify(idGenerator).generateOrderId(orderDate);
        verify(orderService).createOrder(order);
        assertEquals(checkoutResult.getOrder(), result.getOrder());
    }

    @After
    public void teardown() {
        verifyNoMoreInteractions(cartRepository);
        verifyNoMoreInteractions(productService);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(orderService);
        verifyNoMoreInteractions(idGenerator);
    }

}
