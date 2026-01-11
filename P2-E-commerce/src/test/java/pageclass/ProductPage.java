package pageclass;

import java.time.Duration;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductPage {
	
	WebDriver driver;
    WebDriverWait wait;

    By addToCartBtn = By.xpath("//a[text()='Add to cart']");
    By cartLink = By.id("cartur");
    By homeLink = By.partialLinkText("Home");
    
    By productTitle = By.className("name");
    By productPrice = By.className("price-container");
    By productDesc = By.id("more-information");
    By productImage = By.cssSelector(".product-image img, .item img");

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void clickAddToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn)).click();
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

    public void goToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
    }
    
    public void goToHome() {
        wait.until(ExpectedConditions.elementToBeClickable(homeLink)).click();
    }
    
    public String getProductTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(productTitle)).getText();
    }

    public boolean isPriceDisplayed() {
        return driver.findElement(productPrice).isDisplayed();
    }

    public boolean isDescriptionDisplayed() {
        return driver.findElement(productDesc).isDisplayed();
    }
    
    public boolean isImageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(productImage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

}
