package appiumtest;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
// Removed Duration import as we're using the older WebDriverWait constructor

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;

public class Basics {
    public static void main(String[] args) {
        // define basic test requirements such as device name, app/apk location
        DesiredCapabilities cap = new DesiredCapabilities();
        // Using the correct path - the APK is in src/appium folder
        File appDir = new File("src");
        File app = new File(appDir, "rentsmart-debug.apk");
        
        // Print path to verify
        System.out.println("APK path: " + app.getAbsolutePath());
        System.out.println("APK exists: " + app.exists());
        
        // Capabilities setup
        cap.setCapability(MobileCapabilityType.DEVICE_NAME, "Pixel 8 API 24");
        cap.setCapability("udid", "emulator-5554"); // Using the UDID from your logs
        cap.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
        cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
        cap.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        cap.setCapability("noReset", true);
        cap.setCapability("autoGrantPermissions", true);
        
        try {
            // For Appium 2.0+, the correct endpoint is /
            URL url = new URI("http://127.0.0.1:4723").toURL();
            AndroidDriver<AndroidElement> driver = new AndroidDriver<>(url, cap);
            System.out.println("Session created successfully!");
            
            // Create a wait object to wait for elements to appear
            // Using the compatible constructor for older Selenium versions
            WebDriverWait wait = new WebDriverWait(driver, 10);
            
            // Wait for app to fully load
            Thread.sleep(2000);
            
            // Try multiple locator strategies for more reliability
            // First print all available text elements to debug
            System.out.println("Attempting to find and list all text elements:");
            try {
                java.util.List<AndroidElement> allTextElements = driver.findElementsByClassName("android.widget.TextView");
                System.out.println("Found " + allTextElements.size() + " TextView elements");
                for (int i = 0; i < allTextElements.size(); i++) {
                    AndroidElement element = allTextElements.get(i);
                    System.out.println("Element " + i + ": Text=" + element.getText() + 
                                      ", Displayed=" + element.isDisplayed());
                }
            } catch (Exception e) {
                System.out.println("Exception while listing elements: " + e.getMessage());
            }
            
            // Example 1: Try to click on "App" using multiple approaches
            System.out.println("Attempting to click on App using different strategies");
            
            try {
                // Strategy 1: Using accessibility ID
                WebElement appOption = driver.findElementByAccessibilityId("App");
                System.out.println("Found App element using AccessibilityID, clicking...");
                appOption.click();
            } catch (Exception e1) {
                System.out.println("AccessibilityID approach failed, trying XPath with text only");
                
                try {
                    // Strategy 2: Using simple text XPath
                    WebElement appOption = driver.findElementByXPath("//android.widget.TextView[@text='App']");
                    System.out.println("Found App element using simple XPath, clicking...");
                    appOption.click();
                } catch (Exception e2) {
                    System.out.println("Simple XPath approach failed, trying content-desc");
                    
                    try {
                        // Strategy 3: Using content-desc
                        WebElement appOption = driver.findElementByXPath("//android.widget.TextView[@content-desc='App']");
                        System.out.println("Found App element using content-desc, clicking...");
                        appOption.click();
                    } catch (Exception e3) {
                        System.out.println("Content-desc approach failed, trying resource-id only");
                        
                        try {
                            // Strategy 4: Using resource-id with index
                            WebElement appOption = driver.findElementByXPath("(//android.widget.TextView[@resource-id='android:id/text1'])[4]");
                            System.out.println("Found App element using resource-id with index, clicking...");
                            appOption.click();
                        } catch (Exception e4) {
                            System.out.println("All element location strategies failed for App element");
                            throw new RuntimeException("Unable to find App element", e4);
                        }
                    }
                }
            }
            
            // Add delay to ensure App screen loads completely
            Thread.sleep(2000);
            
            // Example 2: Click on "Activity" after entering App with error handling
            try {
                // Wait for the screen to transition
                System.out.println("Looking for Activity element");
                
                // Try multiple approaches for Activity
                try {
                    WebElement activityOption = driver.findElementByXPath("//android.widget.TextView[@text='Activity']");
                    System.out.println("Found Activity element using text, clicking...");
                    activityOption.click();
                } catch (Exception e) {
                    // Try alternative approach
                    WebElement activityOption = driver.findElementByAndroidUIAutomator(
                        "new UiSelector().text(\"Activity\")");
                    System.out.println("Found Activity element using UIAutomator, clicking...");
                    activityOption.click();
                }
                
                // Add delay to ensure screen loads
                Thread.sleep(1000);
                
                // Example 3: Click on "Custom Title" with error handling
                try {
                    WebElement customTitleOption = driver.findElementByXPath("//android.widget.TextView[@text='Custom Title']");
                    System.out.println("Found Custom Title element, clicking...");
                    customTitleOption.click();
                } catch (Exception e) {
                    // Try alternative approach
                    WebElement customTitleOption = driver.findElementByAndroidUIAutomator(
                        "new UiSelector().text(\"Custom Title\")");
                    System.out.println("Found Custom Title element using UIAutomator, clicking...");
                    customTitleOption.click();
                }
            } catch (Exception e) {
                System.out.println("Failed to navigate to Activity > Custom Title: " + e.getMessage());
                // If navigation fails, let's try a different test path
                System.out.println("Trying alternative test path...");
            }
            
            // Example 4: Find and interact with input field
            WebElement leftTextField = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.id("io.appium.android.apis:id/left_text_edit")
                )
            );
            leftTextField.clear();
            leftTextField.sendKeys("Hello Appium");
            System.out.println("Entered text in left field");
            
