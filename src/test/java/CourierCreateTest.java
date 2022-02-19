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

@DisplayName("Create Courier")
public class CourierCreateTest {

    private CourierClient courierClient;
    private int courierId;

    private final String login = "DHL";
    private final String password = "12345678";
    private final String firstName = "DHL_Name";

    @Before
    public void setUp () {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        courierClient.deleteCourier(courierId);
    }

    // Проверяем, что курьера можно создать и успешный запрос возвращает ok: true;
    @Test
    @DisplayName("Check that the courier can be created")
    public void courierCanBeCreatedWithValidData() {
        Courier courier = new Courier(login, password, firstName);
        CourierCredentials courierCredentials = new CourierCredentials(login, password);

        Response response = courierClient.createCourier(courier);
        Response login = courierClient.loginCourier(courierCredentials);
        courierId = login.getBody().path("id");
        assertEquals("Status code is not 201!", 201, response.getStatusCode());
        assertEquals(true, response.getBody().path("ok"));

    }

    // Проверяем, что нельзя создать двух абсолютно одинаковых курьеров (одинаковый логин и пароль);
    @Test
    @DisplayName("Check that a duplicate courier cannot be created")
    public void courierCanNotBeCreatedWithDuplicateData() {
        Courier courier = new Courier(login, password, firstName);
        CourierCredentials courierCredentials = new CourierCredentials(login, password);

        Response courierCreateFirstResponse = courierClient.createCourier(courier);
        Response login = courierClient.loginCourier(courierCredentials);
        courierId = login.getBody().path("id");

        Response courierCreateSecondResponse = courierClient.createCourier(courier);
        assertEquals("Status code is not 409", 409, courierCreateSecondResponse.getStatusCode());
        assertEquals("Этот логин уже используется", courierCreateSecondResponse.getBody().path("message"));
    }

    // Проверяем, что нельзя создать двух курьеров с одинаковым логином;
    @Test
    @DisplayName("Check that the courier cannot be created with existing login")
    public void courierCanNotBeCreatedWithDuplicateLogin() {
        Courier firstCourier = new Courier(login, password, firstName);
        Courier secondCourier = new Courier(login, password + "1", firstName);
        CourierCredentials courierCredentials = new CourierCredentials("DHL", "12345678");

        Response courierCreateFirstResponse = courierClient.createCourier(firstCourier);
        assertEquals("Status code is not 201",201, courierCreateFirstResponse.getStatusCode());
        assertEquals(true, courierCreateFirstResponse.getBody().path("ok"));
        Response login = courierClient.loginCourier(courierCredentials);
        courierId = login.getBody().path("id");

        Response courierCreateSecondResponse = courierClient.createCourier(secondCourier);
        assertEquals("Status code is not 409", 409, courierCreateSecondResponse.getStatusCode());
        assertEquals("Этот логин уже используется", courierCreateSecondResponse.getBody().path("message"));
    }


    // Проверяем, что нельзя создать курьера без логина;
    @Test
    @DisplayName("Check that the courier cannot be created without login")
    public void courierCanNotBeCreatedWithOutLogin() {

        File json = new File("src/test/resources/bodyWithOutLogin.json");
        Response response = courierClient.createCourierWithOutLogin(json);
        assertEquals("Status code is not 400", 400, response.getStatusCode());
        assertEquals("Недостаточно данных для создания учетной записи", response.getBody().path("message"));

    }

    // Проверяем, что нельзя создать курьера без пароля;
    @Test
    @DisplayName("Check that the courier cannot be created without password")
    public void courierCanNotBeCreatedWithOutPassword() {
        File json = new File("src/test/resources/bodyWithOutPassword.json");
        Response response = courierClient.createCourierWithOutPassword(json);
        assertEquals("Status code is not 400", 400, response.getStatusCode());
        assertEquals("Недостаточно данных для создания учетной записи", response.getBody().path("message"));

    }
}
