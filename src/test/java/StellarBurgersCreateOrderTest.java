import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class StellarBurgersCreateOrderTest {

    private OrderClient orderClient;
    private UserClient userClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        userClient = new UserClient();
    }

    @Test
    @Description("Создание заказа")
    @DisplayName("Создание заказа с корректными параметрами без авторизации")
    public void createOrderWithoutAccessTokenTest() {
        Order order = new Order(new String[]{"61c0c5a71d1f82001bdaaa72", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa6c"});
        Response response = orderClient.create(order);
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @Description("Создание заказа")
    @DisplayName("Создание заказа c некорректными параметрами без авторизации")
    public void createOrderWithIncorrectIngredientsAndWithoutAccessTokenTest() {
        Order order = new Order(new String[]{"61c0c5a71d1f82001bdaaa72INCORRECT", "61c0c5a71d1f82001bdaaa6fINCORRECT", "61c0c5a71d1f82001bdaaa6cINCORRECT"});
        Response response = orderClient.create(order);
        response.then().assertThat().statusCode(500);

    }

    @Test
    @Description("Создание заказа")
    @DisplayName("Создание заказа c пустыми параметрами без авторизации")
    public void createOrderWithoutIngredientsAndWithoutAccessTokenTest() {
        Order order = new Order(new String[]{});
        Response response = orderClient.create(order);
        response.then().assertThat().statusCode(400)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @Description("Создание заказа")
    @DisplayName("Создание заказа c корректными параметрами с авторизацией")
    public void createOrderWithAccessTokenTest() {
        User user = User.getRandom();
        userClient.create(user);
        Response responseLogin = userClient.login(LoginRequestBody.from(user));
        String bearerAccessToken = responseLogin.jsonPath().getString("accessToken");
        String accessToken = bearerAccessToken.substring(7);
        Order order = new Order(new String[]{"61c0c5a71d1f82001bdaaa72", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa6c"});
        Response response = orderClient.create(order, accessToken);
        response.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));

        userClient.deleteAccessToken(accessToken);
    }

    @Test
    @Description("Создание заказа")
    @DisplayName("Создание заказа c некорректными параметрами с авторизацией")
    public void createOrderWithIncorrectIngredientsAndWithAccessTokenTest() {
        User user = User.getRandom();
        userClient.create(user);
        Response responseLogin = userClient.login(LoginRequestBody.from(user));
        String bearerAccessToken = responseLogin.jsonPath().getString("accessToken");
        String accessToken = bearerAccessToken.substring(7);
        Order order = new Order(new String[]{"61c0c5a71d1f82001bdaaa72INCORRECT", "61c0c5a71d1f82001bdaaa6fINCORRECT", "61c0c5a71d1f82001bdaaa6cINCORRECT"});
        Response response = orderClient.create(order, accessToken);
        response.then().assertThat().statusCode(500);
        userClient.deleteAccessToken(accessToken);
    }

    @Test
    @Description("Создание заказа")
    @DisplayName("Создание заказа c пустыми параметрами с авторизацией")
    public void createOrderWithoutIngredientsAndWithAccessTokenTest() {
        User user = User.getRandom();
        userClient.create(user);
        Response responseLogin = userClient.login(LoginRequestBody.from(user));
        String bearerAccessToken = responseLogin.jsonPath().getString("accessToken");
        String accessToken = bearerAccessToken.substring(7);
        Order order = new Order(new String[]{});
        Response response = orderClient.create(order, accessToken);
        response.then().assertThat().statusCode(400)
                .and()
                .body("success", equalTo(false));
        userClient.deleteAccessToken(accessToken);
    }
}