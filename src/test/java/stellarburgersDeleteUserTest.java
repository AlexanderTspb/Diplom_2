import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class stellarburgersDeleteUserTest {

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
        System.out.println("Конец теста");
    }

    @Test
    @Description("Удаление пользователя")
    @DisplayName("Удаление пользователя с корректными параметрами c авторизацией")
    public void deleteCourierWhenCorrectParametersTest(){

        Response responseDelete = userClient.deleteS(user);
        responseDelete.then().assertThat().statusCode(202)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @Description("Удаление пользователя")
    @DisplayName("Удаление пользователя с корректными параметрами через accessToken без авторизации")
    public void deleteCourierWhenCorrectParametersWithAccessTokenTest(){

        Response responseDelete = userClient.deleteAccessToken(accessToken);
        responseDelete.then().assertThat().statusCode(202)
                .and()
                .body("success", equalTo(true));
    }

}
