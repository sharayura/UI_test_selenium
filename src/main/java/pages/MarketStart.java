/**
 * Класс PO начальной страницы
 *
 * @author Sharapov Yuri
 */
package pages;

import helpers.XPaths;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static steps.Steps.*;
import static helpers.Properties.testProperties;

public class MarketStart {
    private final WebDriver driver;
    private final String marketTitle;
    private final String category1Text;
    private final String category2Text;

    public MarketStart(WebDriver driver, String marketTitle, String category1Text, String category2Text) {
        this.driver = driver;
        this.marketTitle = marketTitle;
        this.category1Text = category1Text;
        this.category2Text = category2Text;
    }

    private static final String MARKET_URL = testProperties.marketUrl();
    private static final String CATALOG_XPATH = XPaths.CATALOG_XPATH.getXpath();
    private static final String CATEGORY1_XPATH = XPaths.CATEGORY1_XPATH.getXpath();
    private static final String CATEGORY2_XPATH = XPaths.CATEGORY2_XPATH.getXpath();
    private static final String CATEGORY2_CHECK_XPATH = XPaths.CATEGORY2_CHECK_XPATH.getXpath();


    @Step("Открываем категорию {category1Text}")
    public void getCategory1() {
        openSite(MARKET_URL, marketTitle, driver);
        clickByXpath(CATALOG_XPATH, driver);
        moveCursor(category1Text, CATEGORY1_XPATH, driver);
    }

    @Step("Открываем категорию {category2Text}")
    public void getCategory2() {
        getCategory(category2Text, CATEGORY2_XPATH, CATEGORY2_CHECK_XPATH, driver);
    }

}
