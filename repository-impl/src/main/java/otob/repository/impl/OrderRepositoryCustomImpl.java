package otob.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import otob.model.entity.Order;
import otob.model.filter.ExportFilter;
import otob.model.filter.OrderFilter;
import otob.repository.OrderRepositoryCustom;

import java.util.List;

@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Order> findOrderWithFilter(ExportFilter filter) {
        String month = filter.getMonth();
        String year = filter.getYear();
        Query query = new Query();

        if(month.isEmpty()){
            query.addCriteria(Criteria.where("ordDate").regex("^" + year + "/.+"));
        }else {
            query.addCriteria(Criteria.where("ordDate").regex("^" + year + "/" + month + "/.+"));
        }

        return mongoTemplate.find(query, Order.class);
    }

    @Override
    public Page<Order> findOrderWithFilter(OrderFilter filter, Pageable pageable) {
        String status = filter.getOrderStatus();
        String date = filter.getOrderDate();
        Query query = new Query();

        query.with(pageable);

        if(!status.isEmpty()) {
            query.addCriteria(Criteria.where("ordStatus").is(status));
        }

        if(!date.isEmpty()) {
            query.addCriteria(Criteria.where("ordDate").regex(date + ".+"));
        }

        List<Order> orders = mongoTemplate.find(query, Order.class);

        return PageableExecutionUtils.getPage(
                orders,
                pageable,
                () -> mongoTemplate.count(query, Order.class));
    }

}
