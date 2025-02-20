package com.stockaroo.asxnewsapp.scraper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public abstract class AbstractSiteScraper implements BaseScraper {
    protected WebDriver driver;

    public AbstractSiteScraper() {
        System.out.println("Creating new driver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // Updated headless mode for Chrome
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        this.driver = new ChromeDriver(options);
    }

    protected abstract void performScraping();

    @Override
    public void scrape() {
        performScraping();
//        try {
//            performScraping();
//        } finally {
//            if (driver != null) {
//                driver.quit();
//            }
//        }
    }
}
