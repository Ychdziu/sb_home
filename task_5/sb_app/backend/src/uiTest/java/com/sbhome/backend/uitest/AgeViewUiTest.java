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

class AgeViewUiTest {

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
    void submittingValidAgeShowsResultText() {
        driver.get(BASE_URL + "/age");

        driver.findElement(By.id("age")).sendKeys("25");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".result")));
        assertTrue(result.getText().toLowerCase().contains("adult"));
    }

    @Test
    void submittingNegativeAgeShowsErrorMessage() {
        driver.get(BASE_URL + "/age");

        driver.findElement(By.id("age")).sendKeys("-5");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error")));
        assertTrue(error.getText().toLowerCase().contains("negative"));
    }
}