package otob.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import otob.model.constant.path.OrderApiPath;
import otob.model.entity.CartItem;
import otob.model.entity.Order;
import otob.model.exception.GlobalExceptionHandler;
import otob.service.OrderService;
import otob.util.mapper.BeanMapper;
import otob.web.model.OrderDto;
import otob.web.model.PageableOrderDto;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mvc;
    private MockHttpServletRequest request;
    private MockHttpSession session;
    private ObjectMapper objectMapper;

    private Integer page;
    private Integer size;
    private CartItem item1;
    private List<CartItem> items;
    private String orderId;
    private String userEmail;
    private Order order;
    private Order orderAccepted;
    private Order orderRejected;
    private List<Order> orders;
    private PageableOrderDto pageableOrderDto;

    @Before
    public void setUp() {
        initMocks(this);

        mvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        request = new MockHttpServletRequest();
        session = new MockHttpSession();
        request.setSession(session);
        objectMapper = new ObjectMapper();

        page = 0;
        size = 5;

        item1 = CartItem.builder()
                .productId(1L)
                .name("Asus")
                .offerPrice(5000000)
                .qty(1)
                .build();
        items = new ArrayList<>();
        items.add(item1);
        orderId = "ORD1561436040000";
        userEmail = "alexandernsalim@gmail.com";
        order = Order.builder()
                .ordId(orderId)
                .userEmail(userEmail)
                .ordDate("2019/06/25 11:14")
                .ordItems(items)
                .totItem(1)
                .totPrice(5000000L)
                .ordStatus(Status.ORD_WAIT)
                .build();
        orderAccepted = Order.builder()
                .ordId(orderId)
                .userEmail(userEmail)
                .ordDate("2019/06/25 11:14")
                .ordItems(items)
                .totItem(1)
                .totPrice(5000000L)
                .ordStatus(Status.ORD_ACCEPT)
                .build();
        orderRejected = Order.builder()
                .ordId(orderId)
                .userEmail(userEmail)
                .ordDate("2019/06/25 11:14")
                .ordItems(items)
                .totItem(1)
                .totPrice(5000000L)
                .ordStatus(Status.ORD_REJECT)
                .build();
        orders = new ArrayList<>();
        orders.add(order);

        pageableOrderDto = PageableOrderDto.builder()
            .totalPage(1)
            .orders(BeanMapper.mapAsList(orders, OrderDto.class))
            .build();
    }

    @Test
    public void getAllOrder() throws Exception {
        when(orderService.getAllOrder(page, size))
            .thenReturn(pageableOrderDto);

        mvc.perform(
            get(OrderApiPath.BASE_PATH)
                .param("page", "1")
                .param("size", "5")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.orders", hasSize(1)));

        verify(orderService).getAllOrder(page, size);
    }

    @Test
    public void getUserAllOrderTest() throws Exception {
        when(orderService.getAllOrderByUserEmail(userEmail, page, size))
                .thenReturn(pageableOrderDto);

        mvc.perform(
            get(OrderApiPath.BASE_PATH + OrderApiPath.GET_USER_ALL_ORDER)
                .sessionAttr("userId", userEmail)
                .param("page", "1")
                .param("size", "5")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.orders", hasSize(1)));

        verify(orderService).getAllOrderByUserEmail(userEmail, page, size);
    }

    @Test
    public void findOrderTest() throws Exception {
        when(orderService.getOrderByOrderId(orderId))
                .thenReturn(order);

        mvc.perform(
                get(OrderApiPath.BASE_PATH + OrderApiPath.FIND_ORDER, orderId)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(order));

        verify(orderService).getOrderByOrderId(orderId);
    }

    @Test
    public void acceptOrderTest() throws Exception {
        when(orderService.acceptOrder(orderId))
                .thenReturn(orderAccepted);

        mvc.perform(
                get(OrderApiPath.BASE_PATH + OrderApiPath.ACCEPT_ORDER, orderId)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(orderAccepted));

        verify(orderService).acceptOrder(orderId);
    }

    @Test
    public void rejectOrderTest() throws Exception {
        when(orderService.rejectOrder(orderId))
                .thenReturn(orderRejected);

        mvc.perform(
                get(OrderApiPath.BASE_PATH + OrderApiPath.REJECT_ORDER, orderId)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(orderRejected));

        verify(orderService).rejectOrder(orderId);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(orderService);
    }

}
