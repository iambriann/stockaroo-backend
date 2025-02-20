package com.stockaroo.asxnewsapp.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.stockaroo.asxnewsapp.scheduler.ScraperTaskScheduler;
import com.stockaroo.asxnewsapp.scraper.AfrStreetTalkScraper;
import com.stockaroo.asxnewsapp.scraper.AsxScraper;
import com.stockaroo.asxnewsapp.scraper.AusTradingDayScraper;
import com.stockaroo.asxnewsapp.scraper.BaseScraper;
import com.stockaroo.asxnewsapp.service.ArticleService;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    @Bean
    public MongoClient mongoClient() {
        // Replace with your MongoDB connection URI
        return MongoClients.create(connectionString);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, "stockaroo");
    }

    @Bean
    public WebDriver webDriver() {
        // Set up Chrome options
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless"); // Updated headless mode for Chrome
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("--incognito");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        return new FirefoxDriver(options);
    }


    @Bean
    @Scope("prototype")
    public ArticleService articleService(MongoTemplate mongoTemplate) {
        return new ArticleService(mongoTemplate);
    }

    @Bean
    public BaseScraper afrStreetTalkScraper(ArticleService articleService, WebDriver driver) {
        return new AfrStreetTalkScraper(articleService, driver);
    }

    @Bean
    public BaseScraper ausTradingDayScraper(ArticleService articleService, WebDriver driver) {
        return new AusTradingDayScraper(articleService, driver);
    }

    @Bean
    public BaseScraper asxScraper(ArticleService articleService, WebDriver driver) {
        return new AsxScraper(articleService, driver);
    }

    @Bean
    public ScraperTaskScheduler scraperTaskScheduler(List<BaseScraper> scrapers) {
        ScraperTaskScheduler scheduler = new ScraperTaskScheduler(scrapers);
        scheduler.startScheduling();
        return scheduler;
    }

}
