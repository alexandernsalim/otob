package otob.service;

import otob.model.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrder();
    List<Order> getAllOrderByUserEmail(String userEmail);
    Order getOrderByOrderId(String ordId);
    Order createOrder(Order order);
    Order acceptOrder(String ordId);
    Order rejectOrder(String ordId);

}
