package otob.repository.impl;

import com.mongodb.BasicDBObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import otob.model.entity.Cart;
import otob.model.entity.CartItem;
import otob.model.entity.Product;
import otob.model.enumerator.ErrorCode;
import otob.model.exception.CustomException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class CartRepositoryCustomImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private CartRepositoryCustomImpl cartRepositoryCustomImpl;

    private String userEmail;

    private Product product;
    private Cart cart;
    private Query query;
    private Query checkItemQuery;
    private Update update;

    @Before
    public void setUp() {
        initMocks(this);

        userEmail = "test@mail.com";

        product = Product.builder()
                .productId("B-1323")
                .name("Asus")
                .offerPrice(5000000)
                .stock(2)
                .build();

        CartItem cartItem = CartItem.builder()
                .productId(product.getProductId())
                .cartItemName(product.getName())
                .cartItemQty(1)
                .cartItemOfferPrice(product.getOfferPrice())
                .build();

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);

        cart = Cart.builder()
                .userEmail(userEmail)
                .cartItems(cartItems)
                .build();

        query = new Query();
        checkItemQuery = new Query();
        update = new Update();

    }

    @Test
    public void addToCartItemNotExistYetTest() {
        checkItemQuery.addCriteria(Criteria.where("userEmail").is(userEmail).and("cartItems.productId").is(product.getProductId()));
        query.addCriteria(Criteria.where("userEmail").is(userEmail));
        update.push("cartItems", new BasicDBObject()
                .append("productId", product.getProductId())
                .append("name", product.getName())
                .append("offerPrice", product.getOfferPrice())
                .append("cartItemQty", 1)
        );

        when(mongoTemplate.findOne(eq(checkItemQuery), eq(Cart.class)))
                .thenReturn(null);
        when(mongoTemplate.findAndModify(eq(query), eq(update), any(), eq(Cart.class)))
                .thenReturn(cart);

        Cart result = cartRepositoryCustomImpl.addToCart(userEmail, 1, product);

        verify(mongoTemplate).findOne(eq(checkItemQuery), eq(Cart.class));
        verify(mongoTemplate).findAndModify(eq(query), eq(update), any(), eq(Cart.class));
        assertEquals(cart, result);
    }

    @Test
    public void addToCartItemExistTest() {
        checkItemQuery.addCriteria(Criteria.where("userEmail").is(userEmail).and("cartItems.productId").is(product.getProductId()));
        query.addCriteria(Criteria.where("userEmail").is(userEmail).and("cartItems.productId").is(product.getProductId()));
        update.inc("cartItems.$.cartItemQty", 1);

        when(mongoTemplate.findOne(eq(checkItemQuery), eq(Cart.class)))
                .thenReturn(cart);
        when(mongoTemplate.findAndModify(eq(query), eq(update), any(), eq(Cart.class)))
                .thenReturn(cart);

        Cart result = cartRepositoryCustomImpl.addToCart(userEmail, 1, product);

        verify(mongoTemplate).findOne(eq(checkItemQuery), eq(Cart.class));
        verify(mongoTemplate).findAndModify(eq(query), eq(update), any(), eq(Cart.class));
        assertEquals(cart, result);
    }

    @Test
    public void updateQtyTest() {
        checkItemQuery.addCriteria(Criteria.where("userEmail").is(userEmail).and("cartItems.productId").is(product.getProductId()));
        query.addCriteria(Criteria.where("userEmail").is(userEmail).and("cartItems.productId").is(product.getProductId()));
        update.set("cartItems.$.cartItemQty", 1);

        when(mongoTemplate.findOne(eq(checkItemQuery), eq(Cart.class)))
                .thenReturn(cart);
        when(mongoTemplate.findAndModify(eq(query), eq(update), any(), eq(Cart.class)))
                .thenReturn(cart);

        Cart result = cartRepositoryCustomImpl.updateQty(userEmail, 1, product);

        verify(mongoTemplate).findOne(eq(checkItemQuery), eq(Cart.class));
        verify(mongoTemplate).findAndModify(eq(query), eq(update), any(), eq(Cart.class));
        assertEquals(cart, result);
    }

    @Test
    public void updateQtyEmptyTest() {
        checkItemQuery.addCriteria(Criteria.where("userEmail").is(userEmail).and("cartItems.productId").is(product.getProductId()));

        when(mongoTemplate.findOne(eq(checkItemQuery), eq(Cart.class)))
                .thenReturn(null);

        try {
            cartRepositoryCustomImpl.updateQty(userEmail, 1, product);
        } catch (CustomException ex) {
            verify(mongoTemplate).findOne(eq(checkItemQuery), eq(Cart.class));
            assertEquals(ErrorCode.NOT_FOUND.getMessage(), ex.getMessage());
        }
    }

    @Test
    public void removeFromCartTest() {
        checkItemQuery.addCriteria(Criteria.where("userEmail").is(userEmail).and("cartItems.productId").is(product.getProductId()));
        query.addCriteria(Criteria.where("userEmail").is(userEmail).and("cartItems.productId").is(product.getProductId()));
        update.pull("cartItems", new BasicDBObject()
                .append("productId", product.getProductId())
        );

        when(mongoTemplate.findOne(eq(checkItemQuery), eq(Cart.class)))
                .thenReturn(cart);
        when(mongoTemplate.findAndModify(eq(query), eq(update), any(), eq(Cart.class)))
                .thenReturn(cart);

        Cart result = cartRepositoryCustomImpl.removeFromCart(userEmail, product.getProductId());

        verify(mongoTemplate).findOne(eq(checkItemQuery), eq(Cart.class));
        verify(mongoTemplate).findAndModify(eq(query), eq(update), any(), eq(Cart.class));
        assertEquals(cart, result);
    }

    @Test
    public void remvoeFromCartEmptyTest() {
        checkItemQuery.addCriteria(Criteria.where("userEmail").is(userEmail).and("cartItems.productId").is(product.getProductId()));

        when(mongoTemplate.findOne(eq(checkItemQuery), eq(Cart.class)))
                .thenReturn(null);

        try {
            cartRepositoryCustomImpl.removeFromCart(userEmail, product.getProductId());
        } catch (CustomException ex) {
            verify(mongoTemplate).findOne(eq(checkItemQuery), eq(Cart.class));
            assertEquals(ErrorCode.NOT_FOUND.getMessage(), ex.getMessage());
        }
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(mongoTemplate);
    }

}
