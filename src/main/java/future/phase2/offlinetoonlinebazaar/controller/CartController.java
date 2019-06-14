package future.phase2.offlinetoonlinebazaar.controller;

import future.phase2.offlinetoonlinebazaar.mapper.BeanMapper;
import future.phase2.offlinetoonlinebazaar.model.dto.CartDto;
import future.phase2.offlinetoonlinebazaar.model.entity.Cart;
import future.phase2.offlinetoonlinebazaar.model.entity.CartItem;
import future.phase2.offlinetoonlinebazaar.model.response.Response;
import future.phase2.offlinetoonlinebazaar.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController extends GlobalController{

    @Autowired
    private CartService cartService;

    @Autowired
    private BeanMapper mapper;

    @GetMapping
    public Response<List<CartItem>> getCartItems(Principal principal){
        return toResponse(mapper.map(cartService.getUserCart(principal.getName()), CartDto.class));
    }

    @PostMapping("/add/{productId}/{qty}")
    public Response<Cart> addItemToCart(Principal principal,
                                        @PathVariable Long productId,
                                        @PathVariable int qty){
        return toResponse(mapper.map(cartService.addItemToCart(principal.getName(), productId, qty), CartDto.class));
    }

    @PutMapping("/update/{productId}/{qty}")
    public Response<Cart> updateItemQty (Principal principal,
                                         @PathVariable Long productId,
                                         @PathVariable int qty) {
        return toResponse(mapper.map(cartService.updateItemQty(principal.getName(), productId, qty), CartDto.class));
    }

    @DeleteMapping("/remove/{productId}")
    public Response<Cart> removeItemFromCart(Principal principal,
                                             @PathVariable Long productId){
        return toResponse(mapper.map(cartService.removeItemFromCart(principal.getName(), productId), CartDto.class));
    }

    @DeleteMapping("/remove/user/{userEmail}")
    public Response<Boolean> removeUserCart(@PathVariable String userEmail){
        return toResponse(cartService.removeUserCart(userEmail));
    }

    @GetMapping("/{userEmail}")
    public Response<List<CartItem>> getCartItems(@PathVariable String userEmail){
        return toResponse(mapper.map(cartService.getUserCart(userEmail), CartDto.class));
    }

    @PostMapping("/{userEmail}/add/{productId}/{qty}")
    public Response<Cart> addItemToCart(@PathVariable String userEmail,
                                        @PathVariable Long productId,
                                        @PathVariable int qty){
        return toResponse(mapper.map(cartService.addItemToCart(userEmail, productId, qty), CartDto.class));
    }

    @PutMapping("/{userEmail}/update/{productId}/{qty}")
    public Response<Cart> updateItemQty (@PathVariable String userEmail,
                                         @PathVariable Long productId,
                                         @PathVariable int qty) {
        return toResponse(mapper.map(cartService.updateItemQty(userEmail, productId, qty), CartDto.class));
    }

    @DeleteMapping("/{userEmail}/remove/{productId}")
    public Response<Cart> removeItemFromCart(@PathVariable String userEmail,
                                             @PathVariable Long productId){
        return toResponse(mapper.map(cartService.removeItemFromCart(userEmail, productId), CartDto.class));
    }

}
