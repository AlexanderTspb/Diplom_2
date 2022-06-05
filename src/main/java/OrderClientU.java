import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClientU extends RestAssuredClientU {

    private static final String ORDER_PATH = "api/orders";

    @Step("Создаем заказ без авторизации {order}")
    public Response create(OrderU order) {
        Response response =  given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH);
        printResponseBodyToConsole(response,"запрос на создание заказа не авторизованным пользователем");
        return response;
    }

    @Step("Создаем заказ с авторизацией {order}")
    public Response create(OrderU order,String accessToken) {
        Response response =  given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .and()
                .body(order)
                .when()
                .post(ORDER_PATH);
        printResponseBodyToConsole(response,"запрос на создание заказа авторизованным пользователем");
        return response;
    }

    @Step("Получаем список заказов с авторизацией {accessToken}")
    public Response getOrderList(String accessToken) {
        Response response = given()
                    .spec(getBaseSpec())
                    .auth().oauth2(accessToken)
                    .get(ORDER_PATH);
        printResponseBodyToConsole(response,"запрос на получение списка заказов авторизованным пользователем");
        return response;

    }

    @Step("Получаем список заказов без авторизации")
    public Response getOrderList() {
        Response response = given()
                .spec(getBaseSpec())
         //       .auth().oauth2(accessToken)
                .get(ORDER_PATH);
        printResponseBodyToConsole(response,"запрос на получение списка заказов не авторизованным пользователем");
        return response;

    }


    @Step("Print response body to console")
    public void printResponseBodyToConsole(Response response, String requestName){
        System.out.println( "Ответ на " + requestName + response.body().asString());
    }
}
