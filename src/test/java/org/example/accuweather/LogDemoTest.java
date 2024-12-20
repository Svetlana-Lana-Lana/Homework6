package org.example.accuweather;

import io.qameta.allure.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@Epic("Тестирование проекта accuweather.com")
@Feature("Тестирование API Locations")
public class LogDemoTest extends AccuweatherAbstractTest{

    @Test
    @DisplayName("LogDemoTest - демонстрация работы с логами проекта")
    @Description("Демонстрация добавления логов к запросу - логирование параметров и метод взаимодействия с сервисом")
    @Link("https://developer.accuweather.com/apis")
    @Severity(SeverityLevel.NORMAL)
    @Story("Получение информации о 50 лучших городах мира")
    @Owner("С.Д.")
    void logRequestTest() {
        given().log().parameters().log().method()
                .queryParam("apikey", getApiKey())
                .pathParam("version", "v1")
                .pathParam("top", 50)
                .when()
                .get(getBaseUrl()+"/locations/{version}/topcities/{top}")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("LogDemoTest - демонстрация работы с логами проекта")
    @Description("Демонстрация добавления логов к ответу - вывод объекта body")
    @Link("https://developer.accuweather.com/apis")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Получение информации о 50 лучших городах мира")
    @Owner("С.Д.")
    void logResponseTest() {
        given().log().parameters()
                .queryParam("apikey", getApiKey())
                .pathParam("version", "v1")
                .pathParam("top", 50)
                .when()
                .get(getBaseUrl()+"/locations/{version}/topcities/{top}")
                .then().log().body()
                .statusCode(200);
    }

    @Test
    @DisplayName("LogDemoTest - демонстрация работы с логами проекта")
    @Description("Заведомо ложный запрос, для демонстрации логирования по умолчанию, в случае, если тест непройден")
    @Link("https://developer.accuweather.com/apis")
    @Severity(SeverityLevel.NORMAL)
    @Story("Получение информации о 50 лучших городах мира")
    @Owner("С.Д.")
    @Disabled
    void logFailTest() {
        given().log().parameters()
                .queryParam("apikey", getApiKey())
                .pathParam("version", "v1")
                .pathParam("top", 50)
                .when()
                .get(getBaseUrl()+"/locations/{version}/topcities/{top}")
                .then()
                .statusCode(400);
    }
}
