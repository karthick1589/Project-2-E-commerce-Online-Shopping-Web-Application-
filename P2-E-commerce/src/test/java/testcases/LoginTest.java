package testcases;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import baseclass.BaseTest;
import pageclass.HomePage;
import utilityclass.ExcelUtils;

@Listeners(baseclass.TestListener.class)
public class LoginTest extends BaseTest {

	private String testUsername;

	@BeforeClass
	public void setupData() {
		String[] user = ExcelUtils.getRandomUser("CreatedUsers");

		if (user != null) {
			testUsername = user[0];
			System.out.println("--- TEST SETUP: Selected User for all tests: " + testUsername + " ---");
		} else {
			testUsername = "ERROR_NO_USER";
			System.err.println("--- ERROR: No user found in Excel 'CreatedUsers' sheet! ---");
		}
	}

	@Test(priority = 1)
	public void verifyLoginWithValidCredentials() {
		test.set(extent.createTest("Login Functionality-Test_Cases_01-Verify Valid Credentials"));
		HomePage home = new HomePage(getDriver());

		String[] validUser = ExcelUtils.getRandomUser("CreatedUsers");
		if (validUser == null)
			Assert.fail("No user data found in Excel sheet 'CreatedUsers'");

		String username = validUser[0];
		String password = validUser[1];

		home.clickLogin();
		home.login(username, password);
		getTest().info("Logged in with: " + username);

		boolean isWelcomeDisplayed = home.isWelcomeDisplayed();

		if (isWelcomeDisplayed) {
			String path = captureScreenshot("Login_Success_Welcome");
			if (path != null)
				getTest().addScreenCaptureFromPath(path);
		}

		Assert.assertTrue(isWelcomeDisplayed, "Welcome message was not displayed!");
		Assert.assertTrue(home.getWelcomeText().contains(username), "Welcome text does not contain username!");

		getTest().pass("User redirected to Home Page successfully.");

		home.clickLogout();
	}

	@Test(priority = 2)
	public void verifyLoginWithIncorrectPassword() {
		test.set(extent.createTest("Login Functionality-Test_Cases_02- Verify Incorrect Password"));
		HomePage home = new HomePage(getDriver());

		String[] validUser = ExcelUtils.getRandomUser("CreatedUsers");
		if (validUser == null)
			Assert.fail("Data missing");

		String username = validUser[0];

		String[] InvalidPassword = ExcelUtils.getRandomUser("WrongPassword");
		if (InvalidPassword == null)
			Assert.fail("Data missing");

		String wrongPassword = InvalidPassword[0];

		home.clickLogin();
		home.login(username, wrongPassword);
		getTest().info("Attempted login with wrong password.");

		String alertMessage = home.getAlertText();
		getTest().info("Alert received: " + alertMessage);

		Assert.assertTrue(alertMessage.contains("Please fill out Username and Password."),
				"Expected 'Please fill out Username and Password.' alert but got: " + alertMessage);
		getTest().pass("Error message validated successfully.");
	}

	@Test(priority = 3)
	public void verifyLoginWithEmptyFields() {
		test.set(extent.createTest("Login Functionality-Test_Cases_03- Verify Empty Fields"));
		HomePage home = new HomePage(getDriver());

		String[] emptyUser = ExcelUtils.getRandomUser("EmptyFields");

		if (emptyUser == null) {
			System.out.println("Warning: Excel returned null. Using default empty strings.");
			emptyUser = new String[] { "", "" };
		}

		String userName = emptyUser[0];
		String passWord = emptyUser[1];

		if (userName == null)
			userName = "";
		if (passWord == null)
			passWord = "";

		home.clickLogin();
		home.login(userName, passWord);
		getTest().info("Clicked Login with empty fields.");

		String alertMessage = home.getAlertText();
		getTest().info("Alert received: " + alertMessage);

		Assert.assertEquals(alertMessage, "Please fill out Username and Password.");
		getTest().pass("Empty field validation passed.");
	}

	@Test(priority = 4)
	public void verifyLoginWithInvalidFormat() {
		test.set(extent.createTest("Login Functionality-Test_Cases_04- Verify Invalid Email/Username Format"));
		HomePage home = new HomePage(getDriver());

		String[] validUser = ExcelUtils.getRandomUser("InvalidUsers");
		if (validUser == null)
			Assert.fail("Data missing");

		String username = validUser[0];

		String Password = validUser[1];

		home.clickLogin();
		home.login(username, Password);
		getTest().info("Attempted login with invalid format: " + username);

		String alertMessage = home.getAlertText();
		getTest().info("Alert received: " + alertMessage);

		Assert.assertTrue(alertMessage.contains("Please fill out Username and Password."),
				"Expected 'Please fill out Username and Password.' but got: " + alertMessage);
		getTest().pass("System handled invalid user/format correctly.");
	}

	@Test(priority = 5)
	public void verifyPasswordFieldMasking() {
		test.set(extent.createTest("Login Functionality-Test_Cases_05- Verify Password Masking"));
		HomePage home = new HomePage(getDriver());

		home.clickLogin();

		String fieldType = home.getLoginPasswordType();
		getTest().info("Password field type is: " + fieldType);

		Assert.assertEquals(fieldType, "password", "Password field is NOT masked!");

		getTest().pass("Password field is correctly masked.");
	}

	@Test(priority = 6)
	public void verifyLogout() {
		test.set(extent.createTest("Logout Functionality-Test_Cases_20-Verify logout after login"));
		HomePage home = new HomePage(getDriver());

		if (testUsername.startsWith("ERROR"))
			Assert.fail("Excel Data Missing - Cannot run test.");
		
		String[] validUser = ExcelUtils.getRandomUser("CreatedUsers");

		String username = validUser[0];
		String password = validUser[1];

		home.clickLogin();
		home.login(username, password);
		getTest().info("Logged in as: " + testUsername);

		Assert.assertTrue(home.isWelcomeDisplayed(), "Login failed! Cannot proceed to logout verification.");

		home.clickLogout();
		getTest().info("Clicked Log out button.");

		boolean isWelcomeGone = home.isWelcomeButtonInvisible();
		Assert.assertTrue(isWelcomeGone, "'Welcome user' text did not disappear.");

		boolean isLoginVisible = home.isLoginButtonDisplayed();
		Assert.assertTrue(isLoginVisible, "'Log in' button did not reappear.");

		getTest().pass("Logout successful. User redirected to guest view.");
	}
}