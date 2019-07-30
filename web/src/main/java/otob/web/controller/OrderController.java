package otob.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otob.model.constant.path.OrderApiPath;
import otob.web.model.OrderDto;
import otob.util.mapper.BeanMapper;
import otob.model.response.Response;
import otob.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(OrderApiPath.BASE_PATH)
public class OrderController extends GlobalController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BeanMapper mapper;

    @GetMapping
    public Response<List<OrderDto>> getAllOrder() {

        return toResponse(mapper.mapAsList(orderService.getAllOrder(), OrderDto.class));
    }

    @GetMapping(OrderApiPath.GET_USER_ALL_ORDER)
    public Response<List<OrderDto>> getUserAllOrder(HttpServletRequest request) {
        HttpSession session = request.getSession();

        return toResponse(mapper.mapAsList(orderService.getAllOrderByUserEmail(session.getAttribute("userId").toString()), OrderDto.class));
    }

    @GetMapping(OrderApiPath.FIND_ORDER)
    public Response<OrderDto> findOrder(@PathVariable String orderId) {

        return toResponse(mapper.map(orderService.getOrderByOrderId(orderId), OrderDto.class));
    }

    @GetMapping(OrderApiPath.ACCEPT_ORDER)
    public Response<OrderDto> acceptOrder(@PathVariable String orderId) {

        return toResponse(mapper.map(orderService.acceptOrder(orderId), OrderDto.class));
    }

    @GetMapping(OrderApiPath.REJECT_ORDER)
    public Response<OrderDto> rejectOrder(@PathVariable String orderId) {

        return toResponse(mapper.map(orderService.rejectOrder(orderId), OrderDto.class));
    }

}
