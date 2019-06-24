package future.phase2.offlinetoonlinebazaar.service;

import future.phase2.offlinetoonlinebazaar.model.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> getUserOrder(String usrEmail);
    Order createOrder(Order order);

}
