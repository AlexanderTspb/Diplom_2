import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class UserClient extends RestAssuredClientU {

    private static final String USER_PATH = "api/auth";

    @Step("Создаем пользователя {user}")
    public Response create(User user) {
        Response response = given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "/register");
        printResponseBodyToConsole(response,"запрос регистрации");

    //    String bearerAccessToken = response.jsonPath().getString("accessToken");
    //    System.out.println(bearerAccessToken);
    //    String accessToken = bearerAccessToken.substring(7);
    //    System.out.println(accessToken);

        return response;
    }

    @Step("Создаем пользователя без name {user}")
    public Response createUserWithoutName() {

        String email = RandomStringUtils.randomAlphabetic(10) + "@" + "yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);

        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("email",email);
        loginReqObj.put("password",password);

        Response response = given()
                .spec(getBaseSpec())
                .body(loginReqObj.toString())
                .when()
                .post(USER_PATH + "/register");
        printResponseBodyToConsole(response,"запрос регистрации");

        return response;
    }

    @Step("Создаем пользователя без email {name}")
    public Response createUserWithoutEmail() {

        // с помощью библиотеки RandomStringUtils генерируем пароль
        String password = RandomStringUtils.randomAlphabetic(10);
        // с помощью библиотеки RandomStringUtils генерируем имя
        String name = RandomStringUtils.randomAlphabetic(10);

        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("password",password);
        loginReqObj.put("name",name);

        Response response = given()
                .spec(getBaseSpec())
                .body(loginReqObj.toString())
                .when()
                .post(USER_PATH + "/register");
        printResponseBodyToConsole(response,"запрос регистрации");
        return response;
    }

    @Step("Создаем пользователя без password {email} {name}")
    public Response createUserWithoutPassword() {

        String email = RandomStringUtils.randomAlphabetic(10) + "@" + "yandex.ru";
        String name = RandomStringUtils.randomAlphabetic(10);

        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("email",email);
        loginReqObj.put("name",name);

        Response response = given()
                .spec(getBaseSpec())
                .body(loginReqObj.toString())
                .when()
                .post(USER_PATH + "/register");
        printResponseBodyToConsole(response,"запрос регистрации");
        return response;
    }

    @Step("Авторизуемся пользователем {loginRequestBody}")
    public Response login(LoginRequestBodyU loginRequestBody) {
           Response response = given()
                .spec(getBaseSpec())
                .body(loginRequestBody)
                .when()
                .post(USER_PATH + "/login");
        printResponseBodyToConsole(response,"запрос логина");

     //   String bearerAccessToken = response.jsonPath().getString("accessToken");
        //    System.out.println(bearerAccessToken);
     //   String accessToken = bearerAccessToken.substring(7);
    //    System.out.println(accessToken);

        return response;
    }

    @Step("Авторизуемся пользователем {loginRequestBody}")
    public Response loginWithUncorrectLogin(User user) {

        String email = RandomStringUtils.randomAlphabetic(10) + "@" + "yandex.ru";
        LoginRequestBodyU userBody = LoginRequestBodyU.from(user);
        String userPassword = userBody.password;
     //   System.out.println("некорректный email " + email);
     //   System.out.println("пароль" + userPassword);
        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("email",email);
        loginReqObj.put("password",userPassword);
        Response response = given()
                .spec(getBaseSpec())
                .body(loginReqObj.toString())
                .when()
                .post(USER_PATH + "/login");
        printResponseBodyToConsole(response,"запрос логина");
        return response;
    }

    @Step("Авторизуемся пользователем {loginRequestBody}")
    public Response loginWithUncorrectPassword(User user) {

        String userPassword = RandomStringUtils.randomAlphabetic(10);
        LoginRequestBodyU userBody = LoginRequestBodyU.from(user);
        String email = userBody.email;
     //   System.out.println("некорректный пароль " + userPassword);
        //   System.out.println("пароль" + userPassword);
        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("email",email);
        loginReqObj.put("password",userPassword);
        Response response = given()
                .spec(getBaseSpec())
                .body(loginReqObj.toString())
                .when()
                .post(USER_PATH + "/login");
        printResponseBodyToConsole(response,"запрос логина");
        return response;
    }

    @Step("Удаляем пользователя {user}")
    public Response deleteS(User user) {

        Response responseLogin = login(LoginRequestBodyU.from(user));
        String bearerAccessToken = responseLogin.jsonPath().getString("accessToken");
        //    System.out.println(bearerAccessToken);
        String accessToken = bearerAccessToken.substring(7);
    //    System.out.println(accessToken);

        Response response = given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .when()
                .delete(USER_PATH + "/user");
        printResponseBodyToConsole(response,"запрос на удаление пользователя");
        return response;
    }

    @Step("Логаут {refreshToken}")
    public Response logOut(String refreshToken) {

        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("token",refreshToken);

        Response response = given()
                .spec(getBaseSpec())
                .body(loginReqObj.toString())
                .when()
                .post(USER_PATH + "/logout");
        printResponseBodyToConsole(response,"запрос на выход из системы");
        return response;

    }

    @Step("Удаляем пользователя {accessToken}")
    public Response deleteAccessToken(String accessToken) {

        Response response = given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .when()
                .delete(USER_PATH + "/user");
        printResponseBodyToConsole(response,"запрос на удаление курьера");
        return response;

    }

    @Step("Меняем информацию о пользователе email без авторизации ")
    public Response changeUsersInfoEmail() {


        String userEmailSecond = (RandomStringUtils.randomAlphabetic(10) + "@" + "yandex.ru").toLowerCase();
        System.out.println("новый email ===" + userEmailSecond);
        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("email",userEmailSecond);

        Response response = given()
                .spec(getBaseSpec())
         //       .auth().oauth2(accessToken)
                .body(loginReqObj.toString())
                .when()
                .patch(USER_PATH + "/user");
        printResponseBodyToConsole(response,"запрос на изменение email");
        return response;
    }

    @Step("Меняем информацию о пользователе password без авторизации ")
    public Response changeUsersInfoPassword() {

        String userPasswordSecond = RandomStringUtils.randomAlphabetic(10);
        System.out.println("новый password === " + userPasswordSecond);
        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("password",userPasswordSecond);

        Response response = given()
                .spec(getBaseSpec())
                //       .auth().oauth2(accessToken)
                .body(loginReqObj.toString())
                .when()
                .patch(USER_PATH + "/user");
        printResponseBodyToConsole(response,"запрос на изменение password");
        return response;
    }

    @Step("Меняем информацию о пользователе name без авторизации")
    public Response changeUsersInfoName() {

        String userNameSecond = RandomStringUtils.randomAlphabetic(10);
        System.out.println("новый name === " + userNameSecond);
        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("name",userNameSecond);

        Response response = given()
                .spec(getBaseSpec())
                //       .auth().oauth2(accessToken)
                .body(loginReqObj.toString())
                .when()
                .patch(USER_PATH + "/user");
        printResponseBodyToConsole(response,"запрос на изменение name");
        return response;
    }


    @Step("Меняем email пользователя с авторизацией {accessToken}")
    public Response changeUsersInfoEmail(String accessToken) {

        String userEmailSecond = (RandomStringUtils.randomAlphabetic(10) + "@" + "yandex.ru").toLowerCase();
        System.out.println("новый email ===" + userEmailSecond);
        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("email",userEmailSecond);

        Response response = given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .and()
                .body(loginReqObj.toString())
                .when()
                .patch(USER_PATH + "/user");
        printResponseBodyToConsole(response,"запрос на изменение email");
        return response;
    }

    @Step("Меняем password пользователя с авторизацией {accessToken}")
    public Response changeUsersInfoPassword(String accessToken) {

        String userPasswordSecond = RandomStringUtils.randomAlphabetic(10);

        System.out.println("новый password === " + userPasswordSecond);
        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("password",userPasswordSecond);

        Response response = given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .and()
                .body(loginReqObj.toString())
                .when()
                .patch(USER_PATH + "/user");
        printResponseBodyToConsole(response,"запрос на изменение password");
        return response;
    }

    @Step("Меняем name пользователя с авторизацией {accessToken}")
    public Response changeUsersInfoName(String accessToken) {

        String userNameSecond = RandomStringUtils.randomAlphabetic(10);
        System.out.println("новый name === " + userNameSecond);
        JSONObject loginReqObj = new JSONObject();
        loginReqObj.put("name",userNameSecond);

        Response response = given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .and()
                .body(loginReqObj.toString())
                .when()
                .patch(USER_PATH + "/user");
        printResponseBodyToConsole(response,"запрос на изменение name");
        return response;
    }

    @Step("Print response body to console")
    public void printResponseBodyToConsole(Response response, String requestName){
        System.out.println( "Ответ на " + requestName + response.body().asString());
    }

}
