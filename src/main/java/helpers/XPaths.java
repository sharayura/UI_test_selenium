/**
 * Класс для хранения xpath локаторов
 *
 * @author Sharapov Yuri
 */
package helpers;

public enum XPaths {
    CATALOG_XPATH("//div[@data-zone-name='catalog']"),
    CATEGORY1_XPATH("//span[text()='"),
    CATEGORY2_XPATH("//div[@data-baobab-name='linkSnippet']/a[text()='"),
    CATEGORY2_CHECK_XPATH("//span[@itemprop='name']"),
    PRICE_MIN_XPATH("//input[contains(@id, 'range-filter-field-glprice') and contains(@id, '_min')]"),
    PRICE_MAX_XPATH("//input[contains(@id, 'range-filter-field-glprice') and contains(@id, '_max')]"),
    NEW_ATTRIBUTE_XPATH("//div[@data-auto='SerpPage']"),
    FILTER_BUTTON_XPATH("//div[@data-zone-name='QuickFilterButton']"),
    SHOW_VENDORS_BUTTON_XPATH("//div[contains(@data-zone-data, 'Производитель')]//div[@data-baobab-name='showMoreFilters']"),
    VENDORS_BOX_XPATH("//div[contains(@data-zone-data, 'Производитель')]//span[contains(text(), '"),
    WAIT_FOOTER_XPATH("//div[@data-zone-name='footer']"),
    PAGINATION_XPATH("//div[@data-auto='pagination-page']"),
    ARTICLE_XPATH("//article[@data-autotest-id='product-snippet']"),
    ITEM_XPATH(".//h3[@data-auto='snippet-title-header']//span"),
    PRICE_XPATH(".//*[@data-auto='price-value' or @data-auto='snippet-price-current']"),
    SEARCH_INPUT_XPATH("//input[@id='header-search']"),
    SEARCH_BUTTON_XPATH("//button[@data-auto='search-button']");

    private final String xpath;

    XPaths(String xpath) {
        this.xpath = xpath;
    }

    public String getXpath() {
        return xpath;
    }
}
