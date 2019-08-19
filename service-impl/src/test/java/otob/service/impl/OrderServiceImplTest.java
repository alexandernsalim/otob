package otob.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import otob.model.constant.Status;
import otob.model.entity.CartItem;
import otob.model.entity.Order;
import otob.model.entity.Product;
import otob.model.enumerator.ErrorCode;
import otob.model.exception.CustomException;
import otob.model.filter.ExportFilter;
import otob.model.filter.OrderFilter;
import otob.repository.OrderRepository;
import otob.service.ProductService;
import otob.service.UserService;
import otob.web.model.PageableOrderDto;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class OrderServiceImplTest {

    @Mock
    private ProductService productService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    private int page;
    private int size;
    private PageRequest pageable;
    private CartItem item1;
    private List<CartItem> items;
    private String orderId;
    private String userEmail;
    private Order order;
    private Order orderAccepted;
    private Order orderRejected;
    private List<Order> orders;
    private Product product;
    private Product productUpdated;
    private OrderFilter orderFilter;

    @Before
    public void setUp() {
        initMocks(this);

        page = 0;
        size = 5;

        pageable = PageRequest.of(page, size);

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

        product = Product.builder()
                .productId(1L)
                .name("Asus")
                .description("Laptop")
                .listPrice(7500000)
                .offerPrice(5000000)
                .stock(0)
                .build();

        productUpdated = Product.builder()
                .productId(1L)
                .name("Asus")
                .description("Laptop")
                .listPrice(7500000)
                .offerPrice(5000000)
                .stock(1)
                .build();

        orderFilter = OrderFilter.builder()
                .orderDate("2019/06/25")
                .orderStatus(Status.ORD_WAIT)
                .build();

    }

    @Test
    public void getOrderByOrderIdTest() {
        when(orderRepository.existsByOrdId(orderId)).thenReturn(true);
        when(orderRepository.findByOrdId(orderId))
                .thenReturn(order);

        Order result = orderServiceImpl.getOrderByOrderId(orderId);

        verify(orderRepository).existsByOrdId(orderId);
        verify(orderRepository).findByOrdId(orderId);
        assertEquals(order.getOrdId(), result.getOrdId());
        assertTrue(order.getOrdItems().size() == 1);
    }

    @Test
    public void getOrderByOrderIdNotExistsTest() {
        when(orderRepository.existsByOrdId(orderId)).thenReturn(false);

        try {
            orderServiceImpl.getOrderByOrderId(orderId);
        } catch (CustomException ex) {
            verify(orderRepository).existsByOrdId(orderId);
            assertTrue(ex.getMessage().equals(ErrorCode.ORDER_NOT_FOUND.getMessage()));
        }
    }

    @Test
    public void getAllOrderTest() {
        when(orderRepository.findAll(pageable))
            .thenReturn(new PageImpl<>(orders));

        PageableOrderDto result = orderServiceImpl.getAllOrder(page, size);

        verify(orderRepository).findAll(pageable);
        assertTrue(result.getOrders().size() >= 1);
    }

    @Test
    public void getAllOrderEmptyTest() {
        orders.clear();

        when(orderRepository.findAll(pageable))
            .thenReturn(new PageImpl<>(orders));

        try {
            orderServiceImpl.getAllOrder(page, size);
        } catch (CustomException ex) {
            verify(orderRepository).findAll(pageable);
            assertEquals(ErrorCode.ORDER_NOT_FOUND.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void getAllOrderByUserEmailTest() {
        when(userService.checkUser(userEmail)).thenReturn(true);
        when(orderRepository.findAllByUserEmail(userEmail, pageable))
                .thenReturn(new PageImpl<>(orders));

        PageableOrderDto result = orderServiceImpl.getAllOrderByUserEmail(userEmail, page, size);

        verify(userService).checkUser(userEmail);
        verify(orderRepository).findAllByUserEmail(userEmail, pageable);
        assertTrue(result.getOrders().size() >= 1);
    }

    @Test
    public void getAllOrderByUserEmailEmptyTest() {
        orders.clear();

        when(userService.checkUser(userEmail)).thenReturn(true);
        when(orderRepository.findAllByUserEmail(userEmail, pageable))
                .thenReturn(new PageImpl<>(orders));

        try {
            orderServiceImpl.getAllOrderByUserEmail(userEmail, page, size);
        } catch (CustomException ex) {
            verify(userService).checkUser(userEmail);
            verify(orderRepository).findAllByUserEmail(userEmail, pageable);
            assertEquals(ErrorCode.ORDER_NOT_FOUND.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void getAllOrderByUserEmailNotExistsTest() {
        when(userService.checkUser(userEmail)).thenReturn(false);

        try {
            orderServiceImpl.getAllOrderByUserEmail(userEmail, page, size);
        } catch (CustomException ex) {
            verify(userService).checkUser(userEmail);
            assertTrue(ex.getMessage().equals(ErrorCode.USER_NOT_FOUND.getMessage()));
        }
    }

    @Test
    public void getAllOrderByFilterTest() {
        when(orderRepository.findOrderWithFilter(orderFilter, pageable))
                .thenReturn(new PageImpl<>(orders));

        PageableOrderDto result = orderServiceImpl.getAllOrderByFilter("2019/06/25", Status.ORD_WAIT, page, size);

        verify(orderRepository).findOrderWithFilter(orderFilter, pageable);
        assertTrue(result.getOrders().size() >= 1);
    }

    @Test
    public void getAllOrderByFilterEmptyTest() {
        orders.clear();

        when(orderRepository.findOrderWithFilter(orderFilter, pageable))
                .thenReturn(new PageImpl<>(orders));

        try {
            orderServiceImpl.getAllOrderByFilter("2019/06/25", Status.ORD_WAIT, page, size);
        } catch (CustomException ex) {
            verify(orderRepository).findOrderWithFilter(orderFilter, pageable);
            assertEquals(ErrorCode.ORDER_NOT_FOUND.getMessage(), ex.getMessage());
        }
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
        when(orderRepository.existsByOrdId(orderId))
                .thenReturn(true);
        when(orderRepository.findByOrdId(orderId))
                .thenReturn(order);
        when(orderRepository.save(order))
                .thenReturn(order);

        Order result = orderServiceImpl.acceptOrder(orderId);

        verify(orderRepository).existsByOrdId(orderId);
        verify(orderRepository).findByOrdId(orderId);
        verify(orderRepository).save(order);
        assertTrue(result.getOrdStatus().equals(Status.ORD_ACCEPT));
    }

    @Test
    public void acceptOrderFailTest() {
        when(orderRepository.existsByOrdId(orderId))
                .thenReturn(false);

        try {
            orderServiceImpl.acceptOrder(orderId);
        } catch (CustomException ex) {
            verify(orderRepository).existsByOrdId(orderId);
            assertTrue(ex.getMessage().equals(ErrorCode.ORDER_NOT_FOUND.getMessage()));
        }
    }

    @Test
    public void rejectOrder() {
        when(orderRepository.existsByOrdId(orderId))
                .thenReturn(true);
        when(orderRepository.findByOrdId(orderId))
                .thenReturn(order);
        when(productService.getProductById(product.getProductId()))
                .thenReturn(product);
        when(productService.updateProductById(product.getProductId(), product))
                .thenReturn(productUpdated);
        when(orderRepository.save(orderRejected))
                .thenReturn(orderRejected);

        Order result = orderServiceImpl.rejectOrder(orderId);

        verify(orderRepository).existsByOrdId(orderId);
        verify(orderRepository).findByOrdId(orderId);
        verify(productService).getProductById(productUpdated.getProductId());
        verify(productService).updateProductById(product.getProductId(), product);
        verify(orderRepository).save(orderRejected);
        assertTrue(result.getOrdStatus().equals(Status.ORD_REJECT));
    }

    @Test
    public void rejectOrderFailNotFoundTest() {
        when(orderRepository.existsByOrdId(orderId))
                .thenReturn(false);

        try {
            orderServiceImpl.rejectOrder(orderId);
        } catch (CustomException ex) {
            verify(orderRepository).existsByOrdId(orderId);
            assertTrue(ex.getMessage().equals(ErrorCode.ORDER_NOT_FOUND.getMessage()));
        }
    }

    @Test
    public void rejectOrderFailOrderProcessedTest() {
        when(orderRepository.existsByOrdId(orderId))
                .thenReturn(true);
        when(orderRepository.findByOrdId(orderId))
                .thenReturn(orderAccepted);

        try {
            orderServiceImpl.rejectOrder(orderId);
        } catch (CustomException ex) {
            verify(orderRepository).existsByOrdId(orderId);
            verify(orderRepository).findByOrdId(orderId);
            assertTrue(ex.getMessage().equals(ErrorCode.ORDER_PROCESSED.getMessage()));
        }

    }

    @Test
    public void exportOrderTest() {
        String month = "08";
        String year = "2019";

        ExportFilter filter = ExportFilter.builder()
                .month(month)
                .year(year)
                .build();

        when(orderRepository.findOrderWithFilter(filter))
            .thenReturn(orders);

        orderServiceImpl.exportOrder(year, month);

        verify(orderRepository).findOrderWithFilter(filter);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(productService);
        verifyNoMoreInteractions(orderRepository);
        verifyNoMoreInteractions(userService);
    }

}
