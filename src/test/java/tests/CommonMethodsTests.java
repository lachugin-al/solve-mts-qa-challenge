package tests;

import com.sun.xml.bind.v2.model.core.ID;
import config.Endpoints;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.Post;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import service.CommonFunctions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static service.CommonMethods.createPost;
import static service.CommonMethods.getPosts;


public class CommonMethodsTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonMethodsTests.class);

    private static final int POST_ID = 1;
    private static final int OUT_OF_RANGE_POST_ID = 101;
    private static final int STATUS_CODE_OK = 200;
    private static final int STATUS_CODE_CREATED = 201;
    private static final int STATUS_NOT_FOUND = 404;
    List<Post> post = new ArrayList<>();

    @BeforeAll
    public static void setupSpecification() {
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBaseUri(Endpoints.BASE_URL)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.ANY)
                .log(LogDetail.ALL)
                .build();
        ResponseSpecification responseSpecification = new ResponseSpecBuilder()
                .build();

        RestAssured.requestSpecification = requestSpecification;
        RestAssured.responseSpecification = responseSpecification;

    }

    @Test
    @Description(value = "Получение списка постов по идентификатору id")
    @Step(value = "Получение списка постов по идентификатору id")
    public void getPostByIdAndCheckWithPostID() {
        getPosts(POST_ID)
                .then()
                .body(Endpoints.ID, equalTo(POST_ID))
                .and()
                .body("id", is(POST_ID))
                .and()
                .statusCode(STATUS_CODE_OK);
    }

    @Test
    @Epic(value = "Epic Test")
    @Feature(value = "Feature Test")
    @Story(value = "Story Test")
    @Severity(value = SeverityLevel.MINOR)
    @Description(value = "Получение списка всех постов")
    @Step(value="Получение списка всех постов")
    public void getAllPostAndCheckSize() throws IOException {
        ValidatableResponse response = getPosts().then().statusCode(STATUS_CODE_OK);
        post = response.extract().jsonPath().getList(".", Post.class);
        Assertions.assertEquals(100, post.size(), "Количество постов не равно 100");

        CommonFunctions.getBytesAnnotationWithArgs("json.json");
        CommonFunctions.getBytes("picture.jpg");
        CommonFunctions.getBytes("text.txt");
    }

    @Test
    @Description(value = "Получение списка постов более 100")
    @Step(value = "Получение списка постов более 100")
    public void getOutOfRangePosts() {
        getPosts(OUT_OF_RANGE_POST_ID)
                .then()
                .statusCode(STATUS_NOT_FOUND);
    }

    @Test
    @Description(value = "Добавление нового поста")
    @Step(value = "Добавление нового поста")
    public void createNewPostAndCheckIt() {

        String jsonBody = "  {\n" +
                "    \"userId\": 900,\n" +
                "    \"id\": 900,\n" +
                "    \"title\": \"newtitle\",\n" +
                "    \"body\": \"newbody\"\n" +
                "  }";

        Response response = createPost(jsonBody)
                .then().statusCode(STATUS_CODE_CREATED)
                .extract().response();

        JsonPath json = response.jsonPath();

        assertEquals("newtitle", json.get("title"), "Параметр title не соотствует ожидаемому значению");
        assertEquals("newbody", json.get("body"), "Параметр body не соотствует ожидаемому значению");
    }

}
