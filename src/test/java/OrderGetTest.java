import api.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DisplayName("Get Order")
public class OrderGetTest {

    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Check the ability to get order list")
    public void getOrdersList() {
        Response response = orderClient.get();
        List<Object> orders = response.getBody().path("orders");
        assertNotNull("Order list is empty!", orders);
        assertEquals("Status code is not 200!", 200, response.getStatusCode());

    }
}