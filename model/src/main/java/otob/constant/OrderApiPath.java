package otob.constant;

public interface OrderApiPath {

  String BASE_PATH = "/api/orders";
  String GET_ALL_ORDER = "/all";
  String GET_USER_ALL_ORDER = "/user/all";
  String FIND_ORDER = "/{orderId}/search";
  String ACCEPT_ORDER = "/{orderId}/accept";
  String REJECT_ORDER = "/{orderId}/reject";

}
