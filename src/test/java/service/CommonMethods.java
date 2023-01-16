package service;

import config.Endpoints;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CommonMethods {
    public static Response getPosts() {
        return given()
                .when()
                .get(Endpoints.POSTS)
                .then()
                .log().ifError()
                .extract()
                .response();
    }

    public static Response getPosts(int id) {
        return given()
                .pathParam(Endpoints.ID, id)
                .when()
                .get(Endpoints.POSTS_ID)
                .then()
                .log().ifError()
                .extract()
                .response();
    }

    public static Response createPost(String post) {
        return given()
                .contentType("application/json")
                .body(post)
                .when()
                .post(Endpoints.POSTS)
                .then()
                .log().ifError()
                .extract()
                .response();
    }
}
