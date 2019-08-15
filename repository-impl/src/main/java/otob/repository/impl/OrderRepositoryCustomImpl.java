package otob.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import otob.model.entity.Order;
import otob.repository.OrderRepositoryCustom;

import java.util.List;

@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Order> findOrderWithFilter(String year) {
        Query query = new Query();

        query.addCriteria(Criteria.where("ordDate").regex("^" + year + "/.+"));

        return mongoTemplate.find(query, Order.class);
    }

    @Override
    public List<Order> findOrderWithFilter(String year, String month) {
        Query query = new Query();

        query.addCriteria(Criteria.where("ordDate").regex("^" + year + "/" + month + "/.+"));

        return mongoTemplate.find(query, Order.class);
    }

}
