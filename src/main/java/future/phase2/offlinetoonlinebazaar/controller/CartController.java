package future.phase2.offlinetoonlinebazaar.controller;

import future.phase2.offlinetoonlinebazaar.mapper.BeanMapper;
import future.phase2.offlinetoonlinebazaar.model.dto.CartDto;
import future.phase2.offlinetoonlinebazaar.model.entity.Cart;
import future.phase2.offlinetoonlinebazaar.model.entity.CartItem;
import future.phase2.offlinetoonlinebazaar.model.response.Response;
import future.phase2.offlinetoonlinebazaar.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController extends GlobalController{

    @Autowired
    private CartService cartService;

    @Autowired
    private BeanMapper mapper;

    @GetMapping("/{userEmail}")
    public Response<List<Cart>> getCartItems(@PathVariable @Valid String userEmail){
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
