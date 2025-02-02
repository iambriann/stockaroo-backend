package com.stockaroo.asxnewsapp.scraper;

import com.stockaroo.asxnewsapp.model.Article;
import com.stockaroo.asxnewsapp.service.ArticleService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class AusTradingDayScraper extends AbstractSiteScraper {

    private final ArticleService articleService;

    public AusTradingDayScraper(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    @Scheduled(fixedRate = 300000)
    protected void performScraping() {
        driver.get("https://www.theaustralian.com.au/business");
        System.out.println(driver.toString());

        List<WebElement> newsElements = driver.findElements(By.xpath("//ul[@class='livecoverage-posts']"));

        System.out.println(newsElements);

        List<Article> articles = new ArrayList<>();

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
