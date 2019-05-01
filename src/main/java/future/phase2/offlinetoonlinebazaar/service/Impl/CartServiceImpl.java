package future.phase2.offlinetoonlinebazaar.service.Impl;

import com.mongodb.*;
import future.phase2.offlinetoonlinebazaar.exception.ResourceNotFoundException;
import future.phase2.offlinetoonlinebazaar.exception.StockInsufficientException;
import future.phase2.offlinetoonlinebazaar.model.entity.Cart;
import future.phase2.offlinetoonlinebazaar.model.entity.CartItem;
import future.phase2.offlinetoonlinebazaar.model.entity.Order;
import future.phase2.offlinetoonlinebazaar.model.entity.Product;
import future.phase2.offlinetoonlinebazaar.model.enumerator.ErrorCode;
import future.phase2.offlinetoonlinebazaar.model.enumerator.Status;
import future.phase2.offlinetoonlinebazaar.repository.CartRepository;
import future.phase2.offlinetoonlinebazaar.service.CartService;
import future.phase2.offlinetoonlinebazaar.service.OrderService;
import future.phase2.offlinetoonlinebazaar.service.ProductService;
import future.phase2.offlinetoonlinebazaar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Override
    public Cart createUserCart(String userEmail){
        Cart cart = new Cart(userEmail);

        return cartRepository.save(cart);
    }

    @Override
    public List<CartItem> getCartItems(String userEmail) {
        if(!userService.checkUser(userEmail)){
            throw new ResourceNotFoundException(
                    ErrorCode.NOT_FOUND.getCode(),
                    ErrorCode.NOT_FOUND.getMessage()
            );
        }

        Cart cart = cartRepository.findByUserEmail(userEmail);
        List<CartItem> cartItems = cart.getCartItems();

        return cartItems;
    }

    @Override
    public Product addItemToCart(String userEmail, Long productId, int qty) {
        if(!userService.checkUser(userEmail)){
            throw new ResourceNotFoundException(
                    ErrorCode.NOT_FOUND.getCode(),
                    ErrorCode.NOT_FOUND.getMessage()
            );
        }

        Product product = productService.getProductById(productId);

        this.checkStock(qty, product.getStock());

        DBCollection collection = getCollection();
        DBObject find = new BasicDBObject("userEmail", userEmail)
                .append("cartItems.productId", productId);
        DBCursor cursor = collection.find(find);

        if(cursor.length() == 1){
            DBObject updateItem = new BasicDBObject();

            updateItem.put("$inc", new BasicDBObject("cartItems.$.qty", qty));

            collection.findAndModify(find, updateItem);
        }else{
            DBObject findQuery = new BasicDBObject("userEmail", userEmail);
            DBObject newItem = new BasicDBObject("cartItems",
                    new BasicDBObject("productId", productId)
                            .append("productName", product.getName())
                            .append("productPrice", product.getOfferPrice())
                            .append("qty", qty)
            );
            DBObject updateQuery = new BasicDBObject("$push", newItem);

            collection.findAndModify(findQuery, updateQuery);
        }

        return product;
    }

    @Override
    public Product updateItemQty(String userEmail, Long productId, int qty) {
        if(!userService.checkUser(userEmail)){
            throw new ResourceNotFoundException(
                ErrorCode.NOT_FOUND.getCode(),
                ErrorCode.NOT_FOUND.getMessage()
            );
        }

        Product product = productService.getProductById(productId);

        this.checkStock(qty, product.getStock());

        DBCollection collection = getCollection();
        DBObject find = new BasicDBObject("userEmail", userEmail)
                .append("cartItems.productId", productId);
        DBCursor cursor = collection.find(find);

        if(cursor.length() == 1){
            DBObject newQty = new BasicDBObject();

            newQty.put("$set", new BasicDBObject("cartItems.$.qty", qty));

            collection.findAndModify(find, newQty);

            return product;
        }else{
            throw new ResourceNotFoundException(
                    ErrorCode.NOT_FOUND.getCode(),
                    ErrorCode.NOT_FOUND.getMessage()
            );
        }
    }

    @Override
    public boolean removeItemFromCart(String userEmail, Long productId) {
        if(!userService.checkUser(userEmail)){
            throw new ResourceNotFoundException(
                    ErrorCode.NOT_FOUND.getCode(),
                    ErrorCode.NOT_FOUND.getMessage()
            );
        }

        DBCollection collection = getCollection();
        DBObject find = new BasicDBObject("userEmail", userEmail)
                .append("cartItems.productId", productId);
        DBCursor cursor = collection.find(find);

        if(cursor.length() == 1) {
            DBObject updateQuery = new BasicDBObject("$pull",
                    new BasicDBObject("cartItems", new BasicDBObject("productId", productId)));

            collection.findAndModify(find, updateQuery);

            return true;
        }else{
            throw new ResourceNotFoundException(
                    ErrorCode.NOT_FOUND.getCode(),
                    ErrorCode.NOT_FOUND.getMessage()
            );
        }
    }

    private Cart getUserCart(String userEmail){
        return cartRepository.findByUserEmail(userEmail);
    }

    private DBCollection getCollection(){
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB database = mongoClient.getDB("offline-to-online");
        DBCollection collection = database.getCollection("cart");

        return collection;
    }

    private void checkStock(int qty, int stock){
        if(qty > stock){
            throw new StockInsufficientException(
                    ErrorCode.STOCK_INSUFFICIENT.getCode(),
                    ErrorCode.STOCK_INSUFFICIENT.getMessage()
            );
        }
    }

    @Override
    public Order checkout(String userEmail) {
        Cart cart = getUserCart(userEmail);
        List<CartItem> cartItems = cart.getCartItems();
        int totItem = 0;
        long totPrice = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String ordDate = dateFormat.format(new Date());

        for(CartItem item : cartItems){
            Product product = productService.getProductById(item.getProductId());
            int itemQty = item.getQty();
            int productStock = product.getStock();

            if(itemQty > productStock){
                //Buat respon error disini
            }

            totPrice += item.getProductPrice();
            totItem++;
        }

        Order newOrder = Order.builder()
                              .usrEmail(userEmail)
                              .ordDate(ordDate)
                              .ordItems(cartItems)
                              .totItem(totItem)
                              .totPrice(totPrice)
                              .ordStatus(Status.WAIT.getStatus())
                              .build();

        return orderService.createOrder(newOrder);
    }

    //(Increase qty) db.cart.update({"userEmail": "twinzeno21@gmail.com", "cartItems.productId": NumberLong(1)}, {$inc: {"cartItems.$.qty": NumberLong(5)}})
    //(Decrease qty) db.cart.update({"userEmail": "twinzeno21@gmail.com", "cartItems.productId": NumberLong(1)}, {$inc: {"cartItems.$.qty": NumberLong(-5)}})
    //(Set qty) db.cart.update({"userEmail": "twinzeno21@gmail.com", "cartItems.productId": NumberLong(1)}, {$set: {"cartItems.$.qty": NumberLong(5)}})
    //(Remove item) db.cart.update({"userEmail": "twinzeno21@gmail.com"}, {$pull: {"cartItems": {"productId": NumberLong(1)}}})
}
