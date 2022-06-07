import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class StellarBurgersLoginCourierTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandom();
        Response responseCreate = userClient.create(user);
        String bearerAccessToken = responseCreate.jsonPath().getString("accessToken");
        accessToken = bearerAccessToken.substring(7);
    }

    @After
    public void tearDown() {
        userClient.deleteAccessToken(accessToken);
        System.out.println("Конец теста");
    }

    @Test
    @Description("Логин курьера")
    @DisplayName("Логин курьера в системе с корректными параметрами")
    public void loginCourierWhenCorrectParametersTest() {
        //отправляем запрос на логин созданного курьера
        Response responseLogin = userClient.login(LoginRequestBody.from(user));
        responseLogin.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @Description("Логин курьера c несуществующим логином")
    @DisplayName("Логин курьера с подстановкой несуществующего login")
    public void loginUserWhenUncorrectLoginTest() {
        Response responseSecond = userClient.loginWithUncorrectLogin(user);
        responseSecond.then().assertThat().statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @Description("Логин курьера c некорректным паролем")
    @DisplayName("Логин курьера с подстановкой несуществующего password")
    public void loginUserWhenUncorrectPasswordTest() {
        Response responseSecond = userClient.loginWithUncorrectPassword(user);
        responseSecond.then().assertThat().statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }
}