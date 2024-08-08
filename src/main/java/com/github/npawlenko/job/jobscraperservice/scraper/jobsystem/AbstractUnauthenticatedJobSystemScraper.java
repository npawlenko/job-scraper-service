package com.github.npawlenko.job.jobscraperservice.scraper.jobsystem;

import com.github.npawlenko.job.jobmodel.dto.JobOfferDTO;
import com.github.npawlenko.job.jobscraperservice.config.properties.WebDriverProperties;
import com.github.npawlenko.job.jobscraperservice.webdriver.WebDriverFactory;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.util.Set;

@Slf4j
public non-sealed abstract class AbstractUnauthenticatedJobSystemScraper extends AbstractJobSystemScraper {
    public AbstractUnauthenticatedJobSystemScraper(WebDriverProperties webDriverProperties) {
        super(webDriverProperties);
    }

    @Override
    public Set<JobOfferDTO> scrapeJobOffers() {
        WebDriver webDriver = WebDriverFactory.createWebDriver(webDriverProperties);
        try {
            return scrapeThroughAllPages(webDriver);
        } catch(RuntimeException e) {
            throw new RuntimeException("Encountered error scraping job system", e);
        } finally {
            webDriver.close();
        }
    }
}
