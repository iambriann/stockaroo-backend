package com.stockaroo.asxnewsapp.scraper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public abstract class AbstractSiteScraper implements BaseScraper {
    protected WebDriver driver;

    public AbstractSiteScraper() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        this.driver = new ChromeDriver(options);
    }

    protected abstract void performScraping();

    @Override
    public void scrape() {
        try {
            performScraping();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
