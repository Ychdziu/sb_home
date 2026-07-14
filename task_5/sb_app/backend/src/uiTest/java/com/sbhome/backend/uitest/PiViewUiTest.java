package com.sbhome.backend.uitest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PiViewUiTest {

    private static final String BASE_URL = "http://localhost:5173";

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void submittingPrecisionShowsPiValue() {
        driver.get(BASE_URL + "/pi");

        driver.findElement(By.id("precision")).sendKeys("20");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".result")));
        // "3.14159..." - confirm it starts with the expected leading digits, not just "non-empty"
        assertTrue(result.getText().startsWith("3.14159"));
    }

    @Test
    void submittingNegativePrecisionShowsErrorMessage() {
        driver.get(BASE_URL + "/pi");

        driver.findElement(By.id("precision")).sendKeys("-5");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error")));
        assertTrue(error.getText().toLowerCase().contains("negative"));
    }
}