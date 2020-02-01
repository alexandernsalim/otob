package otob.service;

import otob.model.entity.Cart;
import otob.model.entity.Order;

public interface CartService {

    Cart createUserCart(String userEmail);
    Cart getUserCart(String userEmail);
    Cart addItemToCart(String userEmail, String productId, int qty);
    Cart updateItemQty(String userEmail, String productId, int qty);
    Cart removeItemFromCart(String userEmail, String productId);
    Boolean removeUserCart(String userEmail);
    Order checkout(String userEmail);

}
