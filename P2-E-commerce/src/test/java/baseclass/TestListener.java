package baseclass;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.Status;

public class TestListener implements ITestListener {
	
	@Override
    public void onTestFailure(ITestResult result) {
        if (BaseTest.getTest() != null) {
            BaseTest.getTest().log(Status.FAIL, "Test Failed: " + result.getThrowable());
            
            // --- FIX: Robust Null Check ---
            String path = BaseTest.captureScreenshot(result.getName());
            
            // Only add screenshot if path is NOT null and NOT empty
            if (path != null && !path.isEmpty()) {
                try {
                    BaseTest.getTest().addScreenCaptureFromPath(path);
                } catch (Exception e) {
                    BaseTest.getTest().info("Could not attach screenshot.");
                }
            } else {
                BaseTest.getTest().info("Screenshot failed (Browser likely crashed).");
            }
        }
    }
    
    @Override
    public void onTestStart(ITestResult result) {

    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (BaseTest.getTest() != null) {
            BaseTest.getTest().log(Status.PASS, "Test Passed");
            
            // --- SAFE SCREENSHOT CAPTURE ---
            String path = BaseTest.captureScreenshot(result.getName());
            
            // Only add screenshot if path is valid (NOT NULL)
            if (path != null) {
                BaseTest.getTest().addScreenCaptureFromPath(path);
            } else {
                BaseTest.getTest().info("Screenshot could not be captured (Driver might be closed).");
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {}
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
    @Override public void onStart(ITestContext context) {}
    @Override public void onFinish(ITestContext context) {}
}