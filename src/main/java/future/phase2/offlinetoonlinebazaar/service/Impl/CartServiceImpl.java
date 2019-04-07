package future.phase2.offlinetoonlinebazaar.service.Impl;

import com.mongodb.*;
import future.phase2.offlinetoonlinebazaar.exception.EmailExistsException;
import future.phase2.offlinetoonlinebazaar.exception.ResourceNotFoundException;
import future.phase2.offlinetoonlinebazaar.model.entity.Cart;
import future.phase2.offlinetoonlinebazaar.model.entity.CartItem;
import future.phase2.offlinetoonlinebazaar.model.entity.Product;
import future.phase2.offlinetoonlinebazaar.model.enumerator.ErrorCode;
import future.phase2.offlinetoonlinebazaar.repository.CartRepository;
import future.phase2.offlinetoonlinebazaar.service.CartService;
import future.phase2.offlinetoonlinebazaar.service.ProductService;
import future.phase2.offlinetoonlinebazaar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

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
    public Product addItemToCart(String userEmail, Long productId, Long qty) {
        if(!userService.checkUser(userEmail)){
            throw new ResourceNotFoundException(
                    ErrorCode.NOT_FOUND.getCode(),
                    ErrorCode.NOT_FOUND.getMessage()
            );
        }

        Product product = productService.getProductById(productId);

        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB database =   mongoClient.getDB("offline-to-online");
        DBCollection collection = database.getCollection("cart");
        DBObject findQuery = new BasicDBObject("userEmail", userEmail);
        DBObject newItem = new BasicDBObject("cartItems",
                new BasicDBObject("productId", productId)
                .append("productName", product.getName())
                .append("productPrice", product.getOfferPrice())
                .append("qty", qty)
        );
        DBObject updateQuery = new BasicDBObject("$push", newItem);

        collection.findAndModify(findQuery, updateQuery);

        return product;
    }

    @Override
    public Cart createUserCart(String userEmail){
        Cart cart = new Cart(userEmail);

        return cartRepository.save(cart);
    }

    private Cart getUserCart(String userEmail){
        Cart cart = cartRepository.findByUserEmail(userEmail);

        return cart;
    }

}
