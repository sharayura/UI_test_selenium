package helpers;

import io.qameta.allure.Step;

public class Assertions {
    @Step("Проверяем что нет ошибки: {message}")
    public static void assertTrue(boolean condition, String message) {
        org.junit.jupiter.api.Assertions.assertTrue(condition, message);
    }

    @Step("Проверяем на соответствие текста")
    public static void assertEquals(String expected, String actual, String message) {
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual, message);
    }

    @Step("Проверяем на соответствие количества")
    public static void assertEquals(Integer expected, Integer actual, String message) {
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual, message);
    }
}
