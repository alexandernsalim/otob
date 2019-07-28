package otob.constant;

public interface ProductApiPath {

  String BASE_PATH = "/api/products";
  String PRODUCTID_PLACEHOLDER = "/{productId}";

  String GET_PRODUCT_BY_ID = "/id" + PRODUCTID_PLACEHOLDER;
  String GET_PRODUCT_BY_NAME = "/name/{productName}";
  String ADD_PRODUCT_FROM_EXCEL = "/batch";

}
