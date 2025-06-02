package appiumtest;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;

public class AppiumAutomator {
    private static AndroidDriver<AndroidElement> driver;
    private static WebDriverWait wait;
    
    public static void main(String[] args) {
        DesiredCapabilities cap = new DesiredCapabilities();
        File appDir = new File("src");
        File app = new File(appDir, "rentsmart-debug.apk");
        
        System.out.println("APK path: " + app.getAbsolutePath());
        System.out.println("APK exists: " + app.exists());
        
        cap.setCapability(MobileCapabilityType.DEVICE_NAME, "Pixel_8_API_24");
        cap.setCapability("udid", "emulator-5554");
        cap.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
        cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
        cap.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        cap.setCapability("noReset", true);
        cap.setCapability("autoGrantPermissions", true);
        cap.setCapability("appPackage", "com.example.rentsmart");
        cap.setCapability("appActivity", "com.example.rentsmart.MainActivity");
        
        try {
            URL url = new URI("http://127.0.0.1:4723").toURL();
            driver = new AndroidDriver<>(url, cap);
            wait = new WebDriverWait(driver, 30);
            System.out.println("Session created successfully!");
            
            // Wait for app to load
            Thread.sleep(5000);
            
            // Login Process
            performLogin();
            
            // Navigate through app
            navigateApp();
            
            // Perform booking
            performBooking();
            
            // Navigate to other sections
            navigateToOtherSections();
            
            // Logout
            performLogout();
            
            System.out.println("Test completed successfully. Closing driver...");
            driver.quit();
            
        } catch (Exception e) {
            System.out.println("Error during test execution: " + e.getMessage());
            e.printStackTrace();
            if (driver != null) {
                driver.quit();
            }
        }
    }
    
    private static void performLogin() throws Exception {
        System.out.println("Attempting to login...");
        
        try {
            // Wait and find email field with multiple strategies
            WebElement emailField = null;
            
            // Strategy 1: By class and instance
            try {
                WebDriverWait emailWait = new WebDriverWait(driver, 10);
                emailField = emailWait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//android.widget.EditText[1]")
                ));
            } catch (Exception e1) {
                // Strategy 2: UIAutomator selector
                try {
                    emailField = driver.findElementByAndroidUIAutomator(
                        "new UiSelector().className(\"android.widget.EditText\").instance(0)"
                    );
                } catch (Exception e2) {
                    // Strategy 3: By hint or content description
                    emailField = driver.findElementByAndroidUIAutomator(
                        "new UiSelector().textContains(\"email\").className(\"android.widget.EditText\")"
                    );
                }
            }
            
            if (emailField != null) {
                emailField.clear(); // Clear existing text
                Thread.sleep(1000);
                emailField.sendKeys("budi.santoso@gmail.com");
                System.out.println("Email field filled successfully");
            } else {
                throw new Exception("Email field not found");
            }
            
            Thread.sleep(2000);
            
            // Find password field
            WebElement passwordField = null;
            
            try {
                passwordField = driver.findElementByXPath("//android.widget.EditText[2]");
            } catch (Exception e1) {
                try {
                    passwordField = driver.findElementByAndroidUIAutomator(
                        "new UiSelector().className(\"android.widget.EditText\").instance(1)"
                    );
                } catch (Exception e2) {
                    passwordField = driver.findElementByAndroidUIAutomator(
                        "new UiSelector().textContains(\"password\").className(\"android.widget.EditText\")"
                    );
                }
            }
            
            if (passwordField != null) {
                passwordField.clear(); // Clear existing text
                Thread.sleep(1000);
                passwordField.sendKeys("Password123!");
                System.out.println("Password field filled successfully");
            } else {
                throw new Exception("Password field not found");
            }
            
            Thread.sleep(2000);
            
            // Find and click sign in button
            WebElement signInButton = findElementWithMultipleStrategies("Sign in", "button");
            if (signInButton != null) {
                signInButton.click();
                System.out.println("Sign in button clicked");
            } else {
                throw new Exception("Sign in button not found");
            }
            
