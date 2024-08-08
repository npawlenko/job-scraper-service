package com.github.npawlenko.job.jobscraperservice.scraper;

import com.github.npawlenko.job.jobmodel.dto.JobOfferDTO;
import com.github.npawlenko.job.jobmodel.enums.JobSystem;
import com.github.npawlenko.job.jobscraperservice.config.properties.WebDriverProperties;
import com.github.npawlenko.job.jobscraperservice.exception.ScraperAuthenticationException;
import com.github.npawlenko.job.jobscraperservice.exception.ScraperException;
import com.github.npawlenko.job.jobscraperservice.scraper.jobsystem.AbstractJobSystemScraper;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class JobSystemScraper {

    private final WebDriverProperties webDriverProperties;
    private final List<AbstractJobSystemScraper> jobSystemScraperImplementations;

    public JobSystemScraper(WebDriverProperties webDriverProperties, List<AbstractJobSystemScraper> jobSystemScraperImplementations) {
        this.webDriverProperties = webDriverProperties;
        this.jobSystemScraperImplementations = jobSystemScraperImplementations;
    }

    public Set<JobOfferDTO> scrape(JobSystem jobSystem) {
        AbstractJobSystemScraper scraper = getScraperForJobSystem(jobSystem);
        Set<JobOfferDTO> jobOfferDTOS = Sets.newHashSet();
        try {
            jobOfferDTOS = scraper.scrapeJobOffers();
        } catch (ScraperAuthenticationException e) {
            log.error("Could not authenticate to job system. Check if your credentials are correct", e);
        } catch (ScraperException e) {
            log.error("Caught unexpected exception", e);
            throw new RuntimeException(e);
        }
        return jobOfferDTOS;
    }

    private AbstractJobSystemScraper getScraperForJobSystem(JobSystem jobSystem) {
        return jobSystemScraperImplementations.stream()
                .filter(impl -> impl.getJobSystem() == jobSystem)
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Scraper for this job system is not implemented"));
    }
}
