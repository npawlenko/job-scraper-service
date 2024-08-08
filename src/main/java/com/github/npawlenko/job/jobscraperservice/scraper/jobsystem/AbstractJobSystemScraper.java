package com.github.npawlenko.job.jobscraperservice.scraper.jobsystem;

import com.github.npawlenko.job.jobmodel.dto.JobOfferDTO;
import com.github.npawlenko.job.jobmodel.enums.JobSystem;
import com.github.npawlenko.job.jobscraperservice.config.properties.WebDriverProperties;
import com.github.npawlenko.job.jobscraperservice.exception.ScraperException;
import com.google.common.collect.Sets;
import jakarta.annotation.Nullable;
import lombok.NonNull;
import org.openqa.selenium.WebDriver;

import java.util.Set;

public abstract sealed class AbstractJobSystemScraper
        permits AbstractUnauthenticatedJobSystemScraper, AbstractAuthenticatedJobSystemScraper{
    protected final WebDriverProperties webDriverProperties;

    public AbstractJobSystemScraper(WebDriverProperties webDriverProperties) {
        this.webDriverProperties = webDriverProperties;
    }

    /**
     * @return JobSystem enum
     */
    @NonNull
    public abstract JobSystem getJobSystem();

    /**
     * Navigates and scrapes through all pages in job system
     * @return Set of job offers
     */
    protected Set<JobOfferDTO> scrapeThroughAllPages(WebDriver webDriver) {
        Set<JobOfferDTO> result = Sets.newHashSet();

        navigateToJobList(webDriver);
        do {
            Set<JobOfferDTO> jobOffers = extractJobOffersFromPage(webDriver);
            if (jobOffers != null) {
                result.addAll(jobOffers);
            }
        } while (navigateToNextPage(webDriver));

        return result;
    }

    /**
     * Navigates to first page of job offer list
     */
    protected abstract void navigateToJobList(WebDriver driver);

    /**
     * Navigates to next page with job offers
     * @return Returns true if there are more pages and false otherwise.
     */
    protected abstract boolean navigateToNextPage(WebDriver driver);

    /**
     * Extracts job offers from active page
     * @return Set of job offers present on active page
     */
    @Nullable
    protected abstract Set<JobOfferDTO> extractJobOffersFromPage(WebDriver driver);

    /**
     * Extracts all job offers from job system
     * @return Set of all job offers in job system
     * @throws ScraperException on site with incorrect/changed DOM structure or other faults
     */
    @Nullable
    public abstract Set<JobOfferDTO> scrapeJobOffers() throws ScraperException;
}
