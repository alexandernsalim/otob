package otob.service;

import otob.model.entity.Order;
import otob.web.model.PageableOrderDto;

import java.util.List;

public interface OrderService {

    PageableOrderDto getAllOrder(Integer page, Integer size);
    PageableOrderDto getAllOrderByUserEmail(String userEmail, Integer page, Integer size);
    Order getOrderByOrderId(String ordId);
    Order createOrder(Order order);
    Order acceptOrder(String ordId);
    Order rejectOrder(String ordId);

}
