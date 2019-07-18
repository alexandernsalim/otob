package otob.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import otob.dto.CartDto;
import otob.dto.CheckoutDto;
import otob.entity.Cart;
import otob.entity.CartItem;
import otob.enumerator.ErrorCode;
import otob.exception.CustomException;
import otob.mapper.BeanMapper;
import otob.response.Response;
import otob.service.api.CartService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController extends GlobalController {

    @Autowired
    private CartService cartService;

    @Autowired
    private BeanMapper mapper;

    @GetMapping
    public Response<List<CartItem>> getCartItems(HttpServletRequest request) {
        if (!isAuthenticated(request)) {
            throw new CustomException(
                    ErrorCode.UNAUTHORIZED.getCode(),
                    ErrorCode.UNAUTHORIZED.getMessage()
            );
        }

        HttpSession session = request.getSession();

        return toResponse(mapper.map(
            cartService.getUserCart(session.getAttribute("userId").toString()),
            CartDto.class)
        );
    }

    @PostMapping("/add/{productId}/{qty}")
    public Response<Cart> addItemToCart(HttpServletRequest request,
                                        @PathVariable Long productId,
                                        @PathVariable int qty) {
        if (!isAuthenticated(request)) {
            throw new CustomException(
                    ErrorCode.UNAUTHORIZED.getCode(),
                    ErrorCode.UNAUTHORIZED.getMessage()
            );
        }

        HttpSession session = request.getSession();

        return toResponse(mapper.map(
            cartService.addItemToCart(session.getAttribute("userId").toString(), productId, qty),
            CartDto.class)
        );
    }

    @PutMapping("/update/{productId}/{qty}")
    public Response<Cart> updateItemQty(HttpServletRequest request,
                                        @PathVariable Long productId,
                                        @PathVariable int qty) {
        if (!isAuthenticated(request)) {
            throw new CustomException(
                    ErrorCode.UNAUTHORIZED.getCode(),
                    ErrorCode.UNAUTHORIZED.getMessage()
            );
        }

        HttpSession session = request.getSession();

        return toResponse(mapper.map(
            cartService.updateItemQty(session.getAttribute("userId").toString(), productId, qty),
            CartDto.class)
        );
    }

    @DeleteMapping("/remove/{productId}")
    public Response<Cart> removeItemFromCart(HttpServletRequest request,
                                             @PathVariable Long productId) {
        if (!isAuthenticated(request)) {
            throw new CustomException(
                    ErrorCode.UNAUTHORIZED.getCode(),
                    ErrorCode.UNAUTHORIZED.getMessage()
            );
        }

        HttpSession session = request.getSession();

        return toResponse(mapper.map(
            cartService.removeItemFromCart(session.getAttribute("userId").toString(), productId),
            CartDto.class)
        );
    }

    @GetMapping("/checkout")
    public Response<CheckoutDto> checkout(HttpServletRequest request) {
        if (!isAuthenticated(request)) {
            throw new CustomException(
                    ErrorCode.UNAUTHORIZED.getCode(),
                    ErrorCode.UNAUTHORIZED.getMessage()
            );
        }

        HttpSession session = request.getSession();

        return toResponse(cartService.checkout(session.getAttribute("userId").toString()));
    }

}
