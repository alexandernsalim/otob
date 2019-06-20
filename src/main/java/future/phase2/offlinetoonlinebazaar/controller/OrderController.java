package future.phase2.offlinetoonlinebazaar.controller;

import future.phase2.offlinetoonlinebazaar.mapper.BeanMapper;
import future.phase2.offlinetoonlinebazaar.model.dto.OrderDto;
import future.phase2.offlinetoonlinebazaar.model.entity.Order;
import future.phase2.offlinetoonlinebazaar.model.response.Response;
import future.phase2.offlinetoonlinebazaar.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController extends GlobalController{

    @Autowired
    private OrderService orderService;

    @Autowired
    private BeanMapper mapper;
    @GetMapping("/{userEmail}")
    public Response<List<OrderDto>> getOrderItems(@PathVariable String userEmail){
        return toResponse(mapper.map(orderService.getUserAllOrder(userEmail), OrderDto.class));
    }
}
