package com.rohit.fitch;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public class BaseTest {
    protected WebDriver driver;
    protected String baseUrl;
    protected int pageLoadTimeoutSeconds;
    protected int waitTimeoutSeconds;

    @BeforeEach
    public void setUp() throws IOException {
        Properties props = new Properties();
        try (InputStream in = BaseTest.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) props.load(in);
        }

        baseUrl = getenvOrDefault("BASE_URL", props.getProperty("baseUrl", "https://www.fitchratings.com"));
        pageLoadTimeoutSeconds = Integer.parseInt(props.getProperty("pageLoadTimeoutSeconds", "30"));
        waitTimeoutSeconds = Integer.parseInt(props.getProperty("waitTimeoutSeconds", "20"));

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--window-size=1600,1200");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeoutSeconds));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private static String getenvOrDefault(String key, String def) {
        String val = System.getProperty(key);
        if (val != null && !val.isBlank()) return val;
        val = System.getenv(key);
        if (val != null && !val.isBlank()) return val;
        return def;
    }
}
