package com.stockaroo.asxnewsapp.scraper;

import com.stockaroo.asxnewsapp.model.Article;
import com.stockaroo.asxnewsapp.service.ArticleService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.util.List;

public class AsxScraper extends AbstractSiteScraper {

    private final ArticleService articleService;

    public AsxScraper(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    protected void performScraping() {
        driver.get("https://www.asx.com.au/markets/trade-our-cash-market/announcements");

        List<WebElement> newsElements = driver.findElement(By.cssSelector(".table.table-bordered")).findElement(By.tagName("tbody")).findElements(By.tagName("tr"));

        for (WebElement e : newsElements) {

            String priceSensitive = e.findElement(By.className("price-sensitive")).getText();
            if(priceSensitive.equals("no")) {
                continue;
            }

            Instant now = Instant.now();
            Article article = new Article(now,
                    "ASX Announcement",
                    e.findElement(By.xpath("./td[2]")).getText() + " " + e.findElement(By.cssSelector(".list.hidden-xs")).getText(),
                    e.findElement(By.xpath("./td[6]")).findElement(By.tagName("a")).getText().split("\n")[0],
                    now,
                    e.findElement(By.xpath("./td[6]")).findElement(By.tagName("a")).getAttribute("href")
            );

//            boolean isNewArticle = articleService.saveArticleIfNotExists(article);
//
//            if (!isNewArticle) {
//                break;
//            }
        }
    }
}
