package appiumtest;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SeleniumRunner {

    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://www.youtube.com");
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

            // 1. Cari keyword
            WebElement searchBox = driver.findElement(By.name("search_query"));
            searchBox.sendKeys("Rumah Editor");
            searchBox.sendKeys(Keys.ENTER);

            // Tunggu hasil pencarian muncul
            Thread.sleep(4000);

            // 2. Klik video pertama
            List<WebElement> videos = driver.findElements(By.id("video-title"));
            if (videos.size() > 0) {
                videos.get(0).click();
            } else {
                System.out.println("Video tidak ditemukan!");
                return;
            }

            // 3. Tunggu video mulai
            Thread.sleep(5000);

            // 3.1. Coba klik tombol 'Skip Ads' jika muncul
            try {
                WebElement skipButton = driver.findElement(By.className("ytp-ad-skip-button"));
                if (skipButton.isDisplayed()) {
                    skipButton.click();
                    System.out.println("Iklan dilewati.");
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                System.out.println("Tidak ada iklan atau tombol skip tidak muncul.");
            }

            // 4. Scroll perlahan ke bawah untuk load komentar
            JavascriptExecutor js = (JavascriptExecutor) driver;
            for (int i = 0; i < 5; i++) {
                js.executeScript("window.scrollBy(0, 500);");
                Thread.sleep(1500);
            }

            // 5. Cari komentar dengan XPath yang benar (tanpa /text())
            List<WebElement> commentElements = driver.findElements(
                By.xpath("//*[@id='content-text']")
            );

            // 6. Jika belum ada komentar, scroll dan tunggu lagi
            int retries = 5;
            while (commentElements.size() < 1 && retries > 0) {
                js.executeScript("window.scrollBy(0, 300);");
                Thread.sleep(2000);
                commentElements = driver.findElements(By.xpath("//*[@id='content-text']"));
                retries--;
            }

            // 7. Ambil dan cetak komentar
            if (!commentElements.isEmpty()) {
                System.out.println("\nKomentar pada video:");
                for (int i = 0; i < Math.min(commentElements.size(), 5); i++) {
                    System.out.println((i + 1) + ". " + commentElements.get(i).getText());
                }
            } else {
                System.out.println("Tidak ada komentar yang ditemukan atau belum dimuat.");
            }

            // 8. Kembali ke halaman pencarian
            driver.navigate().back();
            Thread.sleep(3000);

            // 9. Ambil judul video berikutnya
            videos = driver.findElements(By.id("video-title"));
            System.out.println("\nJudul 5 video setelah video pertama:");
            for (int i = 1; i <= 5 && i < videos.size(); i++) {
                System.out.println((i) + ". " + videos.get(i).getAttribute("title"));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
