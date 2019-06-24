package future.phase2.offlinetoonlinebazaar.service;

import future.phase2.offlinetoonlinebazaar.model.dto.CheckoutDto;
import future.phase2.offlinetoonlinebazaar.model.entity.Cart;

public interface CartService {

    Cart createUserCart(String userEmail);
    Cart getUserCart(String userEmail);
    Cart addItemToCart(String userEmail, Long productId, int qty);
    Cart updateItemQty(String userEmail, Long productId, int qty);
    Cart removeItemFromCart(String userEmail, Long productId);
    Boolean removeUserCart(String userEmail);
    CheckoutDto checkout(String userEmail);

}
