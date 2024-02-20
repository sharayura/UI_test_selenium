package steps;

import helpers.Assertions;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static helpers.Properties.testProperties;


public class Steps {
    private static WebDriverWait wait;
    private static final String TIMEOUT = testProperties.timeoutSec();

    @Step("Переходим на сайт: {url}")
    public static void openSite(String url, String title, WebDriver currentDriver) {
        currentDriver.get(url);
        wait = new WebDriverWait(currentDriver, Long.parseLong(TIMEOUT));
        wait.until(ExpectedConditions.titleContains(title));
    }

    @Step("Поиск элемента по xPath")
    public static WebElement findElementCustom(String xPath, WebDriver currentDriver) {
        long timeout = Long.parseLong(TIMEOUT) * 1000;
        long start = System.currentTimeMillis();
        List<WebElement> elements = new ArrayList<>();
        while (System.currentTimeMillis() - start < timeout) {
            elements = currentDriver.findElements(By.xpath(xPath));
            if (!elements.isEmpty()) {
                break;
            }
        }
        Assertions.assertFalse(elements.isEmpty(), "Элемент не найден по xPath " + xPath);
        return elements.get(0);
    }

    @Step("Кликаем элемент {xPath}")
    public static void clickByXpath(String xPath, WebDriver currentDriver) {
        WebElement element = findElementCustom(xPath, currentDriver);
        // wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    @Step("Наводим курсор на элемент с текстом {text}")
    public static void moveCursor(String text, String xpath, WebDriver currentDriver) {
        Actions action = new Actions(currentDriver);
        WebElement category = findElementCustom(xpath + text + "']", currentDriver);
        action.moveToElement(category);
        action.perform();
    }

    @Step("Проверяем что перешли в категорию {category}")
    public static void getCategory(String category, String categoryXpath,
                                   String category2Check, WebDriver currentDriver) {
        clickByXpath(categoryXpath + category + "']", currentDriver);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(category2Check)));
        List<WebElement> categories = currentDriver.findElements(By.xpath(category2Check));
        Assertions.assertEquals(category, categories.get(categories.size() - 1).getText(),
                "Не удалось перейти в категорию " + category);
    }

    @Step("Вводим значение {price} в форму фильтра")
    public static void setPrice(String price, String PriceXpath, String attributeXpath, WebDriver currentDriver) {
        String oldAttribute = beforeSearch(attributeXpath, currentDriver);
        findElementCustom(PriceXpath, currentDriver).sendKeys(price);
        afterSearch(oldAttribute, attributeXpath, currentDriver);
    }

    public static String beforeSearch(String attributeXpath, WebDriver currentDriver) {
        return findElementCustom(attributeXpath, currentDriver).getAttribute("id");
    }

    @Step("Ждем обновления результатов")
    public static void afterSearch(String oldAttribute, String attributeXpath, WebDriver currentDriver) {
        for (int i = 0; i < Integer.parseInt(TIMEOUT); i++) {
            waitOneSec();
            if (!oldAttribute.equals(beforeSearch(attributeXpath, currentDriver))) {
                return;
            }
        }
        Assertions.assertTrue(false, "Результаты не обновились");
    }

    public static void waitOneSec() {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 1000) {
        }
    }

    @Step("Проверяем наличие кнопки фильтра цены")
    public static void checkPriceFilter(String minPrice, String maxPrice, String filterButtonXpath, WebDriver currentDriver) {
        findElementCustom(filterButtonXpath, currentDriver);
        List<WebElement> filterButtons = currentDriver.findElements(By.xpath(filterButtonXpath));
        Assertions.assertTrue(filterButtons.stream().anyMatch(w -> w.getText().contains(minPrice + " — " + maxPrice)),
                "Фильтры цены установлены неверно");
    }

    @Step("Устанавливаем фильтр производителей")
    public static void setVendor(List<String> vendors, String xpath, String attributeXpath, WebDriver currentDriver) {
        vendors.forEach(v -> {
            String oldAttribute = beforeSearch(attributeXpath, currentDriver);
            clickByXpath(xpath + v + "')]", currentDriver);
            afterSearch(oldAttribute, attributeXpath, currentDriver);
        });
    }

    @Step("Подгружаем все результаты на странице")
    public static void loadToEnd(String waitXpath, WebDriver currentDriver) {
        ((JavascriptExecutor) currentDriver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        findElementCustom(waitXpath, currentDriver);
    }

    @Step("Проверяем минимальное количество элементов на первой странице - {quantity}")
    public static void firstPageQuantityCheck(String quantity, String xpath, WebDriver currentDriver) {
        Assertions.assertTrue(Integer.parseInt(quantity) <= currentDriver.findElements(By.xpath(xpath)).size(),
                "Минимальное количество результатов (" + quantity + ") не найдено");

    }

    @Step("Проверяем результаты на соответствие фильтрам")
    public static Map<String, Integer> checkResult(String minPrice, String maxPrice, List<String> vendors,
                                                   String elementXpath, String itemXpath, String priceXpath,
                                                   WebDriver currentDriver) {
        List<WebElement> elementList = currentDriver.findElements(By.xpath(elementXpath));
        Map<String, Integer> checkResultMap = new HashMap<>();
        elementList.forEach(e -> {
            List<WebElement> items = e.findElements(By.xpath(itemXpath));
            Assertions.assertFalse(items.isEmpty(), "Описание товара не найдено");
            String item = items.get(0).getText();
            List<WebElement> prices = e.findElements(By.xpath(priceXpath));
            Assertions.assertFalse(prices.isEmpty(), "Цена товара не найдена " + item);
            String price = prices.get(0).getText();
            price = price.replaceAll("[^0-9]", "");
            Assertions.assertFalse(prices.isEmpty(), "Цена товара некорректна " + item);
            Integer priceInt = Integer.parseInt(price);

            if (priceInt < Integer.parseInt(minPrice) || priceInt > Integer.parseInt(maxPrice)
                || !checkItemVendor(vendors, item)) {
                checkResultMap.put(item, priceInt);
            }
        });
        return checkResultMap;
    }

    public static boolean checkItemVendor(List<String> vendors, String item) {
        for (String v : vendors) {
            if (item.toLowerCase().contains(v.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @Step("Ищем товар в результатах поиска")
    public static void searchItem(String item, String elementXpath, String itemXpath, WebDriver currentDriver) {
        List<WebElement> elementList = currentDriver.findElements(By.xpath(elementXpath));
        for (WebElement e : elementList) {
                if (e.findElement(By.xpath(itemXpath)).getText().equals(item)) {
                    return;
                }
            }
        Assertions.assertTrue(false, "В результатах поиска нет товара " + item);
    }
}
