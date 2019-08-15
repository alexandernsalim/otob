package otob.repository;

import otob.model.entity.Order;

import java.util.List;

public interface OrderRepositoryCustom{

    List<Order> findOrderWithFilter(String year);
    List<Order> findOrderWithFilter(String year, String month);

}
