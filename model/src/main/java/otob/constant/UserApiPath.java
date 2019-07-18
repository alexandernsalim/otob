package otob.constant;

public interface UserApiPath {

    String BASE_PATH = "/api/users";
    String GET_ALL = "/all";
    String REGISTER_ADMIN = "/admin/register";
    String REGISTER_CASHIER = "/cashier/register";
    String REGISTER_CUSTOMER = "/customer/register";
    String DELETE_USER = "/{email}/delete";

}
