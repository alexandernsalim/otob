package otob.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otob.dto.CheckoutDto;
import otob.entity.Cart;
import otob.entity.CartItem;
import otob.entity.Order;
import otob.entity.Product;
import otob.enumerator.ErrorCode;
import otob.enumerator.Status;
import otob.exception.CustomException;
import otob.repository.CartRepository;
import otob.generator.IdGenerator;
import otob.service.api.CartService;
import otob.service.api.OrderService;
import otob.service.api.ProductService;
import otob.service.api.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Override
    public Cart createUserCart(String userEmail){
        Cart cart = new Cart(userEmail);

        return cartRepository.save(cart);
    }

    @Override
    public Cart getUserCart(String userEmail) {
        if(!userService.checkUser(userEmail)){
            throw new CustomException(
                ErrorCode.USER_NOT_FOUND.getCode(),
                ErrorCode.USER_NOT_FOUND.getMessage()
            );
        }

        return cartRepository.findByUserEmail(userEmail);
    }

    @Override
    public Cart addItemToCart(String userEmail, Long productId, int qty) {
        if(!userService.checkUser(userEmail)){
            throw new CustomException(
                ErrorCode.USER_NOT_FOUND.getCode(),
                ErrorCode.USER_NOT_FOUND.getMessage()
            );
        }else if(!checkUserCartExistence(userEmail)){
            createUserCart(userEmail);
        }

        Product product = productService.getProductById(productId);

        return cartRepository.addToCart(userEmail, qty, product);
    }

    @Override
    public Cart updateItemQty(String userEmail, Long productId, int qty) {
        if(!userService.checkUser(userEmail)){
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
        if(!userService.checkUser(userEmail)){
            throw new CustomException(
                ErrorCode.USER_NOT_FOUND.getCode(),
                ErrorCode.USER_NOT_FOUND.getMessage()
            );
        }

        return cartRepository.removeFromCart(userEmail, productId);
    }

    @Override
    public CheckoutDto checkout(String userEmail) {
        Cart cart = getUserCart(userEmail);
        List<CartItem> cartItems = cart.getCartItems();
        String ordId = "";
        int totItem = 0;
        long totPrice = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String ordDate = dateFormat.format(new Date());
        List<String> outOfStockProducts = new ArrayList<>();

        int cartItemsSize = cartItems.size();
        if(cartItemsSize == 0){
            throw new CustomException(
                ErrorCode.BAD_REQUEST.getCode(),
                ErrorCode.BAD_REQUEST.getMessage()
            );
        }

        int itemIdx = 0;
        for(int i = 0; i < cartItemsSize; i++){
            CartItem item = cartItems.get(itemIdx);
            Product product = productService.getProductById(item.getProductId());
            int itemQty = item.getQty();
            int productStock = product.getStock();

            if(itemQty > productStock){
                outOfStockProducts.add(product.getName());
                cartItems.remove(item);
                itemIdx--;
            }else{
                totPrice += item.getProductPrice() * itemQty;
                totItem++;
                product.setStock(productStock - itemQty);
                productService.updateProductById(product.getProductId(), product);
            }

            removeItemFromCart(userEmail, product.getProductId());
            itemIdx++;
        }

        try{
            ordId = idGenerator.generateOrderId(ordDate);
        } catch(Exception e) {
            throw new CustomException(
                    ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                    ErrorCode.INTERNAL_SERVER_ERROR.getMessage()
            );
        }

        Order order = Order.builder()
                .ordId(ordId)
                .userEmail(userEmail)
                .ordDate(ordDate)
                .ordItems(cartItems)
                .totItem(totItem)
                .totPrice(totPrice)
                .ordStatus(Status.WAIT.getStatus())
                .build();

        orderService.createOrder(order);

        CheckoutDto checkoutDet = CheckoutDto.builder()
                .order(order)
                .outOfStockProducts(outOfStockProducts)
                .build();

        return checkoutDet;
    }

    @Override
    public Boolean removeUserCart(String userEmail) {
        return (cartRepository.deleteByUserEmail(userEmail) == 1) ? Boolean.TRUE : Boolean.FALSE;
    }

    //Private Method
    private Boolean checkUserCartExistence(String userEmail){
        return cartRepository.existsByUserEmail(userEmail);
    }

}