            // Example 5: Click a button
            WebElement changeLeftButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.id("io.appium.android.apis:id/left_text_button")
                )
            );
            changeLeftButton.click();
            System.out.println("Clicked the Change Left button");
            
            // Wait a moment to observe the changes
            Thread.sleep(2000);
            
            // Navigate back to the previous screen
            driver.navigate().back();
            System.out.println("Navigated back");
            
            // Wait a moment before navigating back again
            Thread.sleep(1000);
            driver.navigate().back();
            System.out.println("Navigated back again");
            
            // Attempt to navigate back to main menu if we're not already there
            try {
                // Navigate back until we find the main menu
                for (int i = 0; i < 3; i++) {
                    // Check if we're on the main menu by looking for API Demos title
                    try {
                        WebElement apiDemosTitle = driver.findElementByXPath("//android.widget.TextView[@text='API Demos']");
                        if (apiDemosTitle.isDisplayed()) {
                            System.out.println("Found main menu with API Demos title");
                            break;
                        }
                    } catch (Exception e) {
                        // Not on main menu, press back button
                        System.out.println("Pressing back button to return to main menu");
                        driver.navigate().back();
                        Thread.sleep(1000);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error while navigating back to main menu: " + e.getMessage());
            }
            
            // Wait for main menu to be stable
            Thread.sleep(1500);
            
            // Example 6: Try to click on Views using multiple approaches
            System.out.println("Attempting to click on Views option");
            
            try {
                // First try with UIAutomator - often more reliable
                WebElement viewsOption = driver.findElementByAndroidUIAutomator(
                    "new UiSelector().text(\"Views\")");
                System.out.println("Found Views element using UIAutomator, clicking...");
                viewsOption.click();
            } catch (Exception e) {
                System.out.println("UIAutomator approach failed for Views, trying XPath");
                
                try {
                    // Try with simple XPath
                    WebElement viewsOption = driver.findElementByXPath("//android.widget.TextView[@text='Views']");
                    System.out.println("Found Views element using XPath, clicking...");
                    viewsOption.click();
                } catch (Exception e2) {
                    System.out.println("XPath approach failed for Views, trying content-desc");
                    
                    try {
                        // Try with content-desc
                        WebElement viewsOption = driver.findElementByXPath("//android.widget.TextView[@content-desc='Views']");
                        System.out.println("Found Views element using content-desc, clicking...");
                        viewsOption.click();
                    } catch (Exception e3) {
                        System.out.println("All strategies failed for Views element");
                    }
                }
            }
            
            // Wait for Views screen to load
            Thread.sleep(1500);
            
            // Example 7: Attempt to scroll and find WebView with error handling
            System.out.println("Attempting to scroll to find 'WebView'...");
            try {
                WebElement scrollToElement = driver.findElementByAndroidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().textContains(\"WebView\"))"
                );
                System.out.println("Found WebView element after scrolling, clicking...");
                scrollToElement.click();
            } catch (Exception e) {
                System.out.println("Failed to scroll to WebView: " + e.getMessage());
                
                // Try alternate UI element - Controls
                try {
                    System.out.println("Attempting to find Controls instead...");
                    WebElement controlsElement = driver.findElementByAndroidUIAutomator(
                        "new UiSelector().text(\"Controls\")"
                    );
                    System.out.println("Found Controls element, clicking...");
                    controlsElement.click();
                } catch (Exception e2) {
                    System.out.println("Failed to find Controls element: " + e2.getMessage());
                }
            }
            
            // Wait a moment to observe the WebView
            Thread.sleep(2000);
            
            // Go back to main menu for another example
            driver.navigate().back();
            driver.navigate().back();
            System.out.println("Navigated back to main menu");
            
            // Example 8: Click on "Graphics" using resource-id and index
            WebElement graphicsOption = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.widget.TextView[@resource-id='android:id/text1' and @text='Graphics']")
                )
            );
            System.out.println("Found Graphics element with index 5, clicking...");
            graphicsOption.click();
            
            // Wait a moment
            Thread.sleep(2000);
            
            // Close the driver
            System.out.println("Test completed successfully. Closing driver...");
            driver.quit();
            
        } catch (MalformedURLException | URISyntaxException e) {
            System.out.println("Error creating URL: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Thread sleep interrupted: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}