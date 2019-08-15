package otob.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import otob.model.constant.path.OrderApiPath;
import otob.model.response.Response;
import otob.service.OrderService;
import otob.util.mapper.BeanMapper;
import otob.web.model.ExportFilterDto;
import otob.web.model.OrderDto;
import otob.web.model.PageableOrderDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;

@RestController
@RequestMapping(OrderApiPath.BASE_PATH)
public class OrderController extends GlobalController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BeanMapper mapper;

    private HttpSession session;

    @GetMapping
    public Response<PageableOrderDto> getAllOrder(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size
    ) {
        page = (page == null) ? 0 : page-1;
        if(size == null) size = 5;

        return toResponse(orderService.getAllOrder(page, size));
    }

    @GetMapping(OrderApiPath.GET_USER_ALL_ORDER)
    public Response<PageableOrderDto> getUserAllOrder(
        HttpServletRequest request,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size
    ) {
        session = request.getSession(true);
        String email = session.getAttribute("userId").toString();
        page = (page == null) ? 0 : page-1;
        if(size == null) size = 5;

        return toResponse(orderService.getAllOrderByUserEmail(email, page, size));
    }

    @GetMapping(OrderApiPath.GET_ORDER_BY_STATUS)
    public Response<PageableOrderDto> getAllOrderByOrderStatus(
        @PathVariable String orderStatus,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size
    ) {
        page = (page == null) ? 0 : page-1;
        if(size == null) size = 5;

        return toResponse(orderService.getAllOrderByOrderStatus(orderStatus, page, size));
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

    @PostMapping(OrderApiPath.EXPORT_ORDER_HISTORY)
    public ResponseEntity<InputStreamResource> exportOrder(HttpServletResponse response,
                                                           @RequestBody ExportFilterDto filter) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=orders.xlsx");

        ByteArrayInputStream in = orderService.exportOrder(response, filter);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

}
