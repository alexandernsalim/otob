package future.phase2.offlinetoonlinebazaar.model.dto;

import future.phase2.offlinetoonlinebazaar.model.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    private String userEmail;

    private List<CartItem> cartItems;

}
