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
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(CartApiPath.BASE_PATH)
public class CartController extends GlobalController {

    @Autowired
    private CartService cartService;

    @Autowired
    private BeanMapper mapper;

    private HttpSession session;

    @GetMapping
    public Response<List<CartItem>> getCartItems(HttpServletRequest request) {
        session = request.getSession(true);

        return toResponse(mapper.map(
                cartService.getUserCart(session.getAttribute("userId").toString()),
                CartDto.class)
        );
    }

    @PostMapping(CartApiPath.ADD_OR_UPDATE_ITEM)
    public Response<Cart> addItemToCart(HttpServletRequest request,
                                        @PathVariable Long productId,
                                        @PathVariable int qty) {
        session = request.getSession(true);

        return toResponse(mapper.map(
                cartService.addItemToCart(session.getAttribute("userId").toString(), productId, qty),
                CartDto.class)
        );
    }

    @PutMapping(CartApiPath.ADD_OR_UPDATE_ITEM)
    public Response<Cart> updateItemQty(HttpServletRequest request,
                                        @PathVariable Long productId,
                                        @PathVariable int qty) {
        session = request.getSession(true);

        return toResponse(mapper.map(
                cartService.updateItemQty(session.getAttribute("userId").toString(), productId, qty),
                CartDto.class)
        );
    }

    @DeleteMapping(CartApiPath.REMOVE_ITEM)
    public Response<Cart> removeItemFromCart(HttpServletRequest request,
                                             @PathVariable Long productId) {
        session = request.getSession(true);

        return toResponse(mapper.map(
                cartService.removeItemFromCart(session.getAttribute("userId").toString(), productId),
                CartDto.class)
        );
    }

    @GetMapping(CartApiPath.CHECKOUT)
    public Response<OrderDto> checkout(HttpServletRequest request) {
        session = request.getSession(true);
        String email = session.getAttribute("userId").toString();

        return toResponse(mapper.map(cartService.checkout(email), OrderDto.class));
    }

}
