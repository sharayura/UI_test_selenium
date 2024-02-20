package pages;

import helpers.Assertions;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static steps.Steps.*;

public class MarketCategory {
    private final WebDriver driver;

    private static final String PRICE_MIN_XPATH = "//input[contains(@id, 'range-filter-field-glprice') and contains(@id, '_min')]";
    private static final String PRICE_MAX_XPATH = "//input[contains(@id, 'range-filter-field-glprice') and contains(@id, '_max')]";
    private static final String PRICE_MIN = "10000";
    private static final String PRICE_MAX = "70000";
    private static final String NEW_ATTRIBUTE_XPATH = "//div[@data-auto='SerpPage']";
    private static final String FILTER_BUTTON_XPATH = "//div[@data-zone-name='QuickFilterButton']";
    private static final String SHOW_VENDORS_BUTTON_XPATH = "//div[contains(@data-zone-data, 'Производитель')]//div[@data-baobab-name='showMoreFilters']";
    private static final String VENDORS_BOX_XPATH = "//div[contains(@data-zone-data, 'Производитель')]//span[contains(text(), '";
    private static final List<String> VENDORS = List.of("ASUS", "Lenovo");
    private static final String WAIT_FOOTER_XPATH = "//div[@data-zone-name='footer']";
    private static final String PAGINATION_XPATH = "//div[@data-auto='pagination-page']";
    private static final String ARTICLE_XPATH = "//article[@data-autotest-id='product-snippet']";
    private static final String ITEM_XPATH = ".//h3[@data-auto='snippet-title-header']//span";
    private static final String PRICE_XPATH = ".//*[@data-auto='price-value' or @data-auto='snippet-price-current']";
    private static final String FIRST_PAGE_QUANTITY = "12";


    public MarketCategory(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Устанавливаем цену от {PRICE_MIN} до {PRICE_MAX}")
    public void setPrices() {
        setPrice(PRICE_MIN, PRICE_MIN_XPATH, NEW_ATTRIBUTE_XPATH, driver);
        setPrice(PRICE_MAX, PRICE_MAX_XPATH, NEW_ATTRIBUTE_XPATH, driver);
        checkPriceFilter(PRICE_MIN, PRICE_MAX, FILTER_BUTTON_XPATH, driver);
    }

    @Step("Устанавливаем производителей")
    public void setVendors() {
        clickByXpath(SHOW_VENDORS_BUTTON_XPATH, driver);
        setVendor(VENDORS, VENDORS_BOX_XPATH, NEW_ATTRIBUTE_XPATH, driver);
    }

    @Step("Обработка результатов фильтрации")
    public void filterResults() {
        loadToEnd(WAIT_FOOTER_XPATH, driver);
        List<WebElement> pagination = driver.findElements(By.xpath(PAGINATION_XPATH));
        firstPageQuantityCheck(FIRST_PAGE_QUANTITY, ARTICLE_XPATH, driver);

        Map<String, Integer> outFilterMap = new HashMap<>(checkResult(PRICE_MIN, PRICE_MAX, VENDORS,
                ARTICLE_XPATH, ITEM_XPATH, PRICE_XPATH, driver));

        WebElement firstPagination = null;
        if (!pagination.isEmpty()) {
            firstPagination = pagination.get(0);
            pagination.remove(0);
        }
        pagination.forEach(w -> {
            w.click();
            loadToEnd(WAIT_FOOTER_XPATH, driver);
            outFilterMap.putAll(checkResult(PRICE_MIN, PRICE_MAX, VENDORS,
                    ARTICLE_XPATH, ITEM_XPATH, PRICE_XPATH, driver));
        });

        if (firstPagination != null) {
            firstPagination.click();
        }
        Assertions.assertTrue(outFilterMap.isEmpty(), "Найдены товары, не попадающие под фильтры: " + outFilterMap);
        if (firstPagination != null) {
            firstPagination.click();
        }
    }

    @Step("Запоминаем первое наименование")
    public String firstItem() {
        return driver.findElements(By.xpath(ARTICLE_XPATH)).get(0).findElement(By.xpath(ITEM_XPATH)).getText();
    }

}
