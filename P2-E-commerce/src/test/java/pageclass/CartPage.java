package pageclass;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartPage {
	
	WebDriver driver;
    WebDriverWait wait;

    By tableRows = By.xpath("//tbody[@id='tbodyid']/tr");
    By totalPriceText = By.id("totalp");
    By placeOrderBtn = By.xpath("//button[text()='Place Order']");
    
    By orderModal = By.id("orderModal");
    By nameField = By.id("name");
    By countryField = By.id("country");
    By cityField = By.id("city");
    By cardField = By.id("card");
    By monthField = By.id("month");
    By yearField = By.id("year");
    
    By purchaseBtn = By.xpath("//button[text()='Purchase']");
    
    By successAlert = By.cssSelector(".sweet-alert");
    By successTitle = By.cssSelector(".sweet-alert h2"); // "Thank you for your purchase!"
    By successDetails = By.cssSelector(".sweet-alert p"); // Contains Id, Amount, etc.
    By okBtn = By.xpath("//button[text()='OK']");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public List<String> getProductTitles() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(placeOrderBtn));
        List<WebElement> rows = driver.findElements(tableRows);
        List<String> titles = new ArrayList<>();
        
        for (WebElement row : rows) {
            // Title is in the 2nd column (td[2])
            List<WebElement> cols = row.findElements(By.tagName("td"));
            if (cols.size() >= 2) {
                titles.add(cols.get(1).getText());
            }
        }
        return titles;
    }
    
    public int getProductCount(String productName) {
        List<String> allProducts = getProductTitles();
        int count = 0;
        for (String title : allProducts) {
            if (title.equals(productName)) {
                count++;
            }
        }
        return count;
    }

    public int getTotalPrice() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(totalPriceText));
            String priceStr = driver.findElement(totalPriceText).getText();
            return Integer.parseInt(priceStr);
        } catch (Exception e) {
            return 0;
        }
    }

    public void deleteProduct(String productName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(tableRows));
        List<WebElement> rows = driver.findElements(tableRows);
        
        for (WebElement row : rows) {
            if (row.getText().contains(productName)) {
                
                WebElement deleteLink = row.findElement(By.xpath(".//td[4]/a"));
                deleteLink.click();
                
                wait.until(ExpectedConditions.stalenessOf(row));
                break;
            }
        }
    }
    
    public void clickPlaceOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(placeOrderBtn)).click();
    }

    public void fillOrderDetails(String name, String country, String city, String card, String month, String year) {

        wait.until(ExpectedConditions.visibilityOfElementLocated(nameField));
        
        driver.findElement(nameField).sendKeys(name);
        driver.findElement(countryField).sendKeys(country);
        driver.findElement(cityField).sendKeys(city);
        driver.findElement(cardField).sendKeys(card);
        driver.findElement(monthField).sendKeys(month);
        driver.findElement(yearField).sendKeys(year);
    }

    public void clickPurchase() {
        wait.until(ExpectedConditions.elementToBeClickable(purchaseBtn)).click();
    }

    public String getSuccessTitle() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(successAlert));
        return driver.findElement(successTitle).getText();
    }

    public String getSuccessDetails() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(successDetails));
        return driver.findElement(successDetails).getText();
    }

    public void clickOK() {
        wait.until(ExpectedConditions.elementToBeClickable(okBtn)).click();
    }
    
    public String getAlertTextAndAccept() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            String text = alert.getText();
            alert.accept();
            return text;
        } catch (Exception e) {
            return "No Alert Found";
        }
    }
}
