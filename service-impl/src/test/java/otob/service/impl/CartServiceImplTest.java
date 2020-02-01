package otob.service.impl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import otob.model.constant.Status;
import otob.model.entity.Cart;
import otob.model.entity.CartItem;
import otob.model.entity.Order;
import otob.model.entity.Product;
import otob.model.enumerator.ErrorCode;
import otob.model.exception.CustomException;
import otob.model.exception.OutOfStockException;
import otob.repository.CartRepository;
import otob.service.OrderService;
import otob.service.ProductService;
import otob.service.UserService;
import otob.util.generator.IdGenerator;

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

    @Before
    public void setUp() {
        initMocks(this);

        userEmail = "alexandernsalim@gmail.com";
        qty = 1;

        item = CartItem.builder()
                .productId("B-1234")
                .cartItemName("Redmi 7")
                .cartItemOfferPrice(1000000)
                .cartItemQty(1)
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
                .productId("B-1234")
                .name("Redmi 7")
                .condition("Smartphone 2/16 GB")
                .listPrice(1500000)
                .offerPrice(1000000)
                .stock(4)
                .build();

        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        orderDate = dateFormat.format(new Date());
        orderId = "ORD1235648790";

        order = Order.builder()
                .orderId(orderId)
                .userEmail(userEmail)
                .orderDate(orderDate)
                .orderItems(cartItems)
                .orderTotalItem(1)
                .orderTotalPrice(1000000L)
                .orderStatus(Status.ORD_WAIT)
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
            Assert.assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void addItemToCartTest() {
        when(userService.checkUser(userEmail))
                .thenReturn(true);
        when(cartRepository.existsByUserEmail(userEmail))
                .thenReturn(true);
        when(productService.getProductById("B-1234"))
                .thenReturn(product);
        when(cartRepository.addToCart(userEmail, qty, product))
                .thenReturn(updatedCart);

        Cart result = cartServiceImpl.addItemToCart(userEmail, "B-1234", 1);

        verify(userService).checkUser(userEmail);
        verify(cartRepository).existsByUserEmail(userEmail);
        verify(productService).getProductById("B-1234");
        verify(cartRepository).addToCart(userEmail, qty, product);
        assertTrue(result.getCartItems().size() >= 1);
        assertEquals(product.getName(), result.getCartItems().get(0).getCartItemName());
    }

    @Test
    public void addItemToCartUserNotFoundTest() {
        when(userService.checkUser(userEmail))
                .thenReturn(false);

        try {
            cartServiceImpl.addItemToCart(userEmail, "B-1234", 1);
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
        when(productService.getProductById("B-1234"))
                .thenReturn(product);
        when(cartRepository.addToCart(userEmail, qty, product))
                .thenReturn(updatedCart);

        Cart result = cartServiceImpl.addItemToCart(userEmail, "B-1234", 1);

        verify(userService).checkUser(userEmail);
        verify(cartRepository).existsByUserEmail(userEmail);
        verify(cartRepository).save(cart);
        verify(productService).getProductById("B-1234");
        verify(cartRepository).addToCart(userEmail, qty, product);
        assertTrue(result.getCartItems().size() >= 1);
        assertEquals(product.getName(), result.getCartItems().get(0).getCartItemName());

    }

    @Test
    public void updateItemQtyTest() {
        updatedCart.getCartItems().get(0).setCartItemQty(2);

        when(userService.checkUser(userEmail))
                .thenReturn(true);
        when(productService.getProductById("B-1234"))
                .thenReturn(product);
        when(cartRepository.updateQty(userEmail, 1, product))
                .thenReturn(updatedCart);

        Cart result = cartServiceImpl.updateItemQty(userEmail, "B-1234", 1);

        verify(userService).checkUser(userEmail);
        verify(productService).getProductById("B-1234");
        verify(cartRepository).updateQty(userEmail, 1, product);
        assertEquals(2, result.getCartItems().get(0).getCartItemQty());
    }

    @Test
    public void updateItemQtyUserNotExistsTest() {
        when(userService.checkUser(userEmail))
                .thenReturn(false);

        try {
            cartServiceImpl.updateItemQty(userEmail, "B-1234", 1);
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
        when(cartRepository.removeFromCart(userEmail, "B-1234"))
                .thenReturn(updatedCart);

        Cart result = cartServiceImpl.removeItemFromCart(userEmail, "B-1234");

        verify(userService).checkUser(userEmail);
        verify(cartRepository).removeFromCart(userEmail, "B-1234");
        assertTrue(result.getCartItems().isEmpty());
    }

    @Test
    public void removeItemFromCartUserNotExistTest() {
        when(userService.checkUser(userEmail))
                .thenReturn(false);

        try {
            cartServiceImpl.removeItemFromCart(userEmail, "B-1234");
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
        when(orderService.createOrder(order))
                .thenReturn(order);

        Order result = cartServiceImpl.checkout(userEmail);

        verify(userService, times(2)).checkUser(userEmail);
        verify(cartRepository).findByUserEmail(userEmail);
        verify(productService, times(2)).getProductById(product.getProductId());
        verify(productService).updateProductById("B-1234", product);
        verify(cartRepository).removeFromCart(userEmail, "B-1234");
        verify(idGenerator).generateOrderId(orderDate);
        verify(orderService).createOrder(order);
        Assert.assertEquals(order, result);
    }

    @Test
    public void checkoutCartItemsEmptyTest() {
        updatedCart.getCartItems().clear();

        when(userService.checkUser(userEmail))
                .thenReturn(true);
        when(cartRepository.findByUserEmail(userEmail))
                .thenReturn(updatedCart);

        try {
            cartServiceImpl.checkout(userEmail);
        } catch (CustomException ex) {
            verify(userService).checkUser(userEmail);
            verify(cartRepository).findByUserEmail(userEmail);
            assertEquals(ErrorCode.BAD_REQUEST.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void checkoutCartOutOfStockTest() {
        product.setStock(0);

        when(userService.checkUser(userEmail))
            .thenReturn(true);
        when(cartRepository.findByUserEmail(userEmail))
            .thenReturn(updatedCart);
        when(productService.getProductById(product.getProductId()))
            .thenReturn(product);

        try {
            cartServiceImpl.checkout(userEmail);
        } catch (OutOfStockException ex) {
            verify(userService).checkUser(userEmail);
            verify(cartRepository).findByUserEmail(userEmail);
            verify(productService).getProductById(product.getProductId());
            assertEquals(ErrorCode.SOME_PRODUCTS_INVALID.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void checkoutGenerateIdErrorTest() throws Exception {
        when(userService.checkUser(userEmail))
            .thenReturn(true);
        when(cartRepository.findByUserEmail(userEmail))
            .thenReturn(updatedCart);
        when(productService.getProductById(product.getProductId()))
            .thenReturn(product);
        when(idGenerator.generateOrderId(orderDate))
            .thenThrow(new Exception());

        try {
            cartServiceImpl.checkout(userEmail);
        } catch (CustomException ex) {
            verify(userService, times(2)).checkUser(userEmail);
            verify(cartRepository).findByUserEmail(userEmail);
            verify(productService, times(2)).getProductById(product.getProductId());
            verify(productService).updateProductById("B-1234", product);
            verify(cartRepository).removeFromCart(userEmail, "B-1234");
            verify(idGenerator).generateOrderId(orderDate);
            assertEquals(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), ex.getMessage());
        }
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(cartRepository);
        verifyNoMoreInteractions(productService);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(orderService);
        verifyNoMoreInteractions(idGenerator);
    }

}
