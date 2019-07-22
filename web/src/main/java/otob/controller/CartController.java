package otob.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import otob.constant.path.CartApiPath;
import otob.dto.CartDto;
import otob.dto.CheckoutDto;
import otob.entity.Cart;
import otob.entity.CartItem;
import otob.mapper.BeanMapper;
import otob.response.Response;
import otob.service.api.CartService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
        HttpSession session = request.getSession();

        return toResponse(mapper.map(
                cartService.getUserCart(session.getAttribute("userId").toString()),
                CartDto.class)
        );
    }

    @PostMapping(CartApiPath.ADD_ITEM)
    public Response<Cart> addItemToCart(HttpServletRequest request,
                                        @PathVariable Long productId,
                                        @PathVariable int qty) {
        HttpSession session = request.getSession();

        return toResponse(mapper.map(
                cartService.addItemToCart(session.getAttribute("userId").toString(), productId, qty),
                CartDto.class)
        );
    }

    @PutMapping(CartApiPath.UPDATE_ITEM_QTY)
    public Response<Cart> updateItemQty(HttpServletRequest request,
                                        @PathVariable Long productId,
                                        @PathVariable int qty) {
        HttpSession session = request.getSession();

        return toResponse(mapper.map(
                cartService.updateItemQty(session.getAttribute("userId").toString(), productId, qty),
                CartDto.class)
        );
    }

    @DeleteMapping(CartApiPath.REMOVE_ITEM)
    public Response<Cart> removeItemFromCart(HttpServletRequest request,
                                             @PathVariable Long productId) {
        HttpSession session = request.getSession();

        return toResponse(mapper.map(
                cartService.removeItemFromCart(session.getAttribute("userId").toString(), productId),
                CartDto.class)
        );
    }

    @GetMapping(CartApiPath.CHECKOUT)
    public Response<CheckoutDto> checkout(HttpServletRequest request) {
        HttpSession session = request.getSession();

        return toResponse(cartService.checkout(session.getAttribute("userId").toString()));
    }

}
