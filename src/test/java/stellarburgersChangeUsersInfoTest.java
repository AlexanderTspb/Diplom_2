import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class stellarburgersChangeUsersInfoTest {

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
    @Description("Измененение информации о пользователе")
    @DisplayName("Измененение информации о пользователе(email) без подставления токена авторизации")
    public void changeUserEmailWithoutAccessTokenTest(){

        Response responseChangeUserInfo = userClient.changeUsersInfoEmail();
        responseChangeUserInfo.then().assertThat().statusCode(401)
                .and()
                .body("success", equalTo(false));
    }

    @Test
    @Description("Измененение информации о пользователе")
    @DisplayName("Измененение информации о пользователе(password) без подставления токена авторизации")
    public void changeUserPasswordWithoutAccessTokenTest(){


        Response responseChangeUserInfo = userClient.changeUsersInfoPassword();
        responseChangeUserInfo.then().assertThat().statusCode(401)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message",equalTo("You should be authorised"));
    }

    @Test
    @Description("Измененение информации о пользователе")
    @DisplayName("Измененение информации о пользователе(name) без подставления токена авторизации")
    public void changeUserNameWithoutAccessTokenTest(){


        Response responseChangeUserInfo = userClient.changeUsersInfoName();
        responseChangeUserInfo.then().assertThat().statusCode(401)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message",equalTo("You should be authorised"));
    }


    @Test
    @Description("Измененение информации о пользователе")
    @DisplayName("Измененение информации о пользователе(email) с подстановкой токена авторизации")
    public void changeUserEmailWithAccessTokenTest(){


        Response responseLogin =  userClient.login(LoginRequestBodyU.from(user));
        String bearerAccessToken = responseLogin.jsonPath().getString("accessToken");
        String accessToken = bearerAccessToken.substring(7);

        Response responseChangeInfo = userClient.changeUsersInfoEmail(accessToken);
        responseChangeInfo.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @Description("Измененение информации о пользователе")
    @DisplayName("Измененение информации о пользователе(password) с подстановкой токена авторизации")
    public void changeUserPasswordWithAccessTokenTest(){


        Response responseLogin =  userClient.login(LoginRequestBodyU.from(user));
        String bearerAccessToken = responseLogin.jsonPath().getString("accessToken");
        String accessToken = bearerAccessToken.substring(7);

        Response responseChangeInfo = userClient.changeUsersInfoPassword(accessToken);
        responseChangeInfo.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @Description("Измененение информации о пользователе")
    @DisplayName("Измененение информации о пользователе(name) с подстановкой токена авторизации")
    public void changeUserNameWithAccessTokenTest(){


        Response responseLogin =  userClient.login(LoginRequestBodyU.from(user));
        String bearerAccessToken = responseLogin.jsonPath().getString("accessToken");
        String accessToken = bearerAccessToken.substring(7);

        Response responseChangeInfo = userClient.changeUsersInfoName(accessToken);
        responseChangeInfo.then().assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

}
