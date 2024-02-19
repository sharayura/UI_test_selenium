package ru.yandex.market;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pages.MarketCategory;
import pages.MarketStart;


public class Tests extends BaseTest {


    @Feature("Тестирование Яндекс-Маркет")
    @DisplayName("Тестирование Яндекс-Маркет")
    @Test
    //  @ParameterizedTest(name = "{displayName}: {arguments}")
    //   @MethodSource("helpers.DataProvider#providerCheckingMoney")
    public void testMarket() {
        MarketStart marketStart = new MarketStart(chromeDriver);
        marketStart.getCategory1();
        marketStart.getCategory2();

        MarketCategory marketCategory = new MarketCategory(chromeDriver);
        marketCategory.setPrices();
        marketCategory.setVendors();
        marketCategory.filterResults();

    }
}
