package com.stockaroo.asxnewsapp.scraper;

import com.stockaroo.asxnewsapp.model.Article;
import com.stockaroo.asxnewsapp.service.ArticleService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class AsxScraper extends AbstractSiteScraper {

    private final ArticleService articleService;

    public AsxScraper(ArticleService articleService) {
        super("ASX Announcement Scraper");
        this.articleService = articleService;
    }

    @Override
    protected void performScraping() {
        driver.get("https://www.asx.com.au/markets/trade-our-cash-market/announcements");
        System.out.println(driver.getTitle());
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("onetrust-accept-btn-handler")));
//        acceptButton.click();

        List<WebElement> newsElements = driver.findElement(By.cssSelector(".table.table-bordered")).findElement(By.tagName("tbody")).findElements(By.tagName("tr"));

        for (WebElement e : newsElements) {

            String priceSensitive = e.findElement(By.className("price-sensitive")).getText();
            if(priceSensitive.equals("no")) {
                continue;
            }

            Instant now = Instant.now();
            Article article = new Article(now,
                    "ASX Announcement",
                    //e.findElement(By.xpath("./td[2]")).getText() + " ASX announcement for " + e.findElement(By.cssSelector(".list.hidden-xs")).getText(),
                    "ASX announcement for " + e.findElement(By.cssSelector(".list.hidden-xs")).getText(),
                    e.findElement(By.xpath("./td[6]")).findElement(By.tagName("a")).getText().split("\n")[0],
                    now,
                    e.findElement(By.xpath("./td[6]")).findElement(By.tagName("a")).getAttribute("href")
            );

            boolean isNewArticle = articleService.saveArticleIfNotExists(article);
//
//            if (!isNewArticle) {
//                break;
//            }
        }
    }
}
