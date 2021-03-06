package otob.repository.impl;

import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import otob.model.entity.Cart;
import otob.model.entity.CartItem;
import otob.model.entity.Product;
import otob.model.enumerator.ErrorCode;
import otob.model.exception.CustomException;
import otob.repository.CartRepositoryCustom;

import java.util.List;

@Repository
public class CartRepositoryCustomImpl implements CartRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Cart addToCart(String email, int qty, Product product) {
        Query query = new Query();
        Update update = new Update();

        Cart cart = checkItemExistsInCart(email, product.getProductId());

        if (cart == null) {
            checkStock(qty, product.getProductStock());

            query.addCriteria(Criteria.where("userEmail").is(email));
            update.push("cartItems", new BasicDBObject()
                    .append("productId", product.getProductId())
                    .append("cartItemName", product.getProductName())
                    .append("cartItemOfferPrice", product.getProductOfferPrice())
                    .append("cartItemQty", qty)
            );
        } else {
            int currQty = getExistingItemQty(cart, product.getProductId());

            checkStock(currQty + qty, product.getProductStock());
            query.addCriteria(Criteria.where("userEmail").is(email).and("cartItems.productId").is(product.getProductId()));
            update.inc("cartItems.$.cartItemQty", qty);
        }

        return mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Cart.class);
    }

    @Override
    public Cart updateQty(String email, int qty, Product product) {
        Query query = new Query();
        Update update = new Update();

        Cart cart = checkItemExistsInCart(email, product.getProductId());

        if (cart != null) {
            checkStock(qty, product.getProductStock());
            query.addCriteria(Criteria.where("userEmail").is(email).and("cartItems.productId").is(product.getProductId()));
            update.set("cartItems.$.cartItemQty", qty);

            return mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Cart.class);
        } else {
            throw new CustomException(
                    ErrorCode.NOT_FOUND.getCode(),
                    ErrorCode.NOT_FOUND.getMessage()
            );
        }
    }

    @Override
    public Cart removeFromCart(String email, Long productId) {
        Query query = new Query();
        Update update = new Update();

        Cart cart = checkItemExistsInCart(email, productId);

        if (cart != null) {
            query.addCriteria(Criteria.where("userEmail").is(email).and("cartItems.productId").is(productId));
            update.pull("cartItems", new BasicDBObject()
                    .append("productId", productId)
            );

            return mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Cart.class);
        } else {
            throw new CustomException(
                    ErrorCode.NOT_FOUND.getCode(),
                    ErrorCode.NOT_FOUND.getMessage()
            );
        }
    }

    //Private Method
    private Cart checkItemExistsInCart(String email, Long productId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userEmail").is(email).and("cartItems.productId").is(productId));

        return mongoTemplate.findOne(query, Cart.class);
    }

    private int getExistingItemQty(Cart cart, Long productId) {
        List<CartItem> cartItems = cart.getCartItems();
        int currStock = 0;

        for (CartItem item : cartItems) {
            if (item.getProductId().equals(productId)) {
                currStock = item.getCartItemQty();
                break;
            }
        }

        return currStock;
    }

    private void checkStock(int qty, int stock) {
        if (qty > stock) {
            throw new CustomException(
                ErrorCode.STOCK_INSUFFICIENT.getCode(),
                ErrorCode.STOCK_INSUFFICIENT.getMessage()
            );
        }
    }

}
