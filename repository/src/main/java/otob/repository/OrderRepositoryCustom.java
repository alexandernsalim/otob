package otob.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import otob.model.entity.Order;
import otob.model.filter.ExportFilter;
import otob.model.filter.OrderFilter;

import java.util.List;

public interface OrderRepositoryCustom{

    List<Order> findOrderWithFilter(ExportFilter filter);
    Page<Order> findOrderWithFilter(OrderFilter filter, Pageable pageable);

}
