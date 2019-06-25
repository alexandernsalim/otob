package future.phase2.offlinetoonlinebazaar.service;

import future.phase2.offlinetoonlinebazaar.model.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> getUserAllOrder(String userEmail);
    Order findOrder(String ordId);
    Order createOrder(Order order);
    Order acceptOrder(String ordId);
    Order rejectOrder(String ordId);

}
