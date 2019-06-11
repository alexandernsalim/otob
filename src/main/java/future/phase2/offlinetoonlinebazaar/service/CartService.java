package future.phase2.offlinetoonlinebazaar.service;

import future.phase2.offlinetoonlinebazaar.model.entity.Cart;
import future.phase2.offlinetoonlinebazaar.model.entity.Order;

public interface CartService {

    Cart createUserCart(String userEmail);
    Cart getUserCart(String userEmail);
    Cart addItemToCart(String userEmail, Long productId, int qty);
    Cart updateItemQty(String userEmail, Long productId, int qty);
    Cart removeItemFromCart(String userEmail, Long productId);
    boolean removeUserCart(String userEmail);
    Order checkout(String userEmail);

}
