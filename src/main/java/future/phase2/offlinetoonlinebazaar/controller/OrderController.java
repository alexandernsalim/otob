package future.phase2.offlinetoonlinebazaar.controller;

import future.phase2.offlinetoonlinebazaar.mapper.BeanMapper;
import future.phase2.offlinetoonlinebazaar.model.dto.OrderDto;
import future.phase2.offlinetoonlinebazaar.model.response.Response;
import future.phase2.offlinetoonlinebazaar.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController extends GlobalController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BeanMapper mapper;

    @GetMapping
    public Response<List<OrderDto>> getUserOrder(@RequestParam String usrEmail){
        return toResponse(mapper.mapAsList(orderService.getUserOrder(usrEmail), OrderDto.class));
    }

}
