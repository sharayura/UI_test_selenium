package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static steps.Steps.*;

public class MarketStart {
    private final WebDriver driver;

    public MarketStart(WebDriver driver) {
        this.driver = driver;
    }

    private static final String MARKET_URL = "https://market.yandex.ru/";
    private static final String MARKET_TITLE = "Яндекс Маркет";
    private static final String CATALOG_XPATH = "//div[@data-zone-name='catalog']";
    private static final String CATEGORY1_TEXT = "Ноутбуки и компьютеры";
    private static final String CATEGORY1_XPATH = "//span[text()='";
    private static final String CATEGORY2_TEXT = "Ноутбуки";
    private static final String CATEGORY2_XPATH = "//div[@data-baobab-name='linkSnippet']/a[text()='";
    private static final String CATEGORY2_CHECK_XPATH = "//span[@itemprop='name']";



    public void getCategory1() {
        openSite(MARKET_URL, MARKET_TITLE, driver);
        clickByXpath(CATALOG_XPATH, driver);
        moveCursor(CATEGORY1_TEXT, CATEGORY1_XPATH, driver);
    }

    public void getCategory2() {
        getCategory(CATEGORY2_TEXT, CATEGORY2_XPATH, CATEGORY2_CHECK_XPATH, driver);
    }

}
