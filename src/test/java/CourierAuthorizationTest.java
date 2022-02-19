import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import api.CourierClient;
import api.model.Courier;
import api.model.CourierCredentials;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@DisplayName("Courier Authorization")
public class CourierAuthorizationTest {

    private CourierClient courierClient;
    private int courierId;

    private final String login = "DHL";
    private final String password = "12345678";
    private final String firstName = "DHL_Name";

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        courierClient.deleteCourier(courierId);
    }

    // Проверяем, что курьер может авторизоваться;
    @Test
    @DisplayName("Check that the courier can be logged")
    public void courierCanBeLoggedWithValidData() {
        Courier courier = new Courier(login, password, firstName);
        CourierCredentials courierCredentials = new CourierCredentials(login, password);

        Response response = courierClient.createCourier(courier);
        Response login = courierClient.loginCourier(courierCredentials);
        courierId = login.getBody().path("id");
        assertEquals("Status code is not 200", 200, login.getStatusCode());
        assertNotNull(login.getBody().path("id"));

    }

    // Проверяем, что если авторизоваться под несуществующим пользователем (неверный логин), запрос возвращает ошибку;
    @Test
    @DisplayName("Check the response in case of authorization with non-existent login")
    public void courierCanNotBeLoggedWithInvalidLogin() {
        Courier courier = new Courier(login, password, firstName);
        CourierCredentials courierCredentials = new CourierCredentials(login, password);
        Response response = courierClient.createCourier(courier);
        Response validLogin = courierClient.loginCourier(courierCredentials);
        courierId = validLogin.getBody().path("id");

        CourierCredentials invalidCourierCredentials = new CourierCredentials(login + "1", password);
        Response invalidLogin = courierClient.loginCourier(invalidCourierCredentials);
        assertEquals("Status code is not 404", 404, invalidLogin.getStatusCode());
        assertEquals("Учетная запись не найдена", invalidLogin.getBody().path("message"));

    }

    // Проверяем, что если авторизоваться с неверным паролем, запрос возвращает ошибку;
    @Test
    @DisplayName("Check the response in case of authorization with non-existent password")
    public void courierCanNotBeLoggedWithInvalidPassword() {
        Courier courier = new Courier(login, password, firstName);
        CourierCredentials courierCredentials = new CourierCredentials(login, password);
        Response response = courierClient.createCourier(courier);
        Response validLogin = courierClient.loginCourier(courierCredentials);
        courierId = validLogin.getBody().path("id");

        CourierCredentials invalidCourierCredentials = new CourierCredentials(login, password + "1");
        Response invalidLogin = courierClient.loginCourier(invalidCourierCredentials);
        assertEquals("Status code is not 404", 404, invalidLogin.getStatusCode());
        assertEquals("Учетная запись не найдена", invalidLogin.getBody().path("message"));

    }

    // Проверяем, для авторизации нужно передать все обязательные поля;
    @Test
    @DisplayName("Check that the login field is a required field")
    public void courierCanNotBeLoggedWithOutLogin() {
        File json = new File("src/test/resources/bodyWithOutLogin.json");
        Response response = courierClient.loginCourierWithOutLogin(json);

        assertEquals("Status code is not 400", 400, response.getStatusCode());
        assertEquals("Недостаточно данных для входа", response.getBody().path("message"));

    }

    @Test
    @DisplayName("Check that the password field is a required field")
    public void courierCanNotBeLoggedWithOutPassword() {
        File json = new File("src/test/resources/bodyWithOutLogin.json");
        Response response = courierClient.loginCourierWithOutPassword(json);

        assertEquals("Status code is not 400", 400, response.getStatusCode());
        assertEquals("Недостаточно данных для входа", response.getBody().path("message"));

    }
}
