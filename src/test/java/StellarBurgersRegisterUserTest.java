import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class StellarBurgersRegisterUserTest {

    private UserClient userClient;
    private User user;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        System.out.println("Конец теста");
    }

    @Test
    @Description("Создание пользователя")
    @DisplayName("Создание пользователя c корректными параметрами")
    public void registerUserWhenCorrectParametersTest() {
        user = User.getRandom();
        Response response = userClient.create(user);
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
        String bearerAccessToken = response.jsonPath().getString("accessToken");
        String accessToken = bearerAccessToken.substring(7);
        userClient.deleteAccessToken(accessToken);
    }

    @Test
    @Description("Создание пользователя c уже существующим email")
    @DisplayName("Создание пользователя с подстановкой существующего email")
    public void registerUserWithExistingEmailTest() {
        user = User.getRandom();
        Response responseFirst = userClient.create(user);
        Response responseSecond = userClient.create(user);
        responseSecond.then().assertThat().statusCode(403)
                .and()
                .body("message", equalTo("User already exists"));
        String bearerAccessToken = responseFirst.jsonPath().getString("accessToken");
        String accessToken = bearerAccessToken.substring(7);
        userClient.deleteAccessToken(accessToken);
    }

    @Test
    @Description("Создание пользователя без имени")
    @DisplayName("Создание пользователя без подстановки Name")
    public void registerCourierWithoutNameTest() {
        Response response = userClient.createUserWithoutName();
        response.then().assertThat().statusCode(403)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @Description("Создание пользователя без email")
    @DisplayName("Создание курьера без подстановки email")
    public void registerCourierWithoutLoginTest() {
        Response response = userClient.createUserWithoutEmail();
        response.then().assertThat().statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Description("Создание пользователя без пароля")
    @DisplayName("Создание пользователя без подстановки password")
    public void registerUserWithoutPasswordTest() {
        Response response = userClient.createUserWithoutPassword();
        response.then().assertThat().statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}