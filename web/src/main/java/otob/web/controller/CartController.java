package otob.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import otob.model.constant.path.CartApiPath;
import otob.model.entity.Cart;
import otob.model.entity.CartItem;
import otob.model.response.Response;
import otob.service.CartService;
import otob.util.mapper.BeanMapper;
import otob.web.model.CartDto;
import otob.web.model.OrderDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(CartApiPath.BASE_PATH)
public class CartController extends GlobalController {

    @Autowired
    private CartService cartService;

    @Autowired
    private BeanMapper mapper;

    @GetMapping
    public Response<List<CartItem>> getCartItems(HttpServletRequest request) {
        String email = request.getUserPrincipal().getName();

        return toResponse(mapper.map(cartService.getUserCart(email), CartDto.class));
    }

    @PostMapping(CartApiPath.ADD_OR_UPDATE_ITEM)
    public Response<Cart> addItemToCart(HttpServletRequest request,
                                        @PathVariable String productId,
                                        @PathVariable int qty) {
        String email = request.getUserPrincipal().getName();

        return toResponse(mapper.map(cartService.addItemToCart(email, productId, qty), CartDto.class));
    }

    @PutMapping(CartApiPath.ADD_OR_UPDATE_ITEM)
    public Response<Cart> updateItemQty(HttpServletRequest request,
                                        @PathVariable String productId,
                                        @PathVariable int qty) {
        String email = request.getUserPrincipal().getName();

        return toResponse(mapper.map(cartService.updateItemQty(email, productId, qty), CartDto.class));
    }

    @DeleteMapping(CartApiPath.REMOVE_ITEM)
    public Response<Cart> removeItemFromCart(HttpServletRequest request,
                                             @PathVariable String productId) {
        String email = request.getUserPrincipal().getName();

        return toResponse(mapper.map(cartService.removeItemFromCart(email, productId), CartDto.class));
    }

    @GetMapping(CartApiPath.CHECKOUT)
    public Response<OrderDto> checkout(HttpServletRequest request) {
        String email = request.getUserPrincipal().getName();

        return toResponse(mapper.map(cartService.checkout(email), OrderDto.class));
    }

}
