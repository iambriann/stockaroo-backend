package com.stockaroo.asxnewsapp.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.stockaroo.asxnewsapp.scheduler.ScraperTaskScheduler;
import com.stockaroo.asxnewsapp.scraper.AbstractSiteScraper;
import com.stockaroo.asxnewsapp.scraper.AfrStreetTalkScraper;
import com.stockaroo.asxnewsapp.scraper.AsxScraper;
import com.stockaroo.asxnewsapp.scraper.AusTradingDayScraper;
import com.stockaroo.asxnewsapp.service.ArticleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
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

    @Value("${CORS_ORIGIN_URL}")
    private String corsOriginUrl;

    @Value("${CORS_ORIGIN_URL_WWW}")
    private String corsOriginUrlWWW;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(corsOriginUrl, corsOriginUrlWWW));  // Dynamic origin URL
        config.addAllowedMethod("*");  // Allow all HTTP methods
        config.addAllowedHeader("*");  // Allow all headers
        return new CorsFilter(request -> config);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, "stockaroo");
    }


    @Bean
    @Scope("prototype")
    public ArticleService articleService(MongoTemplate mongoTemplate) {
        return new ArticleService(mongoTemplate);
    }

    @Bean
    public AbstractSiteScraper afrStreetTalkScraper(ArticleService articleService) {
        return new AfrStreetTalkScraper(articleService);
    }

    @Bean
    public AbstractSiteScraper ausTradingDayScraper(ArticleService articleService) {
        return new AusTradingDayScraper(articleService);
    }

//    @Bean
//    public AbstractSiteScraper asxScraper(ArticleService articleService) {
//        return new AsxScraper(articleService);
//    }

    @Bean
    public ScraperTaskScheduler scraperTaskScheduler(List<AbstractSiteScraper> scrapers) {
        ScraperTaskScheduler scheduler = new ScraperTaskScheduler(scrapers);
        scheduler.startScheduling();
        return scheduler;
    }

}
