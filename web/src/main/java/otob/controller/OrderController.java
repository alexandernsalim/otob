package otob.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import otob.dto.OrderDto;
import otob.mapper.BeanMapper;
import otob.response.Response;
import otob.service.api.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController extends GlobalController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BeanMapper mapper;

    @GetMapping
    public Response<List<OrderDto>> getUserAllOrder(@RequestParam String userEmail){
        return toResponse(mapper.mapAsList(orderService.getUserAllOrder(userEmail), OrderDto.class));
    }

    @GetMapping("/{ordId}")
    public Response<OrderDto> findOrder(@PathVariable String ordId){
        return toResponse(mapper.map(orderService.findOrder(ordId), OrderDto.class));
    }

    @GetMapping("/acc/{ordId}")
    public Response<OrderDto> acceptOrder(@PathVariable String ordId){
        return toResponse(mapper.map(orderService.acceptOrder(ordId), OrderDto.class));
    }

    @GetMapping("/rej/{ordId}")
    public Response<OrderDto> rejectOrder(@PathVariable String ordId){
        return toResponse(mapper.map(orderService.rejectOrder(ordId), OrderDto.class));
    }

}
