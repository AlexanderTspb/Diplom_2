public class LoginRequestBodyU {

    public final String email;
    public final String password;

    public LoginRequestBodyU(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static LoginRequestBodyU from(User user) {
        return new LoginRequestBodyU(user.email, user.password);
    }

    @Override
    public String toString() {
        return "loginRequestBody{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
