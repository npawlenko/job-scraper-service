package com.github.npawlenko.job.jobscraperservice.scraper.jobsystem;

import com.github.npawlenko.job.jobmodel.dto.JobOfferDTO;
import com.github.npawlenko.job.jobscraperservice.config.properties.WebDriverProperties;
import com.github.npawlenko.job.jobscraperservice.exception.ScraperAuthenticationException;
import com.github.npawlenko.job.jobscraperservice.exception.ScraperException;
import com.github.npawlenko.job.jobscraperservice.webdriver.WebDriverFactory;
import org.openqa.selenium.WebDriver;

import java.util.Set;

public non-sealed abstract class AbstractAuthenticatedJobSystemScraper extends AbstractJobSystemScraper {
    public AbstractAuthenticatedJobSystemScraper(WebDriverProperties webDriverProperties) {
        super(webDriverProperties);
    }

    @Override
    public Set<JobOfferDTO> scrapeJobOffers() throws ScraperException {
        WebDriver webDriver = WebDriverFactory.createWebDriver(webDriverProperties);
        authenticate(webDriver);
        Set<JobOfferDTO> result = scrapeThroughAllPages(webDriver);
        webDriver.close();
        return result;
    }

    /**
     * Authenticates user in job system
     * @param webDriver
     * @throws ScraperAuthenticationException
     */
    public abstract void authenticate(WebDriver webDriver) throws ScraperAuthenticationException;
}
