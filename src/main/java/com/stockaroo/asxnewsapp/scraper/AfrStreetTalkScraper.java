package com.stockaroo.asxnewsapp.scraper;

import com.stockaroo.asxnewsapp.model.Article;
import com.stockaroo.asxnewsapp.service.ArticleService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class AfrStreetTalkScraper extends AbstractSiteScraper {

    private final ArticleService articleService;

    public AfrStreetTalkScraper(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    @Scheduled(fixedRate = 300000)
    protected void performScraping() {
        driver.get("https://afr.com/street-talk");
        System.out.println(driver.getTitle());

        List<WebElement> newsElements = driver.findElements(By.cssSelector("[data-testid='MarketSimpleStoryTile']"));

        for(WebElement e : newsElements) {
            Instant now = Instant.now();
            Article article = new Article(now,
                "AFR Street Talk",
                e.findElement(By.cssSelector("[data-testid='StoryTileHeadline-h3']")).getText(),
                e.findElement(By.cssSelector("[data-pb-type=ab]")).getText(),
                //System.out.println(e.findElement(By.cssSelector("[data-testid='StoryTile-Timestamp']")).getAttribute("datetime"));
                now, e.findElement(By.cssSelector("[data-testid='StoryTileHeadline-h3']")).findElement(By.tagName("a")).getAttribute("href")
            );

            boolean isNewArticle = articleService.saveArticleIfNotExists(article);

            if(!isNewArticle) {
                break;
            }
        }

    }

}
