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
    private final String priceMin;
    private final String priceMax;
    private final List<String> vendors;
    private final String minQuantity;

    public MarketCategory(WebDriver driver, String priceMin, String priceMax, List<String> vendors, String minQuantity) {
        this.driver = driver;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.vendors = vendors;
        this.minQuantity = minQuantity;
    }

    private static final String PRICE_MIN_XPATH = "//input[contains(@id, 'range-filter-field-glprice') and contains(@id, '_min')]";
    private static final String PRICE_MAX_XPATH = "//input[contains(@id, 'range-filter-field-glprice') and contains(@id, '_max')]";
    private static final String NEW_ATTRIBUTE_XPATH = "//div[@data-auto='SerpPage']";
    private static final String FILTER_BUTTON_XPATH = "//div[@data-zone-name='QuickFilterButton']";
    private static final String SHOW_VENDORS_BUTTON_XPATH = "//div[contains(@data-zone-data, 'Производитель')]//div[@data-baobab-name='showMoreFilters']";
    private static final String VENDORS_BOX_XPATH = "//div[contains(@data-zone-data, 'Производитель')]//span[contains(text(), '";
    private static final String WAIT_FOOTER_XPATH = "//div[@data-zone-name='footer']";
    private static final String PAGINATION_XPATH = "//div[@data-auto='pagination-page']";
    private static final String ARTICLE_XPATH = "//article[@data-autotest-id='product-snippet']";
    private static final String ITEM_XPATH = ".//h3[@data-auto='snippet-title-header']//span";
    private static final String PRICE_XPATH = ".//*[@data-auto='price-value' or @data-auto='snippet-price-current']";


    @Step("Устанавливаем цену от {priceMin} до {priceMax}")
    public void setPrices() {
        setPrice(priceMin, PRICE_MIN_XPATH, NEW_ATTRIBUTE_XPATH, driver);
        setPrice(priceMax, PRICE_MAX_XPATH, NEW_ATTRIBUTE_XPATH, driver);
        checkPriceFilter(priceMin, priceMax, FILTER_BUTTON_XPATH, driver);
    }

    @Step("Устанавливаем производителей")
    public void setVendors() {
        clickByXpath(SHOW_VENDORS_BUTTON_XPATH, driver);
        setVendor(vendors, VENDORS_BOX_XPATH, NEW_ATTRIBUTE_XPATH, driver);
    }

    @Step("Обработка результатов фильтрации")
    public void filterResults() {
        loadToEnd(WAIT_FOOTER_XPATH, driver);
        List<WebElement> pagination = driver.findElements(By.xpath(PAGINATION_XPATH));
        firstPageQuantityCheck(minQuantity, ARTICLE_XPATH, driver);

        Map<String, Integer> outFilterMap = new HashMap<>(checkResult(priceMin, priceMax, vendors,
                ARTICLE_XPATH, ITEM_XPATH, PRICE_XPATH, driver));

        WebElement firstPagination = null;
        if (!pagination.isEmpty()) {
            firstPagination = pagination.get(0);
            pagination.remove(0);
        }
        pagination.forEach(w -> {
            String oldAttribute = beforeSearch(NEW_ATTRIBUTE_XPATH, driver);
            w.click();
            afterSearch(oldAttribute, NEW_ATTRIBUTE_XPATH, driver);
            loadToEnd(WAIT_FOOTER_XPATH, driver);
            outFilterMap.putAll(checkResult(priceMin, priceMax, vendors,
                    ARTICLE_XPATH, ITEM_XPATH, PRICE_XPATH, driver));
        });
        Assertions.assertTrue(outFilterMap.isEmpty(), "Найдены товары, не попадающие под фильтры: " + outFilterMap);

        if (firstPagination != null) {
            String oldAttribute = beforeSearch(NEW_ATTRIBUTE_XPATH, driver);
            firstPagination.click();
            afterSearch(oldAttribute, NEW_ATTRIBUTE_XPATH, driver);
        }
    }

    @Step("Запоминаем первое наименование")
    public String firstItem() {
        return driver.findElements(By.xpath(ARTICLE_XPATH)).get(0).findElement(By.xpath(ITEM_XPATH)).getText();
    }

}
