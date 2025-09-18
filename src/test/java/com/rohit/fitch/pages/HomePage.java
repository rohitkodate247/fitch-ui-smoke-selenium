package com.rohit.fitch.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Minimal POM for Fitch Ratings homepage */
public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public HomePage(WebDriver driver, int waitTimeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(waitTimeoutSeconds));
    }

    public HomePage open(String baseUrl) {
        driver.navigate().to(baseUrl);
        return this;
    }

    public void acceptCookiesIfPresent() {
        // Try a handful of common selectors/texts used by consent banners; ignore if not found
        String[] selectors = new String[] {
                "button#onetrust-accept-btn-handler",
                "button[aria-label='Accept cookies']",
                "button[aria-label='Accept Cookies']",
                "button.cookie-accept",
                "button[mode='primary']",
                "button[title='Accept']"
        };
        for (String css : selectors) {
            try {
                WebElement el = new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(css)));
                if (el.isDisplayed()) {
                    el.click();
                    return;
                }
            } catch (TimeoutException | NoSuchElementException | ElementClickInterceptedException ignored) {}
        }
        // Fallback by text search
        try {
            WebElement el = driver.findElement(By.xpath("//button[contains(.,'Accept') or contains(.,'I Agree')]"));
            if (el.isDisplayed()) el.click();
        } catch (NoSuchElementException ignored) {}
    }

    public boolean isHeaderVisible() {
        try {
            WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("header")));
            return header.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isFooterVisible() {
        try {
            WebElement footer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("footer")));
            return footer.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void openSearch() {
        // Try common search triggers (icon/button)
        By[] candidates = new By[] {
                By.cssSelector("[aria-label='Search']"),
                By.cssSelector("button[aria-label*='Search']"),
                By.cssSelector("button[title*='Search']"),
                By.cssSelector("button[class*='search']"),
                By.cssSelector("a[href*='search']")
        };
        for (By by : candidates) {
            try {
                WebElement el = wait.until(ExpectedConditions.elementToBeClickable(by));
                el.click();
                return;
            } catch (TimeoutException | ElementClickInterceptedException ignored) {}
        }
        // If none clickable, try keyboard shortcut 's' focusing search—best effort
        try {
            WebElement body = driver.findElement(By.tagName("body"));
            body.sendKeys("s");
        } catch (Exception ignored) {}
    }

    public void typeAndSubmitSearch(String query) {
        // Try multiple input selectors
        By[] inputs = new By[] {
                By.cssSelector("input[type='search']"),
                By.cssSelector("input[name='q']"),
                By.cssSelector("input[placeholder*='Search']"),
                By.cssSelector("input[class*='search']")
        };
        WebElement box = null;
        for (By by : inputs) {
            try {
                box = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                if (box.isDisplayed()) break;
            } catch (TimeoutException ignored) {}
        }
        if (box == null) {
            // As a last resort, try focusing first input
            box = driver.findElement(By.cssSelector("input"));
        }
        box.clear();
        box.sendKeys(query);
        box.sendKeys(Keys.ENTER);
    }

    public String getTitle() {
        return driver.getTitle();
    }
}
