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
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Класс вспомогательных шагов тестов
 *
 * @author Sharapov Yuri
 */

public class Steps {
    private static WebDriverWait wait;
    private static final String TIMEOUT = testProperties.timeoutSec();

    /**
     * Переходим на сайт, ожидаем появления тайтла
     *
     * @param url           адрес сайта
     * @param title         часть ожидаемого тайтла
     * @param currentDriver вебдрайвер
     */
    @Step("Переходим на сайт: {url}")
    public static void openSite(String url, String title, WebDriver currentDriver) {
        currentDriver.get(url);
        wait = new WebDriverWait(currentDriver, Long.parseLong(TIMEOUT));
        wait.until(ExpectedConditions.titleContains(title));
    }

    /**
     * Поиск элемента по xPath
     *
     * @param xPath         искомый локатор
     * @param currentDriver вебдрайвер
     * @return найденный элемент
     */
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

    /**
     * Клик элемента по xPath
     *
     * @param xPath         локатор элемента
     * @param currentDriver вебдрайвер
     */
    @Step("Кликаем элемент {xPath}")
    public static void clickByXpath(String xPath, WebDriver currentDriver) {
        WebElement element = findElementCustom(xPath, currentDriver);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    /**
     * Наводим курсор на элемент с текстом
     *
     * @param text          текст в элементе
     * @param xpath         основная часть локатора элемента
     * @param currentDriver вебдрайвер
     */
    @Step("Наводим курсор на элемент с текстом {text}")
    public static void moveCursor(String text, String xpath, WebDriver currentDriver) {
        Actions action = new Actions(currentDriver);
        WebElement category = findElementCustom(xpath + text + "']", currentDriver);
        action.moveToElement(category);
        action.perform();
    }

    /**
     * Проверяем что перешли во вторую категорию товаров
     *
     * @param category       название категории
     * @param categoryXpath  локатор элемента для выбора категории
     * @param category2Check локатор элемента, определяющего категорию товаров
     * @param currentDriver  вебдрайвер
     */
    @Step("Проверяем что перешли в категорию {category}")
    public static void getCategory(String category, String categoryXpath,
                                   String category2Check, WebDriver currentDriver) {
        clickByXpath(categoryXpath + category + "']", currentDriver);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(category2Check)));
        List<WebElement> categories = currentDriver.findElements(By.xpath(category2Check));
        Assertions.assertEquals(category, categories.get(categories.size() - 1).getText(),
                "Не удалось перейти в категорию " + category);
    }

    /**
     * Ввод значение в форму фильтрации цены товара
     *
     * @param price          значение цены
     * @param PriceXpath     локатор элемента для ввода цены
     * @param attributeXpath локатор аттрибута элемента для отслеживания обновления поиска
     * @param currentDriver  вебдрайвер
     */
    @Step("Вводим значение {price} в форму фильтра")
    public static void setPrice(String price, String PriceXpath, String attributeXpath, WebDriver currentDriver) {
        String oldAttribute = beforeSearch(attributeXpath, currentDriver);
        findElementCustom(PriceXpath, currentDriver).sendKeys(price);
        afterSearch(oldAttribute, attributeXpath, currentDriver);
    }

    /**
     * Сохранение аттрибута элемента для отслеживания обновления поиска
     *
     * @param attributeXpath локатор аттрибута элемента для отслеживания обновления поиска
     * @param currentDriver  вебдрайвер
     * @return текущий аттрибут элемента для отслеживания обновления поиска
     */
    public static String beforeSearch(String attributeXpath, WebDriver currentDriver) {
        return findElementCustom(attributeXpath, currentDriver).getAttribute("id");
    }

    /**
     * @param oldAttribute   старый аттрибут элемента для отслеживания обновления поиска
     * @param attributeXpath локатор аттрибута элемента для отслеживания обновления поиска
     * @param currentDriver  вебдрайвер
     */
    @Step("Ждем обновления результатов")
    public static void afterSearch(String oldAttribute, String attributeXpath, WebDriver currentDriver) {
        for (int i = 0; i < Integer.parseInt(TIMEOUT); i++) {
            waitOneSec();
            if (!oldAttribute.equals(beforeSearch(attributeXpath, currentDriver))) {
                return;
            }
        }
        fail("Результаты не обновились");
    }

    /**
     * Ожидание 1 секунду
     */
    public static void waitOneSec() {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 1000) {
        }
    }

    /**
     * Проверяем что фильтры цены установлены корректно
     *
     * @param minPrice          минимальная установленная цена
     * @param maxPrice          максимальная установленная цена
     * @param filterButtonXpath локатор кнопки фильтра цены
     * @param currentDriver     вебдрайвер
     */
    @Step("Проверяем наличие кнопки фильтра цены")
    public static void checkPriceFilter(String minPrice, String maxPrice, String filterButtonXpath, WebDriver currentDriver) {
        findElementCustom(filterButtonXpath, currentDriver);
        List<WebElement> filterButtons = currentDriver.findElements(By.xpath(filterButtonXpath));
        Assertions.assertTrue(filterButtons.stream().anyMatch(w -> w.getText().contains(minPrice + " — " + maxPrice)),
                "Фильтры цены установлены неверно");
    }

    /**
     * Устанавливаем фильтр производителей товаров
     *
     * @param vendors        Список производителей товаров
     * @param xpath          локатор поля для выбора производителей
     * @param attributeXpath локатор аттрибута элемента для отслеживания обновления поиска
     * @param currentDriver  вебдрайвер
     */
    @Step("Устанавливаем фильтр производителей")
    public static void setVendor(List<String> vendors, String xpath, String attributeXpath, WebDriver currentDriver) {
        vendors.forEach(v -> {
            String oldAttribute = beforeSearch(attributeXpath, currentDriver);
            clickByXpath(xpath + v + "')]", currentDriver);
            afterSearch(oldAttribute, attributeXpath, currentDriver);
        });
    }

    /**
     * Прокрутка страницы до конда с помощью javascript
     *
     * @param waitXpath     локатор элемента в конце страницы
     * @param currentDriver вебдрайвер
     */
    @Step("Подгружаем все результаты на странице")
    public static void loadToEnd(String waitXpath, WebDriver currentDriver) {
        ((JavascriptExecutor) currentDriver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        findElementCustom(waitXpath, currentDriver);
    }

    /**
     * Проверяем минимальное количество элементов на первой странице
     *
     * @param quantity      минимальное количество товаров на странице
     * @param xpath         локатор элемента товара
     * @param currentDriver вебдрайвер
     */
    @Step("Проверяем минимальное количество элементов на первой странице - {quantity}")
    public static void firstPageQuantityCheck(String quantity, String xpath, WebDriver currentDriver) {
        Assertions.assertTrue(Integer.parseInt(quantity) <= currentDriver.findElements(By.xpath(xpath)).size(),
                "Минимальное количество результатов (" + quantity + ") не найдено");

    }

    /**
     * проверка что найденные товары соответствуют фильтрам
     *
     * @param minPrice      минимальная цена товара
     * @param maxPrice      максимальная цена товара
     * @param vendors       список производителей
     * @param elementXpath  локатор элемента товара
     * @param itemXpath     локатор описания товара
     * @param priceXpath    локатор цены товара
     * @param currentDriver вебдрайвер
     * @return карта товаров, не соответствующих фильтрам
     */
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

    /**
     * Проверка описания товара на заданного производителя
     *
     * @param vendors список производителей
     * @param item    описание товара
     * @return соответствие условию
     */
    public static boolean checkItemVendor(List<String> vendors, String item) {
        for (String v : vendors) {
            if (item.toLowerCase().contains(v.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Поиск товара по описанию в результатах поиска
     *
     * @param item          заданное описание товара
     * @param elementXpath  локатор элемента товара
     * @param itemXpath     локатор описания товара
     * @param currentDriver вебдрайвер
     */
    @Step("Ищем товар в результатах поиска")
    public static void searchItem(String item, String elementXpath, String itemXpath, WebDriver currentDriver) {
        List<WebElement> elementList = currentDriver.findElements(By.xpath(elementXpath));
        for (WebElement e : elementList) {
            if (e.findElement(By.xpath(itemXpath)).getText().equals(item)) {
                return;
            }
        }
        fail("В результатах поиска нет товара " + item);
    }
}
