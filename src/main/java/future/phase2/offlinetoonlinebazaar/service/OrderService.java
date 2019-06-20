package future.phase2.offlinetoonlinebazaar.service;

import future.phase2.offlinetoonlinebazaar.model.entity.Order;

public interface OrderService {

    Order createOrder(Order order);

    Order getUserAllOrder(String userEmail);

}
