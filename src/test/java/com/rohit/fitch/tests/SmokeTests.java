package com.rohit.fitch.tests;

import com.rohit.fitch.BaseTest;
import com.rohit.fitch.pages.HomePage;
import com.rohit.fitch.pages.SearchResultsPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SmokeTests extends BaseTest {

    @Test
    @DisplayName("Homepage loads: title, header, footer, and cookies handled")
    void homepageLoads() {
        HomePage home = new HomePage(driver, waitTimeoutSeconds)
                .open(baseUrl);
        home.acceptCookiesIfPresent();

        String title = home.getTitle();
        assertNotNull(title, "Page title should not be null");
        assertFalse(title.isBlank(), "Page title should not be blank");

        assertTrue(home.isHeaderVisible(), "Header should be visible");
        assertTrue(home.isFooterVisible(), "Footer should be visible");
    }

    @Test
    @DisplayName("Search flow returns a results page with items")
    void searchWorks() {
        HomePage home = new HomePage(driver, waitTimeoutSeconds)
                .open(baseUrl);
        home.acceptCookiesIfPresent();
        home.openSearch();
        home.typeAndSubmitSearch("credit rating");

        SearchResultsPage results = new SearchResultsPage(driver, waitTimeoutSeconds);
        assertTrue(results.isLoaded(), "Results page should load");
        assertTrue(results.hasAtLeastOneResult(), "There should be at least one result");
    }
}
