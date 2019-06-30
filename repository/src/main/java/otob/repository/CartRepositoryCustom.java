package otob.repository;

import otob.entity.Cart;

public interface CartRepositoryCustom {
    Cart addToCart(String email, int qty, Long productId);
    Cart updateQty(String email, int qty, Long productId);
    Cart removeFromCart(String email, Long productId);
}
