package testcases;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import baseclass.BaseTest;
import pageclass.HomePage;
import pageclass.ProductPage;

@Listeners(baseclass.TestListener.class)
public class UITest extends BaseTest {

    // ---------------------------------------------------------
    // TEST CASE 1: Verify Alignment of Product Tiles
    // ---------------------------------------------------------
    @Test(priority = 1)
    public void verifyProductAlignment() {
        test.set(extent.createTest("UI & UX Testing-Test_Cases_21- Verify Product Grid Alignment"));
        HomePage home = new HomePage(getDriver());

        List<WebElement> cards = home.getAllProductCards();
        getTest().info("Found " + cards.size() + " product cards.");
        
        Assert.assertTrue(cards.size() >= 3, "Need at least 3 products to test alignment.");

        Point p1 = cards.get(0).getLocation();
        Point p2 = cards.get(1).getLocation();
        Point p3 = cards.get(2).getLocation();
        
        getTest().info("Card 1 Y-pos: " + p1.getY());
        getTest().info("Card 2 Y-pos: " + p2.getY());
        getTest().info("Card 3 Y-pos: " + p3.getY());
        
        boolean alignedY = Math.abs(p1.getY() - p2.getY()) <= 2 && Math.abs(p2.getY() - p3.getY()) <= 2;
        Assert.assertTrue(alignedY, "Product cards in the first row are not aligned horizontally!");

        int width1 = cards.get(0).getSize().getWidth();
        int gap1 = p2.getX() - (p1.getX() + width1);
        
        int width2 = cards.get(1).getSize().getWidth();
        int gap2 = p3.getX() - (p2.getX() + width2);
        
        getTest().info("Gap 1: " + gap1 + "px, Gap 2: " + gap2 + "px");
        
        Assert.assertTrue(Math.abs(gap1 - gap2) <= 5, "Spacing between product cards is inconsistent!");
        
        getTest().pass("Product tiles are aligned and spaced correctly.");
    }

    @Test(priority = 2)
    public void verifyActionButtonsVisibility() {
        test.set(extent.createTest("UI & UX Testing-Test_Cases_22- Verify Action Buttons Visibility"));
        HomePage home = new HomePage(getDriver());

        Assert.assertTrue(home.getLoginButton().isDisplayed(), "Log In button hidden!");
        Assert.assertTrue(home.getSignUpButton().isDisplayed(), "Sign Up button hidden!");
        
        home.clickFirstProduct();
        
        WebElement addToCart = getDriver().findElement(By.xpath("//a[text()='Add to cart']"));
        
        Assert.assertTrue(addToCart.isDisplayed(), "Add to Cart button is not visible!");
        Assert.assertTrue(addToCart.isEnabled(), "Add to Cart button is not clickable!");
        
        getTest().pass("All key action buttons are visible and enabled.");
    }

    @Test(priority = 3)
    public void verifyFontConsistency() {
        test.set(extent.createTest("UI & UX Testing-Test_Cases_23- Verify Font Consistency"));
        HomePage home = new HomePage(getDriver());
        ProductPage product = new ProductPage(getDriver());

        product.goToHome();

        String navFont = home.getLoginButton().getCssValue("font-family");
        getTest().info("Home Page Font: " + navFont);

        home.clickFirstProduct();

        WebElement title = getDriver().findElement(By.className("name"));
        String prodFont = title.getCssValue("font-family");
        getTest().info("Product Page Font: " + prodFont);

        Assert.assertEquals(navFont, prodFont, "Font family is inconsistent across pages!");
        
        getTest().pass("Font style is uniform across pages.");
    }

    @Test(priority = 4)
    public void verifyPopupStyling() {
        test.set(extent.createTest("UI & UX Testing-Test_Cases_24- Verify Modal/Popup Styling"));
        HomePage home = new HomePage(getDriver());
        ProductPage product = new ProductPage(getDriver());

        product.goToHome();

        home.openContactModal();
        getTest().info("Opened Contact Modal.");

        WebElement modalTitle = home.getContactModalTitle();

        String color = modalTitle.getCssValue("color");
        getTest().info("Modal Title Color: " + color);
        
        String fontWeight = modalTitle.getCssValue("font-weight"); // e.g., "500" or "700" or "bold"
        getTest().info("Modal Font Weight: " + fontWeight);
        
        Assert.assertNotNull(color, "Font color is missing!");
        
        boolean isBold = fontWeight.equals("bold") || Integer.parseInt(fontWeight) >= 500;
        Assert.assertTrue(isBold, "Modal title should be bold!");

        getTest().pass("Popup/Modal is readable and styled correctly.");
    }
}