package testcases;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import baseclass.BaseTest;
import pageclass.HomePage;
import pageclass.ProductPage;

@Listeners(baseclass.TestListener.class)
public class NavigationTest extends BaseTest {

    @Test(priority = 1)
    public void verifyBrowseCategories() {
        test.set(extent.createTest("Product Browsing and Navigation-Test_Cases_16-Browse Product Categories"));
        HomePage home = new HomePage(getDriver());

         home.clickCategoryLaptops();
        getTest().info("Clicked on 'Laptops' category.");

        List<String> laptops = home.getProductList();
        getTest().info("Laptops displayed: " + laptops);
        
        Assert.assertTrue(laptops.stream().anyMatch(n -> n.contains("Sony") || n.contains("MacBook") || n.contains("Dell")), 
                "Laptops category did not display expected laptop brands!");

        home.clickCategoryMonitors();
        getTest().info("Clicked on 'Monitors' category.");
        
        List<String> monitors = home.getProductList();
        getTest().info("Monitors displayed: " + monitors);
        
        Assert.assertTrue(monitors.stream().anyMatch(n -> n.contains("Apple monitor") || n.contains("ASUS")), 
                "Monitors category did not display expected monitor brands!");
                
        getTest().pass("Category filtering works correctly.");
    }

    @Test(priority = 2)
    public void verifyViewProductDetails() {
        test.set(extent.createTest("Product Browsing and Navigation-Test_Cases_17-View Product Details"));
        HomePage home = new HomePage(getDriver());
        ProductPage product = new ProductPage(getDriver());

        home.clickHome();

        String clickedName = home.clickFirstProduct();
        getTest().info("Clicked on product: " + clickedName);
        
        if (clickedName == null) Assert.fail("No products found on homepage to click!");

        String detailName = product.getProductTitle();
        getTest().info("Product Detail Page Title: " + detailName);

        Assert.assertEquals(detailName, clickedName, "Product Detail Page title mismatch!");
        Assert.assertTrue(product.isPriceDisplayed(), "Price tag is missing!");
        Assert.assertTrue(product.isDescriptionDisplayed(), "Product description is missing!");
        Assert.assertTrue(product.isImageDisplayed(), "Product image is missing!");

        getTest().pass("Product Detail Page contains correct info.");
    }

    @Test(priority = 3)
    public void verifyHomeNavigation() {
        test.set(extent.createTest("Product Browsing and Navigation-Test_Cases_18-Verify Home Link"));
        HomePage home = new HomePage(getDriver());
        ProductPage product = new ProductPage(getDriver());

        home.clickFirstProduct();
        getTest().info("Navigated to a product detail page.");
        
        product.goToHome();
        getTest().info("Clicked 'Home' link from navbar.");

        boolean isGridVisible = home.isHomeGridDisplayed();
        Assert.assertTrue(isGridVisible, "Product Grid not visible after clicking Home!");

        List<String> items = home.getProductList();
        Assert.assertTrue(items.size() > 1, "Homepage should display multiple products.");
        
        getTest().pass("Successfully navigated back to Home.");
    }
    
    @Test(priority = 4)
    public void verifyNavbarLinks() {
        test.set(extent.createTest("Product Browsing and Navigation-Test_Cases_19-Navbar Links (Home, Cart, Contact)"));
        HomePage home = new HomePage(getDriver());
        ProductPage product = new ProductPage(getDriver()); // Reusing navigation methods
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));

        product.goToHome();
        Assert.assertTrue(home.isHomeGridDisplayed(), "Home Page not displayed!");
        getTest().info("Home Link verified.");

        product.goToCart();
        wait.until(ExpectedConditions.urlContains("cart.html"));
        String currentUrl = getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("cart.html"), "Failed to navigate to Cart! URL: " + currentUrl);
        getTest().info("Cart Link verified.");

        By contactLink = By.linkText("Contact");
        By contactModal = By.id("exampleModal"); // The Contact popup ID

        wait.until(ExpectedConditions.elementToBeClickable(contactLink)).click();
        
        boolean isModalVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(contactModal)).isDisplayed();
        Assert.assertTrue(isModalVisible, "Contact Modal did not open!");
        getTest().info("Contact Link verified (Modal opened).");
        
        getDriver().findElement(By.xpath("//div[@id='exampleModal']//button[text()='Close']")).click();
        
        getTest().pass("All Navbar links (Home, Cart, Contact) work correctly.");
    }
}