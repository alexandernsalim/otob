package otob.repository.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import otob.model.entity.Cart;
import otob.model.entity.CartItem;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CartRepositoryCustomImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private CartRepositoryCustomImpl cartRepositoryCustomImpl;

    private String email;
    private Cart cart;

    @Before
    public void setUp() {
        initMocks(this);

        email = "user@mail.com";

        CartItem item = CartItem.builder()
                .productId(1L)
                .productName("Asus")
                .productPrice(4500000)
                .qty(1)
                .build();

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(item);

        cart = Cart.builder()
                .userEmail(email)
                .cartItems(cartItems)
                .build();

    }

    @Test
    public void addToCartNullTest() {
        Query query = new Query();
        query.addCriteria(Criteria.where("userEmail").is(email).and("cartItems.productId").is(1L));

        when(mongoTemplate.findOne(query, Cart.class))
            .thenReturn(cart);


    }


    @After
    public void tearDown() {
        verifyNoMoreInteractions(mongoTemplate);
    }

}
