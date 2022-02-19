import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import api.OrderClient;
import api.model.Order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(Parameterized.class)

@DisplayName("Create Order")
public class OrderCreateTest {

    private OrderClient orderClient;
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;


    public OrderCreateTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {"Ivan", "Petrov", "Moscow City, Lenina street, house 1, apt. 120", "Petrovschina", "375298765432", 10, "2020-06-06", "Saske, come back to Konoha", new String[]{"BLACK"}},
                {"Anton", "Ivanov", "Minsk City, Lenina street, house 1, apt. 120", "Petrovschina", "375298765432", 15, "2022-06-06", "Saske, come back to Konoha", new String[]{"GREY"}},
                {"Dima", "Antonov", "Kiev City, Lenina street, house 1, apt. 120", "Petrovschina", "375298765432", 20, "2022-02-27", "Saske, come back to Konoha", new String[]{"BLACK", "GREY"}},
                {"Petr", "Vasilievich", "London City, Lenina street, house 1, apt. 120", "Petrovschina", "375298765432", 15, "2023-06-10", "Saske, come back to Konoha", null}

        };
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Check that the order can be created with different valid data")
    public void orderCanBeCreatedWithValidData() {
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        Response response = orderClient.createOrder(order);

        assertEquals("Status Code is not 201", 201, response.getStatusCode());
        assertNotNull(response.getBody().path("track"));

    }

}