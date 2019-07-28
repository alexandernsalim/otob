package otob.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import otob.constant.OrderApiPath;
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
@RequestMapping(OrderApiPath.BASE_PATH)
public class OrderController extends GlobalController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BeanMapper mapper;

    @GetMapping(OrderApiPath.GET_ALL_ORDER)
    public Response<List<OrderDto>> getAllOrder(HttpServletRequest request){
        if(!isAuthenticated(request) || !isAuthorized(request)){
            throw new CustomException(
                ErrorCode.UNAUTHORIZED.getCode(),
                ErrorCode.UNAUTHORIZED.getMessage()
            );
        }

        return toResponse(mapper.mapAsList(orderService.getAllOrder(), OrderDto.class));
    }

    @GetMapping(OrderApiPath.GET_USER_ALL_ORDER)
    public Response<List<OrderDto>> getUserAllOrder(HttpServletRequest request){
        if(!isAuthenticated(request) || !isAuthorized(request)){
            throw new CustomException(
                ErrorCode.UNAUTHORIZED.getCode(),
                ErrorCode.UNAUTHORIZED.getMessage()
            );
        }

        HttpSession session = request.getSession();

        return toResponse(mapper.mapAsList(orderService.getUserAllOrder(session.getAttribute("userId").toString()), OrderDto.class));
    }

    @GetMapping(OrderApiPath.FIND_ORDER)
    public Response<OrderDto> findOrder(HttpServletRequest request, @PathVariable String orderId){
        if(!isAuthenticated(request) || !isAuthorized(request)){
            throw new CustomException(
                ErrorCode.UNAUTHORIZED.getCode(),
                ErrorCode.UNAUTHORIZED.getMessage()
            );
        }

        return toResponse(mapper.map(orderService.findOrder(orderId), OrderDto.class));
    }

    @GetMapping(OrderApiPath.ACCEPT_ORDER)
    public Response<OrderDto> acceptOrder(HttpServletRequest request, @PathVariable String orderId){
        if(!isAuthenticated(request) || !isAuthorized(request)){
            throw new CustomException(
                ErrorCode.UNAUTHORIZED.getCode(),
                ErrorCode.UNAUTHORIZED.getMessage()
            );
        }

        return toResponse(mapper.map(orderService.acceptOrder(orderId), OrderDto.class));
    }

    @GetMapping(OrderApiPath.REJECT_ORDER)
    public Response<OrderDto> rejectOrder(HttpServletRequest request, @PathVariable String orderId){
        if(!isAuthenticated(request) || !isAuthorized(request)){
            throw new CustomException(
                ErrorCode.UNAUTHORIZED.getCode(),
                ErrorCode.UNAUTHORIZED.getMessage()
            );
        }

        return toResponse(mapper.map(orderService.rejectOrder(orderId), OrderDto.class));
    }

}
