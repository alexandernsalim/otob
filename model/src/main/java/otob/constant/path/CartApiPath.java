package otob.constant.path;

public interface CartApiPath {

    String BASE_PATH = "/api/carts";
    String ADD_ITEM = "/add/{productId}/{qty}";
    String UPDATE_ITEM_QTY = "/update/{productId}/{qty}";
    String REMOVE_ITEM = "/remove/{productId}";
    String CHECKOUT = "/checkout";

}
