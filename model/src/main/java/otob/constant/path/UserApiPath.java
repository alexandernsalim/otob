package otob.constant.path;

public interface UserApiPath {

    String BASE_PATH = "/api/users";
    String GET_ALL_USER = "/all";
    String REGISTER_ADMIN = "/admin/register";
    String REGISTER_CASHIER = "/cashier/register";
    String REGISTER_CUSTOMER = "/customer/register";
    String DELETE_USER = "/{email}/delete";

}
