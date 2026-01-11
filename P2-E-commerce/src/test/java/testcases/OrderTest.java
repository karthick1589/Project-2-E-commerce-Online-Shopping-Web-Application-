package testcases;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import baseclass.BaseTest;
import pageclass.CartPage;
import pageclass.HomePage;
import pageclass.ProductPage;
import utilityclass.ExcelUtils;

@Listeners(baseclass.TestListener.class)
public class OrderTest extends BaseTest {

    private String testUsername;
    private String testPassword;

    @BeforeClass
    public void setupData() {
        String[] user = ExcelUtils.getRandomUser("CreatedUsers");
        if (user != null) {
            testUsername = user[0];
            testPassword = user[1];
            System.out.println("--- ORDER TEST SETUP: User selected: " + testUsername + " ---");
        } else {
            Assert.fail("Excel 'CreatedUsers' sheet is empty! Cannot run Order Tests.");
        }
    }

    public void performLoginAndAddProduct(HomePage home, ProductPage product) {
        home.clickLogin();
        home.login(testUsername, testPassword);
        home.getWelcomeText(); // Wait for login
        
        // Add a cheap item to ensure cart is not empty
        home.clickProduct("Nexus 6");
        product.clickAddToCart();
        product.getAlertTextAndAccept();
        product.goToCart();
    }

    @Test(priority = 1)
    public void verifyPlaceOrderWithValidDetails() {
        test.set(extent.createTest("Order Functionality-Test_Cases_13- Place Order with Valid Details"));
        HomePage home = new HomePage(getDriver());
        ProductPage product = new ProductPage(getDriver());
        CartPage cart = new CartPage(getDriver());

        performLoginAndAddProduct(home, product);

        cart.clickPlaceOrder();
        getTest().info("Clicked Place Order button.");

        cart.fillOrderDetails("Karthick", "India", "Chennai", "1234-5678-9012-3456", "12", "2026");
        getTest().info("Filled order details.");

        cart.clickPurchase();

        String title = cart.getSuccessTitle();
        getTest().info("Success Title: " + title);
        
        Assert.assertEquals(title, "Thank you for your purchase!", "Order success message mismatch!");
        
        cart.clickOK();
        getTest().pass("Order placed successfully.");
    }

    @Test(priority = 2)
    public void verifyPlaceOrderWithEmptyForm() {
        test.set(extent.createTest("Order Functionality-Test_Cases_14- Place Order with Empty Form"));
        HomePage home = new HomePage(getDriver());
        ProductPage product = new ProductPage(getDriver());
        CartPage cart = new CartPage(getDriver());

        performLoginAndAddProduct(home, product);

        cart.clickPlaceOrder();
        getTest().info("Opened Order Modal.");

        cart.clickPurchase();
        
        String alertMsg = cart.getAlertTextAndAccept();
        getTest().info("Alert Text: " + alertMsg);
        
        Assert.assertEquals(alertMsg, "Please fill out Name and Creditcard.", "Alert mismatch!");
        getTest().pass("Empty form validation passed.");
    }

    @Test(priority = 3)
    public void verifyOrderConfirmationPopup() {
        test.set(extent.createTest("Order Functionality-Test_Cases_15- Verify Confirmation Popup Details"));
        HomePage home = new HomePage(getDriver());
        ProductPage product = new ProductPage(getDriver());
        CartPage cart = new CartPage(getDriver());

        performLoginAndAddProduct(home, product);

        cart.clickPlaceOrder();
        cart.fillOrderDetails("Test User", "USA", "NY", "987654321", "01", "2027");
        cart.clickPurchase();

        String title = cart.getSuccessTitle();
        Assert.assertEquals(title, "Thank you for your purchase!");

        String details = cart.getSuccessDetails();
        getTest().info("Confirmation Details:\n" + details);

        boolean hasId = details.contains("Id:");
        boolean hasAmount = details.contains("Amount:");
        
        Assert.assertTrue(hasId, "Order ID missing in confirmation!");
        Assert.assertTrue(hasAmount, "Amount missing in confirmation!");
        
        getTest().pass("Confirmation popup contains Order ID and Amount.");
        cart.clickOK();
    }
}