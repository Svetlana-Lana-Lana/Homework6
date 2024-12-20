package org.example.accuweather;

import io.qameta.allure.*;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

@Epic("Тестирование проекта accuweather.com")
@Feature("Тестирование API Locations")
public class DemoTest extends AccuweatherAbstractTest{

    @Test
    @DisplayName("Тест DemoTest - демонстрация покрытия запроса тестами")
    @Description("Демонстрация структуры теста")
    @Link("https://developer.accuweather.com/apis")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Получение списка регионов")
    @Owner("С.Д.")
    void getExampleTest() {
        given()
                .when()
                .get(getBaseUrl()+"/locations/v1/regions?" +
                        "apikey=" +getApiKey())
                .then()
                .statusCode(200);

    }

    @Test
    @DisplayName("Тест DemoTest - демонстрация покрытия запроса тестами")
    @Description("Демонстрация разных вариантов настройки методов, параметров, куки, хэдера запроса")
    @Link("https://developer.accuweather.com/apis")
    @Story("Получение списка регионов")
    @Owner("С.Д.")
    @Severity(SeverityLevel.NORMAL)
    void getSpecifyingRequestDataTest() {
        given()
                .when()
                .request(Method.GET,getBaseUrl()+"/locations/{version}/regions?" +
                        "apikey={apiKey}", "v1", getApiKey())
                .then()
                .statusCode(200);

        given()
                .queryParam("apikey", getApiKey())
                .pathParam("version", "v1")
                .when()
                .request(Method.GET,getBaseUrl()+"/locations/{version}/regions")
                .then()
                .statusCode(200);

        given().cookie("username","Sveta")
                .cookie( new Cookie
                        .Builder("some_cookie", "some_value")
                        .setSecured(true)
                        .setComment("some comment")
                        .build())
                .when()
                .get(getBaseUrl()+"/locations/v1/regions?" +
                        "apikey=" +getApiKey())
                .then()
                .statusCode(200);

        given().headers("username","Sveta")
                .when()
                .get(getBaseUrl()+"/locations/v1/regions?" +
                        "apikey=" +getApiKey())
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Тест DemoTest - демонстрация покрытия запроса тестами")
    @Description("Демонстрация способов взаимодействия с ответом")
    @Link("https://developer.accuweather.com/apis")
    @Severity(SeverityLevel.TRIVIAL)
    @Story("Получение списка регионов")
    @Owner("С.Д.")
    void getResponseData(){
        Response response = given()
                .when()
                .request(Method.GET,getBaseUrl()+"/locations/{version}/regions?" +
                        "apikey={apiKey}", "v1", getApiKey());

        // Get all headers
        Headers allHeaders = response.getHeaders();
        // Get a single header value:
        System.out.println("Content-Encoding: " + response.getHeader("Content-Encoding"));

        // Get all cookies as simple name-value pairs
        Map<String, String> allCookies = response.getCookies();
        // Get a single cookie value:
        System.out.println("CookieName: " + response.getCookie("cookieName"));

        // Get status line
        System.out.println("StatusLine: " + response.getStatusLine());
        // Get status code
        System.out.println("Code: " + response.getStatusCode());


        String regionId = given()
                .when()
                .request(Method.GET,getBaseUrl()+"/locations/{version}/regions?" +
                        "apikey={apiKey}", "v1", getApiKey())
                .path("data[0].ID");

        System.out.println("region code: " + regionId);

        String localizedName = given()
                .when()
                .request(Method.GET,getBaseUrl()+"/locations/{version}/regions?" +
                        "apikey={apiKey}", "v1", getApiKey())
                .then().extract()
                .jsonPath()
                .getString("data[2].LocalizedName");

        System.out.println("localizedName: " + localizedName);
    }

    @Test
    @DisplayName("Тест DemoTest - ")
    @Description("Демонстрация способов проверки результатов запроса с помощью ассертов")
    @Link("https://developer.accuweather.com/apis")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Получение списка регионов")
    @Owner("С.Д.")
    void getVerifyingResponseData(){

        JsonPath response = given()
                .when()
                .request(Method.GET,getBaseUrl()+"/locations/{version}/regions?" +
                        "apikey={apiKey}", "v1", getApiKey())
                .body()
                .jsonPath();

        assertThat(response.get("[0].ID"), equalTo("AFR"));
        assertThat(response.get("[1].ID"), equalTo("ANT"));
        assertThat(response.get("[2].ID"), equalTo("ARC"));


        given().response().expect()
                .body("[0].LocalizedName", equalTo("Africa"))
                .body("[0].EnglishName", equalTo("Africa"))
                .when()
                .request(Method.GET,getBaseUrl()+"/locations/{version}/regions?" +
                        "apikey={apiKey}", "v1", getApiKey())
                .then()
                .assertThat()
                //.cookie("cookieName", "cookieValue")
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .header("Connection", "keep-alive")
                .contentType(ContentType.JSON)
                .time(lessThan(2000L));
    }
}
