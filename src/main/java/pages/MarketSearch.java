/**
 * Класс PO для поиска по названию товара
 *
 * @author Sharapov Yuri
 */

package pages;

import helpers.XPaths;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static steps.Steps.*;

public class MarketSearch {
    private final WebDriver driver;

    private static final String SEARCH_INPUT_XPATH = XPaths.SEARCH_INPUT_XPATH.getXpath();
    private static final String SEARCH_BUTTON_XPATH = XPaths.SEARCH_BUTTON_XPATH.getXpath();
    private static final String ARTICLE_XPATH = XPaths.ARTICLE_XPATH.getXpath();
    private static final String ITEM_XPATH = XPaths.ITEM_XPATH.getXpath();
    private static final String NEW_ATTRIBUTE_XPATH = XPaths.NEW_ATTRIBUTE_XPATH.getXpath();

    public MarketSearch(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Проверяем поиск товара")
    public void searchCheck(String item) {
        findElementCustom(SEARCH_INPUT_XPATH, driver).sendKeys(item);
        String oldAttribute = beforeSearch(NEW_ATTRIBUTE_XPATH, driver);
        findElementCustom(SEARCH_BUTTON_XPATH, driver).click();
        afterSearch(oldAttribute, NEW_ATTRIBUTE_XPATH, driver);
        searchItem(item, ARTICLE_XPATH, ITEM_XPATH, driver);
    }
}
