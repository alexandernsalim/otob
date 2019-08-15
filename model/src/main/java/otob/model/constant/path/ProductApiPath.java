package otob.model.constant.path;

public interface ProductApiPath {

  String BASE_PATH = "/api/products";
  String PRODUCTID_PLACEHOLDER = "/{productId}";

  String GET_PRODUCT_BY_ID = "/id" + PRODUCTID_PLACEHOLDER;
  String GET_PRODUCT_BY_NAME = "/name/{name}";
  String ADD_PRODUCT_FROM_EXCEL = "/batch";

}
