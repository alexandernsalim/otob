package otob.service.api;

import otob.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrder();
    List<Order> getUserAllOrder(String userEmail);
    Order findOrder(String ordId);
    Order createOrder(Order order);
    Order acceptOrder(String ordId);
    Order rejectOrder(String ordId);

}
