package steps;

import helpers.Assertions;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;


public class Steps {
    private static WebDriverWait wait;

    @Step("Переходим на сайт: {url}")
    public static void openSite(String url, String title, WebDriver currentDriver) {
        currentDriver.get(url);
        wait = new WebDriverWait(currentDriver, 30);
        wait.until(ExpectedConditions.titleContains(title));
    }

    @Step("Кликаем элемент {xPath}")
    public static void clickByXpath(String xPath, WebDriver currentDriver) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
        currentDriver.findElement(By.xpath(xPath)).click();
    }

    @Step("Наводим курсор на элемент с текстом {text}")
    public static void moveCursor(String text, String xpath, WebDriver currentDriver) {
        Actions action = new Actions(currentDriver);
        By by = By.xpath(xpath + text + "']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        WebElement category = currentDriver.findElement(by);
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
    public static void setPrice(String price, String xpath, WebDriver currentDriver) {
        clickByXpath(xpath, currentDriver);
        currentDriver.findElement(By.xpath(xpath)).sendKeys(price);
    }

    @Step("Проверяем наличие кнопки фильтра цены")
    public static void checkPriceFilter(String minPrice, String maxPrice, String filterButtonXpath, WebDriver currentDriver) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(filterButtonXpath)));
        List<WebElement> filterButtons = currentDriver.findElements(By.xpath(filterButtonXpath));
        Assertions.assertTrue(filterButtons.stream().anyMatch((w) -> w.getText().contains(minPrice + " — " + maxPrice)),
                "Фильтры цены установлены неверно");

    }


}
