package future.phase2.offlinetoonlinebazaar.repository.impl;

import com.mongodb.BasicDBObject;
import future.phase2.offlinetoonlinebazaar.exception.CustomException;
import future.phase2.offlinetoonlinebazaar.model.entity.Cart;
import future.phase2.offlinetoonlinebazaar.model.entity.CartItem;
import future.phase2.offlinetoonlinebazaar.model.entity.Product;
import future.phase2.offlinetoonlinebazaar.model.enumerator.ErrorCode;
import future.phase2.offlinetoonlinebazaar.repository.CartRepositoryCustom;
import future.phase2.offlinetoonlinebazaar.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

public class CartRepositoryCustomImpl implements CartRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ProductService productService;

    @Override
    public Cart addToCart(String email, int qty, Long productId) {
        Query query = new Query();
        Update update = new Update();

        Cart cart = checkItemExistsInCart(email, productId);
        Product product = productService.getProductById(productId);

        if(cart == null){
            query.addCriteria(Criteria.where("userEmail").is(email));
            update.push("cartItems", new BasicDBObject()
                .append("productId", productId)
                .append("productName", product.getName())
                .append("productPrice", product.getOfferPrice())
                .append("qty", qty)
            );
        }else{
            int currQty = getExistingItemQty(cart, productId);

            this.checkStock(currQty + qty, product.getStock());
            query.addCriteria(Criteria.where("userEmail").is(email).and("cartItems.productId").is(productId));
            update.inc("cartItems.$.qty", qty);
        }

        return mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Cart.class);
    }

    @Override
    public Cart updateQty(String email, int qty, Long productId) {
        Query query = new Query();
        Update update = new Update();

        Cart cart = checkItemExistsInCart(email, productId);
        Product product = productService.getProductById(productId);

        if(cart != null){
            checkStock(qty, product.getStock());
            query.addCriteria(Criteria.where("userEmail").is(email).and("cartItems.productId").is(productId));
            update.set("cartItems.$.qty", qty);

            return mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Cart.class);
        }else{
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

        if(cart != null){
            query.addCriteria(Criteria.where("userEmail").is(email).and("cartItems.productId").is(productId));
            update.pull("cartItems", new BasicDBObject()
                .append("productId", productId)
            );

            return mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Cart.class);
        }else{
            throw new CustomException(
                ErrorCode.NOT_FOUND.getCode(),
                ErrorCode.NOT_FOUND.getMessage()
            );
        }
    }

    //Private Method
    private Cart checkItemExistsInCart(String email, Long productId){
        Query query = new Query();
        query.addCriteria(Criteria.where("userEmail").is(email).and("cartItems.productId").is(productId));

        return mongoTemplate.findOne(query, Cart.class);
    }

    private int getExistingItemQty(Cart cart, Long productId){
        List<CartItem> cartItems = cart.getCartItems();
        int currStock = 0;

        for(CartItem item : cartItems){
            if(item.getProductId().equals(productId)){
                currStock = item.getQty();
                break;
            }
        }

        return currStock;
    }

    private void checkStock(int qty, int stock){
        if(qty > stock){
            throw new CustomException(
                ErrorCode.STOCK_INSUFFICIENT.getCode(),
                ErrorCode.STOCK_INSUFFICIENT.getMessage()
            );
        }
    }

}
