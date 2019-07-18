package otob.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import otob.dto.OrderDto;
import otob.enumerator.ErrorCode;
import otob.exception.CustomException;
import otob.mapper.BeanMapper;
import otob.response.Response;
import otob.service.api.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController extends GlobalController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BeanMapper mapper;

    @GetMapping("/all")
    public Response<List<OrderDto>> getAllOrder(HttpServletRequest request){
        if(!isAuthorized(request)){
            throw new CustomException(
                ErrorCode.UNAUTHORIZED.getCode(),
                ErrorCode.UNAUTHORIZED.getMessage()
            );
        }

        return toResponse(mapper.mapAsList(orderService.getAllOrder(), OrderDto.class));
    }

    @GetMapping("/user/all")
    public Response<List<OrderDto>> getUserAllOrder(HttpServletRequest request){
        if(!isAuthorized(request)){
            throw new CustomException(
                    ErrorCode.UNAUTHORIZED.getCode(),
                    ErrorCode.UNAUTHORIZED.getMessage()
            );
        }

        HttpSession session = request.getSession();

        return toResponse(mapper.mapAsList(orderService.getUserAllOrder(session.getAttribute("userId").toString()), OrderDto.class));
    }

    @GetMapping("/{orderId}")
    public Response<OrderDto> findOrder(@PathVariable String orderId){
        return toResponse(mapper.map(orderService.findOrder(orderId), OrderDto.class));
    }

    @GetMapping("/{orderId}/accept")
    public Response<OrderDto> acceptOrder(@PathVariable String orderId){
        return toResponse(mapper.map(orderService.acceptOrder(orderId), OrderDto.class));
    }

    @GetMapping("/{orderId}/reject")
    public Response<OrderDto> rejectOrder(@PathVariable String orderId){
        return toResponse(mapper.map(orderService.rejectOrder(orderId), OrderDto.class));
    }

}
