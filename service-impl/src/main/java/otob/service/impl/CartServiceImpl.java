package otob.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otob.model.constant.Status;
import otob.model.entity.Cart;
import otob.model.entity.CartItem;
import otob.model.entity.Order;
import otob.model.entity.Product;
import otob.model.enumerator.ErrorCode;
import otob.model.exception.CustomException;
import otob.model.exception.OutOfStockException;
import otob.repository.CartRepository;
import otob.service.CartService;
import otob.service.OrderService;
import otob.service.ProductService;
import otob.service.UserService;
import otob.util.generator.IdGenerator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private IdGenerator idGenerator;

    private static Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Override
    public Cart createUserCart(String userEmail) {
        Cart cart = Cart.builder()
                .userEmail(userEmail)
                .build();

        return cartRepository.save(cart);
    }

    @Override
    public Cart getUserCart(String userEmail) {
        if (!userService.checkUser(userEmail)) {
            throw new CustomException(
                    ErrorCode.USER_NOT_FOUND.getCode(),
                    ErrorCode.USER_NOT_FOUND.getMessage()
            );
        }

        return cartRepository.findByUserEmail(userEmail);
    }

    @Override
    public Cart addItemToCart(String userEmail, Long productId, int qty) {
        if (!userService.checkUser(userEmail)) {
            throw new CustomException(
                ErrorCode.USER_NOT_FOUND.getCode(),
                ErrorCode.USER_NOT_FOUND.getMessage()
            );
        } else if (!checkUserCartExistence(userEmail)) {
            logger.info("User cart not found, create new cart");
            createUserCart(userEmail);
        }

        Product product = productService.getProductById(productId);

        return cartRepository.addToCart(userEmail, qty, product);
    }

    @Override
    public Cart updateItemQty(String userEmail, Long productId, int qty) {
        if (!userService.checkUser(userEmail)) {
            throw new CustomException(
                    ErrorCode.USER_NOT_FOUND.getCode(),
                    ErrorCode.USER_NOT_FOUND.getMessage()
            );
        }

        Product product = productService.getProductById(productId);

        return cartRepository.updateQty(userEmail, qty, product);
    }

    @Override
    public Cart removeItemFromCart(String userEmail, Long productId) {
        if (!userService.checkUser(userEmail)) {
            throw new CustomException(
                    ErrorCode.USER_NOT_FOUND.getCode(),
                    ErrorCode.USER_NOT_FOUND.getMessage()
            );
        }

        return cartRepository.removeFromCart(userEmail, productId);
    }

    @Override
    public Order checkout(String userEmail) {
        Cart cart = getUserCart(userEmail);
        List<CartItem> cartItems = cart.getCartItems();
        String orderId;
        int totItem = 0;
        long totPrice = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String ordDate = dateFormat.format(new Date());
        List<String> outOfStockProducts = new ArrayList<>();

        if (cartItems.isEmpty()) {
            throw new CustomException(
                ErrorCode.BAD_REQUEST.getCode(),
                ErrorCode.BAD_REQUEST.getMessage()
            );
        }

        for (CartItem item : cartItems) {
            Product product = productService.getProductById(item.getProductId());
            int itemQty = item.getQty();
            int productStock = product.getStock();

            if (itemQty > productStock) {
                outOfStockProducts.add(product.getName());
            }
        }

        if(!outOfStockProducts.isEmpty()) {
            throw new OutOfStockException(
                ErrorCode.SOME_PRODUCTS_INVALID.getCode(),
                ErrorCode.SOME_PRODUCTS_INVALID.getMessage(),
                outOfStockProducts
            );
        }

        for (CartItem item : cartItems) {
            Product product = productService.getProductById(item.getProductId());
            double itemPrice = item.getOfferPrice();
            int itemQty = item.getQty();
            int productStock = product.getStock();

            totPrice += itemPrice * itemQty;
            totItem++;
            product.setStock(productStock - itemQty);
            productService.updateProductById(product.getProductId(), product);
            removeItemFromCart(userEmail, product.getProductId());
        }

        try {
            orderId = idGenerator.generateOrderId(ordDate);
        } catch (Exception e) {
            throw new CustomException(
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                ErrorCode.INTERNAL_SERVER_ERROR.getMessage()
            );
        }

        Order order = Order.builder()
                .ordId(orderId)
                .userEmail(userEmail)
                .ordDate(ordDate)
                .ordItems(cartItems)
                .totItem(totItem)
                .totPrice(totPrice)
                .ordStatus(Status.ORD_WAIT)
                .build();

        return orderService.createOrder(order);
    }

    @Override
    public Boolean removeUserCart(String userEmail) {
        return (cartRepository.deleteByUserEmail(userEmail) == 1) ? Boolean.TRUE : Boolean.FALSE;
    }

    //Private Method
    private Boolean checkUserCartExistence(String userEmail) {
        return cartRepository.existsByUserEmail(userEmail);
    }

}
