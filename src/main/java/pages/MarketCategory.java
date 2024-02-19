package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static steps.Steps.*;

public class MarketCategory {
    private final WebDriver driver;

    private static final String PRICE_MIN_XPATH = "//input[contains(@id, 'range-filter-field-glprice') and contains(@id, '_min')]";
    private static final String PRICE_MAX_XPATH = "//input[contains(@id, 'range-filter-field-glprice') and contains(@id, '_max')]";
    private static final String PRICE_MIN = "10000";
    private static final String PRICE_MAX = "40000";
    private static final String FILTER_BUTTON_XPATH = "//div[@data-zone-name='QuickFilterButton']";
    private static final String SHOW_VENDORS_BUTTON_XPATH = "//div[contains(@data-zone-data, 'Производитель')]//div[@data-baobab-name='showMoreFilters']";
    private static final String VENDORS_BOX_XPATH = "//div[contains(@data-zone-data, 'Производитель')]//span[contains(text(), '";
    private static final List<String> VENDORS = List.of("ASUS", "Lenovo");
    private static final String WAIT_RESULTS_XPATH = "//div[@data-test-id='virtuoso-item-list']";
    private static final String PAGINATION_XPATH = "//div[@data-auto='pagination-page']";
    private static final String ARTICLE_XPATH = "//article[@data-autotest-id='product-snippet']";
    private static final String FIRST_PAGE_QUANTITY = "12";


    public MarketCategory(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Устанавливаем цену от {PRICE_MIN} до {PRICE_MAX}")
    public void setPrices() {
        setPrice(PRICE_MIN, PRICE_MIN_XPATH, driver);
        setPrice(PRICE_MAX, PRICE_MAX_XPATH, driver);
        checkPriceFilter(PRICE_MIN, PRICE_MAX, FILTER_BUTTON_XPATH, driver);
    }

    @Step("Устанавливаем производителей")
    public void setVendors() {
        clickByXpath(SHOW_VENDORS_BUTTON_XPATH, driver);
        setVendor(VENDORS, VENDORS_BOX_XPATH, driver);
    }

    @Step("Обработка результатов фильтрации")
    public void filterResults() {
        loadToEnd(WAIT_RESULTS_XPATH, driver);
        List<WebElement> pagination = driver.findElements(By.xpath(PAGINATION_XPATH));
        firstPageQuantityCheck(FIRST_PAGE_QUANTITY, ARTICLE_XPATH, driver);

        WebElement firstPagination = null;
        if (!pagination.isEmpty()) {
            firstPagination = pagination.get(0);
            pagination.remove(0);
        }

        pagination.forEach(w -> {
            w.click();
            loadToEnd(WAIT_RESULTS_XPATH, driver);
        });
    }

}
