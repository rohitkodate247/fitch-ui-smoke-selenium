# Fitch Ratings UI Smoke (Selenium + Java)

A quick demo UI test suite for https://www.fitchratings.com/ using Selenium WebDriver (Java) and JUnit 5, running headless in GitHub Actions.

## What it does
- Opens the Fitch Ratings homepage
- Attempts to accept cookie/consent banners if present
- Verifies key UI elements render (header/footer)
- Performs a basic search and verifies search results page loads

## Tech
- Java 17, Maven
- Selenium 4 (Chrome headless)
- JUnit 5
- Page Object Model (POM) style

## Run locally
Prereqs: Java 17+, Maven

```bash
mvn test
