package otob.repository;

import otob.model.entity.Cart;
import otob.model.entity.Product;

public interface CartRepositoryCustom {

    Cart addToCart(String email, int qty, Product product);
    Cart updateQty(String email, int qty, Product productId);
    Cart removeFromCart(String email, Long productId);

}
