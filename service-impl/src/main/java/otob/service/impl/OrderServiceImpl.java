package otob.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otob.constant.Status;
import otob.entity.CartItem;
import otob.entity.Order;
import otob.entity.Product;
import otob.enumerator.ErrorCode;
import otob.exception.CustomException;
import otob.repository.OrderRepository;
import otob.service.api.OrderService;
import otob.service.api.ProductService;
import otob.service.api.UserService;

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
    public List<Order> getAllOrder() {
        return orderRepository.findAll();
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
    public List<Order> getAllOrderByUserEmail(String userEmail) {
        if (!userService.checkUser(userEmail)) {
            throw new CustomException(
                    ErrorCode.USER_NOT_FOUND.getCode(),
                    ErrorCode.USER_NOT_FOUND.getMessage()
            );
        }

        return orderRepository.findAllByUserEmail(userEmail);
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
                    ErrorCode.ORDER_NOT_FOUND.getMessage()
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
}
