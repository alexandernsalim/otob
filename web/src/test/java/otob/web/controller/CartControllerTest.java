package otob.web.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import otob.model.constant.Status;
import otob.model.constant.path.CartApiPath;
import otob.model.entity.Cart;
import otob.model.entity.CartItem;
import otob.model.entity.Order;
import otob.model.exception.GlobalExceptionHandler;
import otob.service.CartService;
import otob.util.mapper.BeanMapper;
import otob.web.model.CartDto;
import otob.web.model.OrderDto;

import java.util.ArrayList;
import java.util.List;

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

public class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private MockMvc mvc;
    private MockHttpServletRequest request;
    private MockHttpSession session;

    private String userEmail;
    private String  orderId;
    private List<CartItem> cartItems;
    private Cart cart;
    private CartDto cartDto;
    private Order order;
    private OrderDto orderDto;

    @Before
    public void setUp() {
        initMocks(this);

        mvc = MockMvcBuilders.standaloneSetup(cartController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        request = new MockHttpServletRequest();
        session = new MockHttpSession();
        request.setSession(session);

        userEmail = "user@mail.com";
        orderId = "ORD1561436040000";
        CartItem item = CartItem.builder()
                .productId(1L)
                .productName("Redmi 7")
                .productPrice(1000000)
                .qty(1)
                .build();
        cartItems = new ArrayList<>();
        cartItems.add(item);
        cart = Cart.builder()
                .id("user@mail.com")
                .userEmail(userEmail)
                .cartItems(cartItems)
                .build();
        cartDto = CartDto.builder()
                .userEmail(userEmail)
                .cartItems(cartItems)
                .build();
        order = Order.builder()
                .orderId(orderId)
                .userEmail(userEmail)
                .ordDate("2019/06/25 11:14")
                .ordItems(cartItems)
                .totItem(1)
                .totPrice(5000000L)
                .ordStatus(Status.ORD_WAIT)
                .build();
        orderDto = BeanMapper.map(order, OrderDto.class);
    }

    @Test
    public void getCartItemsTest() throws Exception {
        when(cartService.getUserCart(userEmail))
            .thenReturn(cart);

        mvc.perform(
            get(CartApiPath.BASE_PATH)
                .sessionAttr("userId", userEmail)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(cartDto));

        verify(cartService).getUserCart(userEmail);
    }

    @Test
    public void addItemToCartTest() throws Exception {
        when(cartService.addItemToCart(userEmail, 1L, 1))
                .thenReturn(cart);

        mvc.perform(
            post(CartApiPath.BASE_PATH + CartApiPath.ADD_OR_UPDATE_ITEM, 1, 1)
                .sessionAttr("userId", userEmail)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(cartDto));

        verify(cartService).addItemToCart(userEmail, 1L, 1);
    }

    @Test
    public void updateItemQtyTest() throws Exception {
        cart.getCartItems().get(0).setQty(2);
        cartDto.getCartItems().get(0).setQty(2);
        when(cartService.updateItemQty(userEmail, 1L, 1))
                .thenReturn(cart);

        mvc.perform(
            put(CartApiPath.BASE_PATH + CartApiPath.ADD_OR_UPDATE_ITEM, 1, 1)
                .sessionAttr("userId", userEmail)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(cartDto));

        verify(cartService).updateItemQty(userEmail, 1L, 1);
    }

    @Test
    public void removeItemFromCartTest() throws Exception {
        cart.getCartItems().clear();
        cartDto.getCartItems().clear();
        when(cartService.removeItemFromCart(userEmail, 1L))
                .thenReturn(cart);

        mvc.perform(
            delete(CartApiPath.BASE_PATH + CartApiPath.REMOVE_ITEM, 1)
                .sessionAttr("userId", userEmail)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(cartDto));

        verify(cartService).removeItemFromCart(userEmail, 1L);
    }

    @Test
    public void checkoutTest() throws Exception {
        when(cartService.checkout(userEmail))
                .thenReturn(order);

        mvc.perform(
            get(CartApiPath.BASE_PATH + CartApiPath.CHECKOUT)
                .sessionAttr("userId", userEmail)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(orderDto));

        verify(cartService).checkout(userEmail);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(cartService);
    }

}
