package otob.model.constant.path;

public interface CartApiPath {

    String BASE_PATH = "/api/carts";
    String ADD_OR_UPDATE_ITEM = "/{productId}/{qty}";
    String REMOVE_ITEM = "/{productId}";
    String CHECKOUT = "/checkout";

}
