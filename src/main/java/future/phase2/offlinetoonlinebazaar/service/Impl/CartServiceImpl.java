package future.phase2.offlinetoonlinebazaar.service.Impl;

import future.phase2.offlinetoonlinebazaar.exception.CustomException;
import future.phase2.offlinetoonlinebazaar.exception.StockInsufficientException;
import future.phase2.offlinetoonlinebazaar.generator.IdGenerator;
import future.phase2.offlinetoonlinebazaar.model.dto.CheckoutDto;
import future.phase2.offlinetoonlinebazaar.model.entity.Cart;
import future.phase2.offlinetoonlinebazaar.model.entity.CartItem;
import future.phase2.offlinetoonlinebazaar.model.entity.Order;
import future.phase2.offlinetoonlinebazaar.model.entity.Product;
import future.phase2.offlinetoonlinebazaar.model.enumerator.ErrorCode;
import future.phase2.offlinetoonlinebazaar.model.enumerator.Status;
import future.phase2.offlinetoonlinebazaar.repository.CartRepository;
import future.phase2.offlinetoonlinebazaar.service.CartService;
import future.phase2.offlinetoonlinebazaar.service.OrderService;
import future.phase2.offlinetoonlinebazaar.service.ProductService;
import future.phase2.offlinetoonlinebazaar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        this.checkStock(qty, product.getStock());

        return cartRepository.addToCart(userEmail, qty, productId);
    }

    @Override
    public Cart updateItemQty(String userEmail, Long productId, int qty) {
        if(!userService.checkUser(userEmail)){
            throw new CustomException(
                ErrorCode.USER_NOT_FOUND.getCode(),
                ErrorCode.USER_NOT_FOUND.getMessage()
            );
        }

        return cartRepository.updateQty(userEmail, qty, productId);
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
    private void checkStock(int qty, int stock){
        if(qty > stock){
            throw new StockInsufficientException(
                ErrorCode.STOCK_INSUFFICIENT.getCode(),
                ErrorCode.STOCK_INSUFFICIENT.getMessage()
            );
        }
    }

    private Boolean checkUserCartExistence(String userEmail){
        return cartRepository.existsByUserEmail(userEmail);
    }

}
