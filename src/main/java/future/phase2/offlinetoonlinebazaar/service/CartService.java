package future.phase2.offlinetoonlinebazaar.service;

import future.phase2.offlinetoonlinebazaar.model.entity.Cart;
import future.phase2.offlinetoonlinebazaar.model.entity.CartItem;
import future.phase2.offlinetoonlinebazaar.model.entity.Product;

import java.util.List;

public interface CartService {

    Cart createUserCart(String userEmail);
    List<CartItem> getCartItems(String userEmail);
    Product addItemToCart(String userEmail, Long productId, Long qty);
    Product updateItemQty(String userEmail, Long productId, Long qty);
    boolean removeItemFromCart(String userEmail, Long productId);

}
