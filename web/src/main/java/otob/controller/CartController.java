package otob.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import otob.dto.CartDto;
import otob.dto.CheckoutDto;
import otob.entity.Cart;
import otob.entity.CartItem;
import otob.mapper.BeanMapper;
import otob.response.Response;
import otob.service.impl.CartService;

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

    @GetMapping("/checkout")
    public Response<CheckoutDto> checkout(Principal principal){
        return toResponse(cartService.checkout(principal.getName()));
    }

}
