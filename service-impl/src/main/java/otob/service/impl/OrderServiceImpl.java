package otob.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import otob.model.constant.Status;
import otob.model.entity.CartItem;
import otob.model.entity.Order;
import otob.model.entity.Product;
import otob.model.enumerator.ErrorCode;
import otob.model.exception.CustomException;
import otob.service.OrderService;
import otob.service.ProductService;
import otob.service.UserService;
import otob.repository.OrderRepository;
import otob.util.mapper.BeanMapper;
import otob.web.model.OrderDto;
import otob.web.model.PageableOrderDto;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public PageableOrderDto getAllOrder(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> pages = orderRepository.findAll(pageable);
        List<Order> orders = pages.getContent();

        return generateResult(pages, orders);
    }

    @Override
    public Order getOrderByOrderId(String orderId) {
        if(!orderRepository.existsByOrderId(orderId)){
            throw new CustomException(
                ErrorCode.ORDER_NOT_FOUND.getCode(),
                ErrorCode.ORDER_NOT_FOUND.getMessage()
            );
        }

        return orderRepository.findByOrderId(orderId);
    }

    @Override
    public PageableOrderDto getAllOrderByUserEmail(String userEmail, Integer page, Integer size) {
        if (!userService.checkUser(userEmail)) {
            throw new CustomException(
                    ErrorCode.USER_NOT_FOUND.getCode(),
                    ErrorCode.USER_NOT_FOUND.getMessage()
            );
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Order> pages = orderRepository.findAllByUserEmail(userEmail, pageable);
        List<Order> orders = pages.getContent();

        return generateResult(pages, orders);
    }

    @Override
    public PageableOrderDto getAllOrderByOrderStatus(String status, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> pages = orderRepository.findAllByOrdStatus(status, pageable);
        List<Order> orders = pages.getContent();

        return generateResult(pages, orders);
    }

    @Override
    public Order acceptOrder(String ordId) {
        if (!orderRepository.existsByOrderId(ordId)) {
            throw new CustomException(
                    ErrorCode.ORDER_NOT_FOUND.getCode(),
                    ErrorCode.ORDER_NOT_FOUND.getMessage()
            );
        }

        Order order = orderRepository.findByOrderId(ordId);
        order.setOrdStatus(Status.ORD_ACCEPT);

        return orderRepository.save(order);
    }

    @Override
    public Order rejectOrder(String ordId) {
        if (!orderRepository.existsByOrderId(ordId)) {
            throw new CustomException(
                    ErrorCode.ORDER_NOT_FOUND.getCode(),
                    ErrorCode   .ORDER_NOT_FOUND.getMessage()
            );
        }

        Order order = orderRepository.findByOrderId(ordId);

        if (!order.getOrdStatus().equals("Waiting")) {
            throw new CustomException(
                    ErrorCode.ORDER_PROCESSED.getCode(),
                    ErrorCode.ORDER_PROCESSED.getMessage()
            );
        }

        order.setOrdStatus(Status.ORD_REJECT);

        List<CartItem> ordItems = order.getOrdItems();

        for (CartItem item : ordItems) {
            Product product = productService.getProductById(item.getProductId());
            int currStock = product.getStock();

            product.setStock(currStock + item.getQty());

            productService.updateProductById(product.getProductId(), product);
        }

        return orderRepository.save(order);
    }

    private void checkEmptiness(List<Order> orders) throws CustomException {
        if(orders.isEmpty()) {
            throw new CustomException(
                ErrorCode.ORDER_NOT_FOUND.getCode(),
                ErrorCode.ORDER_NOT_FOUND.getMessage()
            );
        }
    }

    private PageableOrderDto generateResult(Page<Order> pages, List<Order> orders) {
        checkEmptiness(orders);

        List<OrderDto> ordersResult = BeanMapper.mapAsList(orders, OrderDto.class);
        PageableOrderDto result = PageableOrderDto.builder()
                .totalPage(pages.getTotalPages())
                .orders(ordersResult)
                .build();

        return result;
    }

}