            // Wait for dashboard to load
            Thread.sleep(5000);
            System.out.println("Login completed successfully");
            
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            throw e;
        }
    }
    
    private static void navigateApp() throws InterruptedException {
        System.out.println("Starting app navigation...");
        
        // Scroll dashboard
        scrollDown();
        Thread.sleep(2000);
        
        // Click notifications
        WebElement notifButton = findElementWithMultipleStrategies("Notifikasi", null);
        if (notifButton != null) {
            notifButton.click();
            Thread.sleep(2000);
            driver.navigate().back();
            Thread.sleep(1000);
            System.out.println("Notifications accessed successfully");
        }
        
        // Click explore tab
        WebElement exploreTab = findElementWithMultipleStrategies("Jelajah", null);
        if (exploreTab != null) {
            exploreTab.click();
            Thread.sleep(2000);
            System.out.println("Explore tab opened");
            
            // Back from explore
            try {
                WebElement backButton = driver.findElementByAndroidUIAutomator(
                    "new UiSelector().className(\"android.widget.Button\").instance(0)"
                );
                backButton.click();
            } catch (Exception e) {
                driver.navigate().back();
            }
            Thread.sleep(1000);
        }
        
//        // Click booking/pesanan tab
//        WebElement bookingTab = findElementWithMultipleStrategies("Pesanan", null);
//        if (bookingTab != null) {
//            bookingTab.click();
//            Thread.sleep(2000);
//            System.out.println("Booking tab opened");
//        }
//        
//        // Back to notifications
//        WebElement notifTab = findElementWithMultipleStrategies("Notifikasi", null);
//        if (notifTab != null) {
//            notifTab.click();
//            Thread.sleep(1000);
//        }
        
        // Back to explore (kedua kali - dengan pengecekan yang lebih robust)
        try {
            // Wait untuk memastikan UI sudah ready
            Thread.sleep(2000);
            
            WebElement exploreTab2 = findElementWithMultipleStrategies("Jelajah", null);
            if (exploreTab2 != null && exploreTab2.isEnabled()) {
                exploreTab2.click();
                Thread.sleep(5000);
                System.out.println("Second explore access successful");
            } else {
                System.out.println("Explore tab not available for second access, skipping...");
            }
        } catch (Exception e) {
            System.out.println("Second explore access failed, continuing with test: " + e.getMessage());
        }
    }
    
    private static void performBooking() throws InterruptedException {
        System.out.println("Starting booking process...");

        // Scroll dulu supaya item muncul
        scrollDown();
        Thread.sleep(2000);

        // Coba cari dengan descriptionContains
        WebElement equipment = null;
        try {
            equipment = driver.findElementByAndroidUIAutomator(
                "new UiSelector().descriptionContains(\"Drone DJI Phantom 4\")"
            );
        } catch (Exception e) {
            // Coba dengan description full (pakai newline)
            try {
                equipment = driver.findElementByAndroidUIAutomator(
                    "new UiSelector().description(\"Drone DJI Phantom 4\\nTeknik Sipil\\nRp 200000/hari\")"
                );
            } catch (Exception e2) {
                // Coba dengan XPath
                try {
                    equipment = driver.findElementByXPath(
                        "//android.widget.ImageView[@content-desc=\"Drone DJI Phantom 4\nTeknik Sipil\nRp 200000/hari\"]"
                    );
                } catch (Exception e3) {
                    System.out.println("Equipment not found with any strategy!");
                    throw e3;
                }
            }
        }

        if (equipment != null) {
            equipment.click();
            Thread.sleep(2000);
            
            
            // Click book now
            WebElement bookNowButton = findElementWithMultipleStrategies("Pesan Sekarang", null);
            if (bookNowButton != null) {
                bookNowButton.click();
                Thread.sleep(2000);
                
                // Scroll down untuk payment method
                scrollDown();
                Thread.sleep(1000);
                
                // Select cash payment
                WebElement cashOption = findElementWithMultipleStrategies("Cash Tunai", null);
                if (cashOption != null) {
                    cashOption.click();
                    Thread.sleep(1000);
                }
                
                // Scroll up untuk date selection
                scrollUp();
                Thread.sleep(1000);
                
                // Select date
                WebElement datePickerButton = findElementWithMultipleStrategies("Jun 2025", null);
                if (datePickerButton != null) {
                    datePickerButton.click();
                    Thread.sleep(1000);
                    
                    // Select specific date
                    WebElement datePickerButton2 = findElementWithMultipleStrategies("June 6, 2025", null);
                    if (datePickerButton2 != null) {
                        datePickerButton2.click();
                        Thread.sleep(1000);
                        
                        // Confirm date
                        WebElement confirmDateButton = findElementWithMultipleStrategies("OK", null);
                        if (confirmDateButton != null) {
                            confirmDateButton.click();
                            Thread.sleep(1000);
                        }
                    }
                }
                
                // Click pay now
                WebElement payNowButton = findElementWithMultipleStrategies("Bayar Sekarang", null);
                if (payNowButton != null) {
                    payNowButton.click();
                    Thread.sleep(3000);
                    System.out.println("Booking completed successfully");
                }
            }
        }
    }
    
    private static void navigateToOtherSections() throws InterruptedException {
        // Click history tab
        WebElement historyTab = findElementWithMultipleStrategies("Pesanan", null);
        if (historyTab != null) {
            historyTab.click();
            Thread.sleep(2000);
        }
        
        // Click profile tab
        WebElement profileTab = findElementWithMultipleStrategies("Profil", null);
        if (profileTab != null) {
            profileTab.click();
            Thread.sleep(2000);
        }
    }
    
    private static void performLogout() throws InterruptedException {
        System.out.println("Performing logout...");
        
        // Click logout
        WebElement logoutButton = findElementWithMultipleStrategies("Keluar", null);
        if (logoutButton != null) {
            logoutButton.click();
            Thread.sleep(1000);
            
            // Confirm logout
            WebElement confirmLogout = findElementWithMultipleStrategies("Keluar", null);
            if (confirmLogout != null) {
                confirmLogout.click();
                Thread.sleep(2000);
                System.out.println("Logout completed successfully");
            }
        }
    }
    
    // Helper method untuk mencari element dengan berbagai strategi
    private static WebElement findElementWithMultipleStrategies(String text, String elementType) {
        WebElement element = null;
        
        try {
            // Strategy 1: By description contains
            element = driver.findElementByAndroidUIAutomator(
                "new UiSelector().descriptionContains(\"" + text + "\")"
            );
        } catch (Exception e1) {
            try {
                // Strategy 2: By text contains
                element = driver.findElementByAndroidUIAutomator(
                    "new UiSelector().textContains(\"" + text + "\")"
                );
            } catch (Exception e2) {
                try {
                    // Strategy 3: By text exact match
                    element = driver.findElementByAndroidUIAutomator(
                        "new UiSelector().text(\"" + text + "\")"
                    );
                } catch (Exception e3) {
                    if (elementType != null && elementType.equals("button")) {
                        try {
                            // Strategy 4: Button with text
                            element = driver.findElementByAndroidUIAutomator(
                                "new UiSelector().className(\"android.widget.Button\").textContains(\"" + text + "\")"
                            );
                        } catch (Exception e4) {
                            System.out.println("Element not found with any strategy: " + text);
                        }
                    }
                }
            }
        }
        
        return element;
    }
    
    // Helper method untuk scroll down
    private static void scrollDown() {
        try {
            driver.findElementByAndroidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollForward()"
            );
        } catch (Exception e) {
            System.out.println("Scroll down failed: " + e.getMessage());
        }
    }
    
    // Helper method untuk scroll up
    private static void scrollUp() {
        try {
            driver.findElementByAndroidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollBackward()"
            );
        } catch (Exception e) {
            System.out.println("Scroll up failed: " + e.getMessage());
        }
    }
}