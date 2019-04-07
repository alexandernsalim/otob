package future.phase2.offlinetoonlinebazaar.service;

import future.phase2.offlinetoonlinebazaar.model.entity.Cart;
import future.phase2.offlinetoonlinebazaar.model.entity.CartItem;
import future.phase2.offlinetoonlinebazaar.model.entity.Product;

import java.util.List;

public interface CartService {

    List<CartItem> getCartItems(String userEmail);
    Product addItemToCart(String userEmail, Long productId, Long qty);
    Cart createUserCart(String userEmail);

}
