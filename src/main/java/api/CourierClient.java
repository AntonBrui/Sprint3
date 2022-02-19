package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import api.model.Courier;
import api.model.CourierCredentials;

import java.io.File;

import static io.restassured.RestAssured.given;

public class CourierClient extends ScooterRestClient{

    public final String PATH = BASE_URL + "/courier";

    @Step("Create courier")
    public Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(PATH);
    }

    @Step("Create courier without login")
    public Response createCourierWithOutLogin(File json) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post(PATH);
    }

    @Step("Create courier without password")
    public Response createCourierWithOutPassword(File json) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post(PATH);
    }

    @Step("Authorization courier without login")
    public Response loginCourierWithOutLogin(File json) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post(PATH);
    }

    @Step("Authorization courier without password")
    public Response loginCourierWithOutPassword(File json) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post(PATH);
    }

    @Step("Login courier")
    public Response loginCourier(CourierCredentials courierCredentials) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courierCredentials)
                .when()
                .post(PATH + "/login");
    }

    @Step("Delete courier")
    public Response deleteCourier(int courierId) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete(PATH + "/" + courierId);
    }

}
