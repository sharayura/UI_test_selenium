package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import static steps.Steps.*;

public class MarketSearch {
    private final WebDriver driver;

    private static final String SEARCH_INPUT_XPATH = "//input[@id='header-search']";
    private static final String SEARCH_BUTTON_XPATH = "//button[@data-auto='search-button']";
    private static final String ARTICLE_XPATH = "//article[@data-autotest-id='product-snippet']";
    private static final String ITEM_XPATH = ".//h3[@data-auto='snippet-title-header']//span";
    private static final String NEW_ATTRIBUTE_XPATH = "//div[@data-auto='SerpPage']";

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
