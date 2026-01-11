package testcases;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import baseclass.BaseTest;
import pageclass.HomePage;
import utilityclass.ExcelUtils;

@Listeners(baseclass.TestListener.class)

public class SignUpTest extends BaseTest {

	@DataProvider(name = "EmptyFieldData")
	public Object[][] getEmptyFieldData() {
		Object[][] data = ExcelUtils.getTestData("EmptyFields");
		if (data == null || data.length == 0) {
			return new Object[][] { { "", "" }, { "user_only", "" }, { "", "pass_only" } };
		}
		return data;
	}

	@Test(priority = 1)
	public void verifySignUpWithUniqueCredentials() {

		test.set(extent.createTest("Sign-Up Functionality-Test_Cases_06-Verify user sign-up with unique credentials"));

		HomePage home = new HomePage(getDriver());
		home.clickSignUp();
		getTest().info("Clicked on Sign Up button");

		String uniqueUser = "user_" + System.currentTimeMillis();
		String password = "password123";

		home.signUp(uniqueUser, password);
		getTest().info("Entered credentials: " + uniqueUser);

		String alertMessage = home.getAlertText();
		getTest().info("Alert received: " + alertMessage);

		ExcelUtils.writeToExcel("CreatedUsers", uniqueUser, password);
		getTest().info("Credentials saved to Excel sheet 'CreatedUsers'");

		Assert.assertEquals(alertMessage, "Sign up successful.", "Alert message did not match!");
		getTest().pass("Sign Up validated successfully.");

	}

	@Test(priority = 2)
	public void verifySignUpWithExistingUsername() {
		test.set(extent.createTest("Sign-Up Functionality-Test_Cases_07-Verify sign-up with existing username"));

		HomePage home = new HomePage(getDriver());

		String[] existingUser = ExcelUtils.getRandomUser("CreatedUsers");

		if (existingUser == null) {
			getTest().fail("Could not fetch data from excel");
			Assert.fail("Excel Data missing.");
		}

		String username = existingUser[0];
		String password = existingUser[1];

		home.clickSignUp();
		getTest().info("Attempting Sign Up with Existing User: " + username);
		home.signUp(username, password);

		String alertMessage = home.getAlertText();
		getTest().info("Alert Text Retrieved:" + alertMessage);

		String screenPath = captureScreenshot("Signup_Existing_Error_PostAlert");
		if (screenPath != null)
			getTest().addScreenCaptureFromPath(screenPath);

		Assert.assertTrue(alertMessage.contains("already exist"),
				"Expected 'already exist' warning but got: " + alertMessage);

		getTest().pass("Verified existing user cannot sign up.");
	}

	@Test(priority = 3, dataProvider = "EmptyFieldData")
	public void verifySignUpWithEmptyFields(String username, String password) {
		test.set(extent.createTest("Sign-Up Functionality-Test_Cases_08-Empty Fields Check"));
		HomePage home = new HomePage(getDriver());

		home.clickSignUp();

		if (username == null)
			username = "";
		if (password == null)
			password = "";

		getTest().info("Testing with input: User='" + username + "', Pass='" + password + "'");

		home.signUp(username, password);

		String alertMessage = home.getAlertText();

		String ScreenPath = captureScreenshot("SignUp_Empty_Error");
		if (ScreenPath != null)
			getTest().addScreenCaptureFromPath(ScreenPath);

		Assert.assertEquals(alertMessage, "Please fill out Username and Password.");
		getTest().pass("Empty fields validation passed.");
	}

}