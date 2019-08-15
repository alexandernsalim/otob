package otob.service;

import otob.model.entity.Order;
import otob.web.model.ExportFilterDto;
import otob.web.model.PageableOrderDto;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.util.List;

public interface OrderService {

    PageableOrderDto getAllOrder(Integer page, Integer size);
    PageableOrderDto getAllOrderByUserEmail(String userEmail, Integer page, Integer size);
    PageableOrderDto getAllOrderByOrderStatus(String status, Integer page, Integer size);
    Order getOrderByOrderId(String ordId);
    Order createOrder(Order order);
    Order acceptOrder(String ordId);
    Order rejectOrder(String ordId);
    ByteArrayInputStream exportOrder(HttpServletResponse response, ExportFilterDto filter);

}
