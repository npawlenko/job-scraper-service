package com.github.npawlenko.job.jobscraperservice.scraper.jobsystem.rocketjobs;

import com.github.npawlenko.job.jobmodel.dto.JobOfferDTO;
import com.github.npawlenko.job.jobmodel.enums.JobSystem;
import com.github.npawlenko.job.jobscraperservice.config.properties.WebDriverProperties;
import com.github.npawlenko.job.jobscraperservice.scraper.annotation.Scraper;
import com.github.npawlenko.job.jobscraperservice.scraper.jobsystem.AbstractUnauthenticatedJobSystemScraper;
import lombok.Getter;
import org.openqa.selenium.WebDriver;

import java.util.Set;

// Not implemented yet
//@Scraper
@Getter
public class RocketJobsScraper extends AbstractUnauthenticatedJobSystemScraper {
    private final JobSystem jobSystem = JobSystem.RocketJobs;

    public RocketJobsScraper(WebDriverProperties webDriverProperties) {
        super(webDriverProperties);
    }

    @Override
    protected void navigateToJobList(WebDriver driver) {

    }

    @Override
    protected boolean navigateToNextPage(WebDriver driver) {
        return false;
    }

    @Override
    protected Set<JobOfferDTO> extractJobOffersFromPage(WebDriver driver) {
        return null;
    }
}
