package otob.model.constant.path;

public interface UserApiPath {

    String BASE_PATH = "/api/users";
    String REGISTER_ADMIN = "/admin/register";
    String REGISTER_CASHIER = "/cashier/register";
    String REGISTER_CUSTOMER = "/customer/register";
    String CHANGE_PASSWORD = "/api/users/change-password";
    String DELETE_USER = "/{email}/delete";

}
