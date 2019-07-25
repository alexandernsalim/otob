package otob.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import otob.constant.Status;
import otob.entity.CartItem;
import otob.entity.Order;
import otob.enumerator.ErrorCode;
import otob.exception.CustomException;
import otob.repository.OrderRepository;
import otob.repository.ProductRepository;
import otob.service.api.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class OrderServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    private CartItem item1;
    private List<CartItem> items;
    private String orderId;
    private String userEmail;
    private Order order;
    private Order orderProcessed;
    private List<Order> orders;

    @Before
    public void setUp() {
        initMocks(this);

        item1 = CartItem.builder()
                .productId(1L)
                .productName("Asus")
                .productPrice(5000000)
                .qty(1)
                .build();

        items = new ArrayList<>();
        items.add(item1);

        orderId = "ORD1561436040000";
        userEmail = "alexandernsalim@gmail.com";
        order = Order.builder()
                .orderId(orderId)
                .userEmail(userEmail)
                .ordDate("2019/06/25 11:14")
                .ordItems(items)
                .totItem(1)
                .totPrice(5000000L)
                .ordStatus(Status.ORD_WAIT)
                .build();

        orderProcessed = Order.builder()
                .orderId(orderId)
                .userEmail(userEmail)
                .ordDate("2019/06/25 11:14")
                .ordItems(items)
                .totItem(1)
                .totPrice(5000000L)
                .ordStatus(Status.ORD_ACCEPT)
                .build();

        orders = new ArrayList<>();
        orders.add(order);

    }

    @Test
    public void getOrderByOrderIdTest() {
        when(orderRepository.existsByOrderId(orderId)).thenReturn(true);
        when(orderRepository.findByOrderId(orderId))
                .thenReturn(order);

        Order result = orderServiceImpl.getOrderByOrderId(orderId);

        verify(orderRepository).existsByOrderId(orderId);
        verify(orderRepository).findByOrderId(orderId);
        assertEquals(order.getOrderId(), result.getOrderId());
        assertTrue(order.getOrdItems().size() == 1);
    }

    @Test
    public void getOrderByOrderIdNotExistsTest() {
        when(orderRepository.existsByOrderId(orderId)).thenReturn(false);

        try {
            orderServiceImpl.getOrderByOrderId(orderId);
        } catch (CustomException ex) {
            verify(orderRepository).existsByOrderId(orderId);
            assertTrue(ex.getMessage().equals(ErrorCode.ORDER_NOT_FOUND.getMessage()));
        }
    }

    @Test
    public void getAllOrderByUserEmailTest() {
        when(userService.checkUser(userEmail)).thenReturn(true);
        when(orderRepository.findAllByUserEmail(userEmail))
                .thenReturn(orders);

        List<Order> userOrders = orderServiceImpl.getAllOrderByUserEmail(userEmail);

        verify(userService).checkUser(userEmail);
        verify(orderRepository).findAllByUserEmail(userEmail);
        assertTrue(userOrders.size() >= 1);
    }

    @Test
    public void getAllOrderByUserEmailNotExistsTest() {
        when(userService.checkUser(userEmail)).thenReturn(false);

        try {
            orderServiceImpl.getAllOrderByUserEmail(userEmail);
        } catch (CustomException ex) {
            verify(userService).checkUser(userEmail);
            assertTrue(ex.getMessage().equals(ErrorCode.USER_NOT_FOUND.getMessage()));
        }
    }

    @Test
    public void getAllOrderTest() {
        when(orderRepository.findAll())
                .thenReturn(orders);

        List<Order> orders = orderServiceImpl.getAllOrder();

        verify(orderRepository).findAll();
        assertTrue(orders.size() >= 1);
    }

    @Test
    public void createOrderTest() {
        when(orderRepository.save(order))
                .thenReturn(order);

        Order result = orderServiceImpl.createOrder(order);

        verify(orderRepository).save(order);
        assertTrue(result.getOrdItems().size() >= 1);
        assertTrue(result.getUserEmail().equals(userEmail));
    }

    @Test
    public void acceptOrderTest() {
        when(orderRepository.existsByOrderId(orderId))
                .thenReturn(true);
        when(orderRepository.findByOrderId(orderId))
                .thenReturn(order);
        when(orderRepository.save(order))
                .thenReturn(order);

        Order result = orderServiceImpl.acceptOrder(orderId);

        verify(orderRepository).existsByOrderId(orderId);
        verify(orderRepository).findByOrderId(orderId);
        verify(orderRepository).save(order);
        assertTrue(result.getOrdStatus().equals(Status.ORD_ACCEPT));
    }

    @Test
    public void acceptOrderFailTest() {
        when(orderRepository.existsByOrderId(orderId))
                .thenReturn(false);

        try {
            orderServiceImpl.acceptOrder(orderId);
        } catch (CustomException ex) {
            verify(orderRepository).existsByOrderId(orderId);
            assertTrue(ex.getMessage().equals(ErrorCode.ORDER_NOT_FOUND.getMessage()));
        }
    }

//    @Test
//    public void rejectOrder() {
//        when(orderRepository.existsByOrderId(orderId))
//                .thenReturn(true);
//        when(orderRepository.findByOrderId(orderId))
//                .thenReturn(order);
//
//
//    }

    @Test
    public void rejectOrderFailNotFoundTest() {
        when(orderRepository.existsByOrderId(orderId))
                .thenReturn(false);

        try {
            orderServiceImpl.rejectOrder(orderId);
        } catch (CustomException ex) {
            verify(orderRepository).existsByOrderId(orderId);
            assertTrue(ex.getMessage().equals(ErrorCode.ORDER_NOT_FOUND.getMessage()));
        }
    }

    @Test
    public void rejectOrderFailOrderProcessedTest() {
        when(orderRepository.existsByOrderId(orderId))
                .thenReturn(true);
        when(orderRepository.findByOrderId(orderId))
                .thenReturn(orderProcessed);

        try {

        } catch (CustomException ex) {
            verify(orderRepository).existsByOrderId(orderId);
            verify(orderRepository).findByOrderId(orderId);
            assertTrue(ex.getMessage().equals(ErrorCode.ORDER_PROCESSED.getMessage()));
        }

    }

    @After
    public void teardown() {
        verifyNoMoreInteractions(productRepository);
        verifyNoMoreInteractions(orderRepository);
        verifyNoMoreInteractions(userService);
    }

}
