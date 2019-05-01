package future.phase2.offlinetoonlinebazaar.controller;

import future.phase2.offlinetoonlinebazaar.model.dto.ProductDto;
import future.phase2.offlinetoonlinebazaar.model.entity.CartItem;
import future.phase2.offlinetoonlinebazaar.model.entity.Product;
import future.phase2.offlinetoonlinebazaar.model.response.Response;
import future.phase2.offlinetoonlinebazaar.service.CartService;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController extends GlobalController{

    @Autowired
    private CartService cartService;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    @GetMapping("/{userEmail}")
    public Response<List<CartItem>> getCartItems(@PathVariable @Valid String userEmail){
        return toResponse(cartService.getCartItems(userEmail));
    }

    @PostMapping("/add/{userEmail}/{productId}/{qty}")
    public Response<ProductDto> addItemToCart(@PathVariable String userEmail,
                                              @PathVariable Long productId,
                                              @PathVariable int qty){
        return toResponse(convertToDto(cartService.addItemToCart(userEmail, productId, qty)));
    }

    @PutMapping("/update/{userEmail}/{productId}/{qty}")
    public Response<ProductDto> updateItemQty (@PathVariable String userEmail,
                                               @PathVariable Long productId,
                                               @PathVariable int qty) {
        return toResponse(convertToDto(cartService.updateItemQty(userEmail, productId, qty)));
    }

    @DeleteMapping("/remove/{userEmail}/{productId}")
    public Response<Boolean> removeItemFromCart(@PathVariable String userEmail,
                                                @PathVariable Long productId){
        return toResponse(cartService.removeItemFromCart(userEmail, productId));
    }

    //Private
    private ProductDto convertToDto(Product product){
        MapperFacade mapper = mapperFactory.getMapperFacade();
        ProductDto productDto = mapper.map(product, ProductDto.class);

        return productDto;
    }

}
