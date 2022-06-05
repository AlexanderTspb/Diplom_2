import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class stellarburgersGetOrderListTest {

    private OrderClientU orderClient;
    private UserClient userClient;
    private User user;

    private String accessToken;

    @Before
    public void setUp() {
        orderClient = new OrderClientU();
        userClient = new UserClient();

        user = User.getRandom();
        userClient.create(user);

    }

    @After
    public void tearDown() {

        userClient.deleteAccessToken(accessToken);
        System.out.println("Конец теста");

    }

    @Test
    @Description("Получение списка заказов")
    @DisplayName("Получение списка заказов c корректными параметрами с авторизацией")
    public void getOrderListWithAccessTokenTest(){

        Response responseLogin =  userClient.login(LoginRequestBodyU.from(user));

        String bearerAccessToken = responseLogin.jsonPath().getString("accessToken");
        //    System.out.println(bearerAccessToken);
        accessToken = bearerAccessToken.substring(7);
        //    System.out.println(accessToken);

        //создаем заказ
        OrderU order = new OrderU(new String[]{"61c0c5a71d1f82001bdaaa72","61c0c5a71d1f82001bdaaa6f","61c0c5a71d1f82001bdaaa6c"});

        orderClient.create(order,accessToken);
        orderClient.create(order,accessToken);
        //запрашиваем список заказов пользователя
        Response responseOrderList =  orderClient.getOrderList(accessToken);
        responseOrderList.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));

    }

    @Test
    @Description("Получение списка заказов")
    @DisplayName("Получение списка заказов c корректными параметрами без авторизации")
    public void getOrderListWithoutAccessTokenTest(){

        //авторизуемся пользователем
        Response responseLogin =  userClient.login(LoginRequestBodyU.from(user));
        String bearerAccessToken = responseLogin.jsonPath().getString("accessToken");
        //    System.out.println(bearerAccessToken);
        accessToken = bearerAccessToken.substring(7);
        String refreshToken = responseLogin.jsonPath().getString("refreshToken");
        //    System.out.println(accessToken);

        //создаем заказ
        OrderU order = new OrderU(new String[]{"61c0c5a71d1f82001bdaaa72","61c0c5a71d1f82001bdaaa6f","61c0c5a71d1f82001bdaaa6c"});

        orderClient.create(order,accessToken);
        orderClient.create(order,accessToken);
        //выходим из системы
        userClient.logOut(refreshToken);
        //запрашиваем список заказов пользователя
        Response responseOrderList =  orderClient.getOrderList();
        responseOrderList.then().assertThat().statusCode(401)
                .and()
                .body("success", equalTo(false));

    }


}
