/**
 * Основной класс тестов
 *
 * @author Sharapov Yuri
 */

package ru.yandex.market;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pages.MarketCategory;
import pages.MarketSearch;
import pages.MarketStart;

import java.util.List;


public class Tests extends BaseTest {

    @Feature("Тестирование Яндекс-Маркет")
    @DisplayName("Тестирование Яндекс-Маркет")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @MethodSource("helpers.DataProvider#provideTest")
    public void testMarket(String marketTitle, String category1, String category2,
                           String priceMin, String priceMax,
                           List<String> vendors, String minQuantity) {

        MarketStart marketStart = new MarketStart(chromeDriver, marketTitle, category1, category2);
        marketStart.getCategory1();
        marketStart.getCategory2();

        MarketCategory marketCategory = new MarketCategory(chromeDriver, priceMin, priceMax, vendors, minQuantity);
        marketCategory.setPrices();
        marketCategory.setVendors();
        marketCategory.filterResults();

        MarketSearch marketSearch = new MarketSearch(chromeDriver);
        marketSearch.searchCheck(marketCategory.firstItem());

    }
}
