package testcases;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import baseclass.BaseTest;

@Listeners(baseclass.TestListener.class)
public class ResponsiveTest extends BaseTest {

    public boolean isHorizontalScrollPresent() {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        return (Boolean) js.executeScript(
            "return document.documentElement.scrollWidth > document.documentElement.clientWidth;"
        );
    }

    @Test(priority = 1)
    public void verifyResponsiveDesign() {
        test.set(extent.createTest("Responsive Design-Test_Cases_25- Verify No Horizontal Scroll"));
        verifyResolution(375, 812, "Mobile Portrait (375x812)");

        verifyResolution(768, 1024, "Tablet Portrait (768x1024)");

        verifyResolution(1366, 768, "Desktop Standard (1366x768)");
    }

    public void verifyResolution(int width, int height, String deviceName) {

        getDriver().manage().window().setSize(new Dimension(width, height));

        try { Thread.sleep(1000); } catch (Exception e) {}

        boolean hasScroll = isHorizontalScrollPresent();
        
        if (hasScroll) {
            getTest().fail("Horizontal scrollbar detected on " + deviceName);
        } else {
            getTest().pass("Layout fits correctly on " + deviceName);
        }

        Assert.assertFalse(hasScroll, "Page requires horizontal scrolling on " + deviceName + "!");
    }
}
