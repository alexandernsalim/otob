package future.phase2.offlinetoonlinebazaar.service.Impl;

import future.phase2.offlinetoonlinebazaar.exception.CustomException;
import future.phase2.offlinetoonlinebazaar.model.entity.CartItem;
import future.phase2.offlinetoonlinebazaar.model.entity.Order;
import future.phase2.offlinetoonlinebazaar.model.entity.Product;
import future.phase2.offlinetoonlinebazaar.model.enumerator.ErrorCode;
import future.phase2.offlinetoonlinebazaar.model.enumerator.Status;
import future.phase2.offlinetoonlinebazaar.repository.OrderRepository;
import future.phase2.offlinetoonlinebazaar.service.OrderService;
import future.phase2.offlinetoonlinebazaar.service.ProductService;
import future.phase2.offlinetoonlinebazaar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Order findOrder(String ordId) {
        return orderRepository.findByOrdId(ordId);
    }

    @Override
    public List<Order> getUserAllOrder(String userEmail) {
        if(!userService.checkUser(userEmail)){
            throw new CustomException(
                    ErrorCode.NOT_FOUND.getCode(),
                    ErrorCode.NOT_FOUND.getMessage()
            );
        }

        return orderRepository.findAllByUserEmail(userEmail);
    }

    @Override
    public Order acceptOrder(String ordId) {
        if(!orderRepository.existsByOrdId(ordId)){
            throw new CustomException(
                ErrorCode.NOT_FOUND.getCode(),
                ErrorCode.NOT_FOUND.getMessage()
            );
        }

        Order order = orderRepository.findByOrdId(ordId);
        order.setOrdStatus(Status.ACCEPT.getStatus());

        return orderRepository.save(order);
    }

    @Override
    public Order rejectOrder(String ordId) {
        if(!orderRepository.existsByOrdId(ordId)){
            throw new CustomException(
                    ErrorCode.NOT_FOUND.getCode(),
                    ErrorCode.NOT_FOUND.getMessage()
            );
        }

        Order order = orderRepository.findByOrdId(ordId);

        if(!order.getOrdStatus().equals("Waiting")){
            throw new CustomException(
                ErrorCode.ORDER_PROCESSED.getCode(),
                ErrorCode.ORDER_PROCESSED.getMessage()
            );
        }

        order.setOrdStatus(Status.REJECT.getStatus());

        List<CartItem> ordItems = order.getOrdItems();

        for(CartItem item : ordItems){
            Product product = productService.getProductById(item.getProductId());
            int currStock = product.getStock();

            product.setStock(currStock + item.getQty());

            productService.updateProductById(product.getProductId(), product);
        }

        return orderRepository.save(order);
    }
}
