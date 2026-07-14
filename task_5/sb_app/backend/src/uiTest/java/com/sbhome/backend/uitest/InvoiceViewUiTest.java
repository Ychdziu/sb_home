package com.sbhome.backend.uitest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class InvoiceViewUiTest {

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
    void unpaidInvoicesTableLoadsAndShowsRows() {
        driver.get(BASE_URL + "/invoices");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table tbody tr")));
        List<org.openqa.selenium.WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));

        assertTrue(rows.size() > 0, "Expected at least one unpaid invoice row to render");
    }
}