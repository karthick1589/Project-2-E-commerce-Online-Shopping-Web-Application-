package pageclass;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

	WebDriver driver;
	WebDriverWait wait;

	By loginLink = By.id("login2");
	By loginUsername = By.id("loginusername");
	By loginPassword = By.id("loginpassword");
	By loginBtn = By.xpath("//button[text()='Log in']");

	By welcomeUser = By.id("nameofuser");
	By logoutLink = By.id("logout2");

	By signUpLink = By.id("signin2");
	By signUsername = By.id("sign-username");
	By signPassword = By.id("sign-password");
	By signUpBtn = By.xpath("//button[text()='Sign up']");

	By samsungGalaxyS6 = By.linkText("Samsung galaxy s6");
	By addToCartBtn = By.xpath("//a[text()='Add to cart']");
	By cartLink = By.id("cartur");
	By loginModal = By.id("logInModal");

	By homeLink = By.partialLinkText("Home");
	By categoryPhones = By.linkText("Phones");
	By categoryLaptops = By.linkText("Laptops");
	By categoryMonitors = By.linkText("Monitors");

	By productCardTitle = By.cssSelector(".card-title a");
	By productContainer = By.id("tbodyid");

	By productCard = By.cssSelector(".card");
	By productBody = By.cssSelector(".card-body");

	public HomePage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	public void clickLogin() {
		wait.until(ExpectedConditions.elementToBeClickable(loginLink)).click();
	}

	public void login(String user, String pass) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(loginUsername));

		driver.findElement(loginUsername).clear();
		driver.findElement(loginUsername).sendKeys(user);

		driver.findElement(loginPassword).clear();
		driver.findElement(loginPassword).sendKeys(pass);

		WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(loginBtn));

		try {
			btn.click();
		} catch (Exception e) {
			System.out.println("Standard login click failed. Switching to JS Click.");
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
		}
	}

	public String getLoginPasswordType() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(loginPassword));
		return driver.findElement(loginPassword).getAttribute("type");
	}

	public boolean isWelcomeDisplayed() {
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(welcomeUser)).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public String getWelcomeText() {
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(welcomeUser)).getText();
		} catch (Exception e) {
			return "";
		}
	}

	public void clickLogout() {
		wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
	}

	public void clickSignUp() {
		wait.until(ExpectedConditions.elementToBeClickable(signUpLink)).click();
	}

	public void signUp(String user, String pass) {

		wait.until(ExpectedConditions.visibilityOfElementLocated(signUsername));

		driver.findElement(signUsername).clear();
		driver.findElement(signUsername).sendKeys(user);

		driver.findElement(signPassword).clear();
		driver.findElement(signPassword).sendKeys(pass);

		wait.until(ExpectedConditions.elementToBeClickable(signUpBtn)).click();
	}

	public String getAlertText() {
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			String text = alert.getText();
			alert.accept();
			return text;

		} catch (Exception e) {
			try {
				Alert alert = driver.switchTo().alert();
				String text = alert.getText();
				alert.accept();
				return text;
			} catch (Exception ex) {
				return "No Alert Found";
			}
		}
	}

	public void addProductToCart() {
		wait = null;
		wait.until(ExpectedConditions.elementToBeClickable(samsungGalaxyS6)).click();
		wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn)).click();
		wait.until(ExpectedConditions.alertIsPresent()).accept(); // Accept "Product added" alert
		driver.findElement(cartLink).click();
	}

	public void clickHome() {
		wait.until(ExpectedConditions.elementToBeClickable(homeLink)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(productCardTitle));
	}

	public void clickCategoryPhones() {
		wait.until(ExpectedConditions.elementToBeClickable(categoryPhones)).click();
		wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(productCardTitle)));
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}
	}

	public void clickCategoryLaptops() {
		wait.until(ExpectedConditions.elementToBeClickable(categoryLaptops)).click();
		wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(productCardTitle)));
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}
	}

	public void clickCategoryMonitors() {
		wait.until(ExpectedConditions.elementToBeClickable(categoryMonitors)).click();
		wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(productCardTitle)));
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}
	}

	public List<String> getProductList() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(productContainer));
		List<WebElement> products = driver.findElements(productCardTitle);
		return products.stream().map(WebElement::getText).toList();
	}

	public String clickFirstProduct() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(productCardTitle));
		List<WebElement> products = driver.findElements(productCardTitle);
		if (!products.isEmpty()) {
			String title = products.get(0).getText();

			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", products.get(0));
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}

			products.get(0).click();
			return title;
		}
		return null;
	}

	public boolean isHomeGridDisplayed() {
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(productContainer)).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public void clickProduct(String productName) {
		By productLocator = By.linkText(productName);
		WebDriverWait navWait = new WebDriverWait(driver, Duration.ofSeconds(5));

		for (int i = 0; i < 3; i++) {
			try {
				try {
					wait.until(ExpectedConditions.invisibilityOfElementLocated(loginModal));
				} catch (Exception e) {
				}

				WebElement element = wait.until(ExpectedConditions.elementToBeClickable(productLocator));

				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
				try {
					Thread.sleep(500);
				} catch (Exception e) {
				}

				if (i == 0) {
					element.click();
				} else {
					System.out.println("Standard click failed. Switching to JS Click for '" + productName + "'");
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
				}

				navWait.until(ExpectedConditions.urlContains("prod.html"));

				return;

			} catch (Exception e) {
				System.out.println("Navigation failed or click intercepted. Retrying... Attempt: " + (i + 1));

				if (i == 2) {
					throw new RuntimeException(
							"Failed to navigate to product page for '" + productName + "' after 3 attempts.");
				}
			}
		}
	}

	public boolean isLoginButtonDisplayed() {
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(loginLink)).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isWelcomeButtonInvisible() {
		try {
			return wait.until(ExpectedConditions.invisibilityOfElementLocated(welcomeUser));
		} catch (Exception e) {
			return false;
		}
	}

	public List<WebElement> getAllProductCards() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(productCard));
		return driver.findElements(productCard);
	}

	public void openContactModal() {
		driver.findElement(By.linkText("Contact")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("exampleModal")));
	}

	public WebElement getContactModalTitle() {
		return driver.findElement(By.id("exampleModalLabel"));
	}

	public WebElement getLoginButton() {
		return driver.findElement(loginLink);
	}

	public WebElement getSignUpButton() {
		return driver.findElement(signUpLink);
	}
}
