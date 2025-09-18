package com.rohit.fitch.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Minimal POM for Fitch search results */
public class SearchResultsPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public SearchResultsPage(WebDriver driver, int waitTimeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(waitTimeoutSeconds));
    }

    public boolean isLoaded() {
        // Typical indicators: results container, result list, or title change
        try {
            wait.until(d -> d.getTitle() != null && d.getTitle().toLowerCase().contains("search"));
        } catch (TimeoutException ignored) {
        }
        try {
            // Try a few candidate containers/items
            By[] candidates = new By[] {
                    By.cssSelector("[data-testid*='result'], [data-test*='result']"),
                    By.cssSelector("section[id*='results'], div[id*='results']"),
                    By.cssSelector("ul[class*='results'] li, ol[class*='results'] li"),
                    By.cssSelector("article"),
            };
            for (By by : candidates) {
                if (!driver.findElements(by).isEmpty()) {
                    return true;
                }
            }
        } catch (Exception ignored) {
        }
        // Fallback: any anchor list on page
        return !driver.findElements(By.cssSelector("a")).isEmpty();
    }

    public boolean hasAtLeastOneResult() {
        By[] itemSelectors = new By[] {
                By.cssSelector("[data-testid*='result'], [data-test*='result']"),
                By.cssSelector("ul[class*='results'] li, ol[class*='results'] li"),
                By.cssSelector("article"),
                By.cssSelector("a[href]")
        };
        for (By by : itemSelectors) {
            if (!driver.findElements(by).isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
