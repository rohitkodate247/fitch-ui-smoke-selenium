package com.rohit.fitch.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Minimal POM for Fitch Ratings homepage (hardened for CI) */
public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public HomePage(WebDriver driver, int waitTimeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(waitTimeoutSeconds));
    }

    public HomePage open(String baseUrl) {
        driver.navigate().to(baseUrl);
        waitForDocumentComplete();
        return this;
    }

    /** Waits for the document to be fully loaded. */
    private void waitForDocumentComplete() {
        try {
            wait.until((ExpectedCondition<Boolean>) d -> "complete"
                    .equals(((JavascriptExecutor) d).executeScript("return document.readyState")));
        } catch (Exception ignored) {
        }
    }

    /**
     * Dismiss cookie/consent banners if present, and wait for them to disappear.
     */
    public void acceptCookiesIfPresent() {
        String[] primarySelectors = new String[] {
                "#onetrust-accept-btn-handler",
                "button#onetrust-accept-btn-handler",
                "button[aria-label='Accept cookies']",
                "button[aria-label='Accept Cookies']",
                "button.cookie-accept",
                "button[title='Accept']",
                "button[class*='accept'][class*='cookie']"
        };

        boolean clicked = false;

        // Try clickable buttons first
        for (String css : primarySelectors) {
            try {
                WebElement el = new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.elementToBeClickable(By.cssSelector(css)));
                el.click();
                clicked = true;
                break;
            } catch (TimeoutException | ElementClickInterceptedException ignored) {
            }
        }

        // Fallback by text
        if (!clicked) {
            try {
                WebElement byText = driver.findElement(By.xpath(
                        "//button[contains(.,'Accept') or contains(.,'I Agree') or contains(.,'I agree')]"));
                if (byText.isDisplayed()) {
                    byText.click();
                    clicked = true;
                }
            } catch (NoSuchElementException ignored) {
            }
        }

        // If we clicked something, wait for common banner containers to become
        // invisible
        if (clicked) {
            try {
                new WebDriverWait(driver, Duration.ofSeconds(7)).until(
                        ExpectedConditions.invisibilityOfElementLocated(
                                By.cssSelector(
                                        "#onetrust-banner-sdk, .ot-sdk-container, [id*='cookie'], [class*='cookie']")));
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * More resilient check: header OR banner region OR logo visible, after the page
     * is ready and cookies handled.
     */
    public boolean isHeaderVisible() {
        try {
            waitForDocumentComplete();
            acceptCookiesIfPresent();

            // Common header candidates
            By[] headerCandidates = new By[] {
                    By.cssSelector("header"),
                    By.cssSelector("[role='banner']"),
                    By.cssSelector("div[class*='header'], nav[class*='header']")
            };

            for (By by : headerCandidates) {
                try {
                    WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                    if (el.isDisplayed())
                        return true;
                } catch (TimeoutException ignored) {
                }
            }

            // Fallback: look for logo/brand near the top
            By[] logoCandidates = new By[] {
                    By.cssSelector("img[alt*='Fitch' i]"),
                    By.cssSelector("a[aria-label*='Fitch' i]"),
                    By.cssSelector("[class*='logo' i] img, [class*='logo' i] svg")
            };
            for (By by : logoCandidates) {
                if (!driver.findElements(by).isEmpty())
                    return true;
            }

            // Last resort: if we can see a visible nav at the top
            try {
                WebElement nav = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("nav")));
                return nav.isDisplayed();
            } catch (TimeoutException ignored) {
            }

            return false;
        } catch (Exception e) {
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
                By.cssSelector("button[aria-label*='Search' i]"),
                By.cssSelector("button[title*='Search' i]"),
                By.cssSelector("button[class*='search' i]"),
                By.cssSelector("a[href*='search' i]")
        };
        for (By by : candidates) {
            try {
                WebElement el = wait.until(ExpectedConditions.elementToBeClickable(by));
                el.click();
                return;
            } catch (TimeoutException | ElementClickInterceptedException ignored) {
            }
        }
        // Keyboard fallback
        try {
            driver.findElement(By.tagName("body")).sendKeys("s");
        } catch (Exception ignored) {
        }
    }

    public void typeAndSubmitSearch(String query) {
        By[] inputs = new By[] {
                By.cssSelector("input[type='search']"),
                By.cssSelector("input[name='q' i]"),
                By.cssSelector("input[placeholder*='Search' i]"),
                By.cssSelector("input[class*='search' i]")
        };

        WebElement box = null;
        for (By by : inputs) {
            try {
                box = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                if (box.isDisplayed())
                    break;
            } catch (TimeoutException ignored) {
            }
        }
        if (box == null) {
            // As a last resort, try the first input
            box = driver.findElement(By.cssSelector("input"));
        }

        try {
            box.clear();
        } catch (Exception ignored) {
        }
        box.sendKeys(query);
        box.sendKeys(Keys.ENTER);
    }

    public String getTitle() {
        return driver.getTitle();
    }
}
