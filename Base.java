package appiumtest;

import java.io.File;
import java.net.URL;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;

public class Base {
    public static void main(String[] args) throws Exception {

        DesiredCapabilities cap = new DesiredCapabilities();

        File appDir = new File("src");
        File app = new File(appDir, "punya ibu.apk");

        cap.setCapability(MobileCapabilityType.DEVICE_NAME, "Pixel 8 API 24");
        cap.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
        cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
        cap.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");

        AndroidDriver<AndroidElement> driver = new AndroidDriver<>(
                new URL("http://127.0.0.1:4723"), cap);
    }
}
