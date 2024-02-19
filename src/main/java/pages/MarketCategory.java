package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import static steps.Steps.*;

public class MarketCategory {
    private final WebDriver driver;

    private static final String PRICE_MIN_XPATH = "//input[contains(@id, 'range-filter-field-glprice') and contains(@id, '_min')]";
    private static final String PRICE_MAX_XPATH = "//input[contains(@id, 'range-filter-field-glprice') and contains(@id, '_max')]";
    private static final String PRICE_MIN = "10000";
    private static final String PRICE_MAX = "60000";
    private static final String FILTER_BUTTON_XPATH = "//div[@data-zone-name='QuickFilterButton']";
    private static final String SHOW_VENDORS_BUTTON_XPATH = "//div[contains(@data-zone-data, 'Производитель')]//div[@data-baobab-name='showMoreFilters']";



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

    }
}
