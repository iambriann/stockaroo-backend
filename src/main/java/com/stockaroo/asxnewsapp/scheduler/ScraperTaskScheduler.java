package com.stockaroo.asxnewsapp.scheduler;

import com.stockaroo.asxnewsapp.scraper.AbstractSiteScraper;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScraperTaskScheduler {

    private final List<AbstractSiteScraper> scrapers;
    private final ScheduledExecutorService threadPool;
    private final Random random;

    public ScraperTaskScheduler(List<AbstractSiteScraper> scrapers) {
        this.scrapers = scrapers;
        this.threadPool = Executors.newScheduledThreadPool(scrapers.size());
        this.random = new Random();
    }

    public void startScheduling() {
        scrapers.forEach(this::scheduleScraper);
    }

    private void scheduleScraper(AbstractSiteScraper scraper) {
        long initialDelay = getRandomDelay();
        System.out.println("Scheduling scraper for URL: " + scraper.toString() + " with initial delay " + initialDelay + " ms");
        threadPool.scheduleAtFixedRate(() -> {
            LocalTime now = LocalTime.now();
            DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
            // Only run during weekdays (Monday - Friday) from 7 AM to 9 PM
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                if (now.isAfter(LocalTime.of(7, 0)) && now.isBefore(LocalTime.of(21, 0))) {
                    scraper.scrape();
                    System.out.println("Next scrape for " + scraper.toString());
                } else {
                    System.out.println("Outside of allowed time window (7 AM - 9 PM). Skipping.");
                }
            } else {
                System.out.println("It's the weekend. Skipping scrape.");
            }
        }, initialDelay, getRandomDelay(), TimeUnit.MILLISECONDS);
    }

    private long getRandomDelay() {
        return 60000L + random.nextInt(120000); // Random delay between 1 and 3 minutes
    }

    public void shutdown() {
        threadPool.shutdown();
        System.out.println("Scraper scheduler shutdown.");
    }


}
