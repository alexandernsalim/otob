package otob.service;

import otob.model.entity.Order;
import otob.model.filter.ExportFilter;
import otob.web.model.PageableOrderDto;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;

public interface OrderService {

    PageableOrderDto getAllOrder(Integer page, Integer size);
    PageableOrderDto getAllOrderByUserEmail(String userEmail, Integer page, Integer size);
    PageableOrderDto getAllOrderByFilter(String date, String status, Integer page, Integer size);
    Order getOrderByOrderId(String ordId);
    Order createOrder(Order order);
    Order acceptOrder(String ordId);
    Order rejectOrder(String ordId);
    ByteArrayInputStream exportOrder(String year, String month);

}
