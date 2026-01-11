package testcases;

import java.util.List;

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
public class CartTest extends BaseTest {
	
	private String testUsername;
    private String testPassword;
	
	@BeforeClass
    public void setupData() {

        String[] user = ExcelUtils.getRandomUser("CreatedUsers");
        
        if (user != null) {
            testUsername = user[0];
            testPassword = user[1];
            System.out.println("--- CART TEST SETUP: User selected: " + testUsername + " ---");
        } else {
            Assert.fail("Excel 'CreatedUsers' sheet is empty! Cannot run Cart Tests.");
        }
    }

    public void performLogin(HomePage home) {
        home.clickLogin();
        
        home.login(testUsername, testPassword);
        
        home.getWelcomeText(); 
    }

	@Test(priority = 1)
	public void verifyAddProductToCart() {
		test.set(extent.createTest("Cart Functionality-Test_Cases_09- Add Product to Cart"));
		HomePage home = new HomePage(getDriver());
		ProductPage product = new ProductPage(getDriver());

		performLogin(home); // Uses new robust login

		String productName = "Samsung galaxy s6";
		home.clickProduct(productName);
		getTest().info("Selected product: " + productName);

		product.clickAddToCart();

		String alertMsg = product.getAlertTextAndAccept();
		getTest().info("Alert Text: " + alertMsg);

		Assert.assertEquals(alertMsg, "Product added.", "Alert message mismatch!");
		getTest().pass("Product added successfully.");
	}

	@Test(priority = 2)
	public void verifyViewCart() {
		test.set(extent.createTest("Cart Functionality-Test_Cases_10- View Cart Content"));
		HomePage home = new HomePage(getDriver());
		ProductPage product = new ProductPage(getDriver());
		CartPage cart = new CartPage(getDriver());

		performLogin(home);

		home.clickProduct("Samsung galaxy s6");
		product.clickAddToCart();
		product.getAlertTextAndAccept();

		product.goToCart();

		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}

		List<String> items = cart.getProductTitles();
		boolean isItemPresent = items.contains("Samsung galaxy s6");
		Assert.assertTrue(isItemPresent, "Samsung galaxy s6 should be in the cart!");

		getTest().pass("Product visible in cart.");
	}

	@Test(priority = 3)
	public void verifyAddMultipleProducts() {
		test.set(extent.createTest("Cart Functionality-Test_Cases_11- Add Multiple Products"));
		HomePage home = new HomePage(getDriver());
		ProductPage product = new ProductPage(getDriver());
		CartPage cart = new CartPage(getDriver());

		performLogin(home);

		home.clickProduct("Nokia lumia 1520");
		product.clickAddToCart();
		product.getAlertTextAndAccept();
		product.goToHome();

		home.clickProduct("Nexus 6");
		product.clickAddToCart();
		product.getAlertTextAndAccept();

		product.goToCart();

		List<String> items = null;
		boolean nokiaFound = false;
		boolean nexusFound = false;

		for (int i = 0; i < 3; i++) {
			try {
				Thread.sleep(1500);
			} catch (Exception e) {
			}

			items = cart.getProductTitles();
			nokiaFound = items.contains("Nokia lumia 1520");
			nexusFound = items.contains("Nexus 6");

			if (nokiaFound && nexusFound)
				break; // Found both! Exit loop.
			getTest().info("Attempt " + (i + 1) + ": Items not fully populated yet...");
		}

		getTest().info("Final Items in Cart: " + items);

		Assert.assertTrue(nokiaFound, "Nokia lumia 1520 missing from cart!");
		Assert.assertTrue(nexusFound, "Nexus 6 missing from cart!");

		int total = cart.getTotalPrice();
		Assert.assertTrue(total > 0, "Total price should be > 0");
		getTest().pass("Multiple products added successfully.");
	}

	@Test(priority = 4)
	public void verifyRemoveProduct() {
		test.set(extent.createTest("Cart Functionality-Test_Cases_12- Remove Product"));
		HomePage home = new HomePage(getDriver());
		ProductPage product = new ProductPage(getDriver());
		CartPage cart = new CartPage(getDriver());

		performLogin(home);

		String itemToDelete = "Samsung galaxy s6";
		home.clickProduct(itemToDelete);
		product.clickAddToCart();
		product.getAlertTextAndAccept();
		product.goToCart();

		try {
			Thread.sleep(2000);
		} catch (Exception e) {
		}

		int initialCount = cart.getProductCount(itemToDelete);
		getTest().info("Count of '" + itemToDelete + "' before delete: " + initialCount);

		cart.deleteProduct(itemToDelete);
		getTest().info("Clicked delete for one instance of: " + itemToDelete);

		try {
			Thread.sleep(2000);
		} catch (Exception e) {
		}

		int newCount = cart.getProductCount(itemToDelete);
		getTest().info("Count of '" + itemToDelete + "' after delete: " + newCount);

		Assert.assertEquals(newCount, initialCount - 1, "Product count did not decrease!");

		getTest().pass("Product removed successfully.");
	}
}
