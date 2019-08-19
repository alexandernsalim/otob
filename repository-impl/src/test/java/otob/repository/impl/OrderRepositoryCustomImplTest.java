package otob.repository.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import otob.model.constant.Status;
import otob.model.entity.CartItem;
import otob.model.entity.Order;
import otob.model.filter.ExportFilter;
import otob.model.filter.OrderFilter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class OrderRepositoryCustomImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private OrderRepositoryCustomImpl orderRepositoryCustomImpl;

    private Integer page;
    private Integer size;
    private String month;
    private String year;
    private String orderDate;
    private String orderStatus;

    private Pageable pageable;
    private ExportFilter exportFilter;
    private OrderFilter orderFilter;
    private CartItem item;
    private List<CartItem> items;
    private Order order;
    private List<Order> orders;

    private ArgumentCaptor<Query> queryCaptor;

    @Before
    public void setUp() {
        initMocks(this);

        page = 0;
        size = 5;
        month = "08";
        year = "2019";
        orderDate = "2019/08/12";
        orderStatus = Status.ORD_WAIT;

        pageable = PageRequest.of(page, size);

        exportFilter = ExportFilter.builder()
                .month(month)
                .year(year)
                .build();

        orderFilter = OrderFilter.builder()
                .orderDate(orderDate)
                .orderStatus(orderStatus)
                .build();

        item = CartItem.builder()
                .productId(1L)
                .name("Asus")
                .offerPrice(5000000)
                .qty(1)
                .build();

        items = new ArrayList<>();
        items.add(item);

        order = Order.builder()
                .ordId("ORD12346578910")
                .userEmail("test@mail.com")
                .ordDate("2019/08/12 11:14")
                .ordItems(items)
                .totItem(1)
                .totPrice(5000000L)
                .ordStatus(Status.ORD_WAIT)
                .build();

        orders = new ArrayList<>();
        orders.add(order);

        queryCaptor = ArgumentCaptor.forClass(Query.class);
    }

    @Test
    public void findOrderWithExportFilterTest() {
        when(mongoTemplate.find(queryCaptor.capture(), any()))
            .thenReturn(new ArrayList<>(orders));

        List<Order> result = orderRepositoryCustomImpl.findOrderWithFilter(exportFilter);

        verify(mongoTemplate).find(queryCaptor.capture(), any());
        assertTrue(result.size() >= 1);
    }

    @Test
    public void findOrderWithExportFilterMonthEmptyTest() {
        exportFilter.setMonth("");

        when(mongoTemplate.find(queryCaptor.capture(), any()))
                .thenReturn(new ArrayList<>(orders));

        List<Order> result = orderRepositoryCustomImpl.findOrderWithFilter(exportFilter);

        verify(mongoTemplate).find(queryCaptor.capture(), any());
        assertTrue(result.size() >= 1);
    }

    @Test
    public void findOrderWithOrderFilterTest() {
        when(mongoTemplate.find(queryCaptor.capture(), any()))
            .thenReturn(new ArrayList<>(orders));

        Page<Order> result = orderRepositoryCustomImpl.findOrderWithFilter(orderFilter, pageable);

        verify(mongoTemplate).find(queryCaptor.capture(), any());
        assertTrue(result.getTotalPages() >= 1);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(mongoTemplate);
    }

}
