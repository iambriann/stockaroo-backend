package com.stockaroo.asxnewsapp.scheduler;

import com.stockaroo.asxnewsapp.scraper.AbstractSiteScraper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScraperTaskScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ScraperTaskScheduler.class);

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
        logger.info("Scheduling scraper for URL: {} with initial delay {} seconds", scraper.toString(), initialDelay * 1000);
        threadPool.scheduleAtFixedRate(() -> {
            ZoneId sydneyZone = ZoneId.of("Australia/Sydney");
            ZonedDateTime now = ZonedDateTime.now(sydneyZone);
            LocalTime currentTime = now.toLocalTime();
            DayOfWeek dayOfWeek = now.getDayOfWeek();
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                if(scraper.getDriverName().equals("ASX Announcement Scraper")) {
                    restartOrScrapeHelper(scraper, currentTime, LocalTime.of(10, 0), LocalTime.of(16, 20));
                } else {
                    restartOrScrapeHelper(scraper, currentTime, LocalTime.of(7, 0), LocalTime.of(17, 20));
                }
            } else {
                logger.info("It's the weekend. Skipping scrape. Go enjoy it! Driver: {}", scraper.getDriverName());
            }
        }, initialDelay, getRandomDelay(), TimeUnit.MILLISECONDS);
    }

    private long getRandomDelay() {
        return 61000L + random.nextInt(140000); // Random delay between 1 and 2.5 minutes
    }

    public void shutdown() {
        threadPool.shutdown();
        logger.info("Scraper scheduler shutdown.");
    }

    public void restartOrScrapeHelper(AbstractSiteScraper scraper, LocalTime current, LocalTime start, LocalTime end) {
        if(current.isAfter(start) && current.isBefore(end)) {
            if(Instant.now().minus(20, ChronoUnit.MINUTES).isAfter(scraper.getDriverCreationTime())){
                logger.info("Restarting {} driver due to 20 minutes past driver creation", scraper.getDriverName());
                scraper.restartDriver();
            } else {
                logger.info("{} Driver due to restart in {} minutes past driver creation", scraper.getDriverName(), ChronoUnit.MINUTES.between(scraper.getDriverCreationTime(), Instant.now()));
            }
            scraper.scrape();
            logger.info("Scrape for {} just completed", scraper.getDriverName());
        } else {
            logger.info("Outside of allowed time window ({} - {}). Skipping. Driver: {}", start, end, scraper.getDriverName());
        }

    }


}
