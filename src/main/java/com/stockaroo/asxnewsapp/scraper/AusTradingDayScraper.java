package com.stockaroo.asxnewsapp.scraper;

import com.stockaroo.asxnewsapp.model.Article;
import com.stockaroo.asxnewsapp.service.ArticleService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class AusTradingDayScraper implements BaseScraper {

    private final WebDriver driver;

    private final ArticleService articleService;

    public AusTradingDayScraper(ArticleService articleService, WebDriver driver) {
        this.driver = driver;
        this.articleService = articleService;
    }

    public void performScraping() {
        driver.get("https://theaustralian.com.au/business");

        List<WebElement> newsElements = driver.findElements(By.cssSelector("ul.livecoverage-posts > li"));

        for(WebElement e : newsElements) {
            Instant now = Instant.now();
            Article article = new Article(now,
                "The Australian Trading Day",
                e.findElement(By.className("livecoverage-posts_title")).getText(),
                "Not available",
                now,
                e.findElement(By.className("livecoverage-posts_title")).findElement(By.tagName("a")).getAttribute("href")
            );

            boolean isNewArticle = articleService.saveArticleIfNotExists(article);

            if(!isNewArticle) {
                break;
            }
        }

    }

}
