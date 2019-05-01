package future.phase2.offlinetoonlinebazaar.service.Impl;

import future.phase2.offlinetoonlinebazaar.model.entity.Order;
import future.phase2.offlinetoonlinebazaar.repository.OrderRepository;
import future.phase2.offlinetoonlinebazaar.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }
}
