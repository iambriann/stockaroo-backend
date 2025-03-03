package com.stockaroo.asxnewsapp.scraper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public abstract class AbstractSiteScraper implements BaseScraper {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSiteScraper.class);

    protected WebDriver driver;
    private Instant driverCreationTime;
    private String driverName;

    public AbstractSiteScraper(String name) {
        this.driverName = name;
        startDriver();
    }

    protected abstract void performScraping();

    @Override
    public void scrape() {
        performScraping();
    }

    public void startDriver() {
        if(driver != null) {
            stopDriver();
        }

        logger.info("Creating new driver {}", driverName);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // Updated headless mode for Chrome
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        this.driver = new ChromeDriver(options);
        this.driverCreationTime = Instant.now();
    }

    public void stopDriver() {
        if(driver != null) {
            try {
                driver.quit();
                logger.info("Webdriver stopped successfully!");
            } catch (Exception e) {
                logger.info("Error with stopping Webdriver: {}!", e.getMessage());
            } finally {
                driver = null;
                driverCreationTime = null;
            }
        }
    }

    public void restartDriver() {
        logger.info("Restarting the webdriver ... ");
        stopDriver();
        startDriver();
    }

    public Instant getDriverCreationTime() {
        return driverCreationTime;
    }

    public String getDriverName() {
        return driverName;
    }

}
