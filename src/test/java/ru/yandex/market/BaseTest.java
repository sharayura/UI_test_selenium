package ru.yandex.market;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import static helpers.Properties.testProperties;

public class BaseTest {
    protected WebDriver chromeDriver;

    @BeforeEach
    public void before() {
        System.setProperty("webdriver.chrome.driver", System.getenv(testProperties.chromeDriver()));

        ChromeOptions chromeOptions = new ChromeOptions();
        if (testProperties.userDataEnabled().equals("true")) {
            chromeOptions.addArguments("user-data-dir=" + testProperties.userDataDir());
        }
        chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        chromeOptions.addArguments("start-maximized");

        chromeDriver = new ChromeDriver(chromeOptions);
    }

    @AfterEach
    public void after() {
        chromeDriver.quit();
    }
}
