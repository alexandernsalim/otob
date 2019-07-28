package otob.repository.impl;

import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import otob.entity.Cart;
import otob.entity.CartItem;
import otob.entity.Product;
import otob.enumerator.ErrorCode;
import otob.exception.CustomException;
import otob.repository.CartRepositoryCustom;

import java.util.List;

public class CartRepositoryCustomImpl implements CartRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Cart addToCart(String email, int qty, Product product) {
        Query query = new Query();
        Update update = new Update();

        Cart cart = checkItemExistsInCart(email, product.getProductId());

        if (cart == null) {
            checkStock(qty, product.getStock());

            query.addCriteria(Criteria.where("userEmail").is(email));
            update.push("cartItems", new BasicDBObject()
                    .append("productId", product.getProductId())
                    .append("productName", product.getName())
                    .append("productPrice", product.getOfferPrice())
                    .append("qty", qty)
            );
        } else {
            int currQty = getExistingItemQty(cart, product.getProductId());

            checkStock(currQty + qty, product.getStock());
            query.addCriteria(Criteria.where("userEmail").is(email).and("cartItems.productId").is(product.getProductId()));
            update.inc("cartItems.$.qty", qty);
        }

        return mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Cart.class);
    }

    @Override
    public Cart updateQty(String email, int qty, Product product) {
        Query query = new Query();
        Update update = new Update();

        Cart cart = checkItemExistsInCart(email, product.getProductId());

        if (cart != null) {
            checkStock(qty, product.getStock());
            query.addCriteria(Criteria.where("userEmail").is(email).and("cartItems.productId").is(product.getProductId()));
            update.set("cartItems.$.qty", qty);

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
                currStock = item.getQty();
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
