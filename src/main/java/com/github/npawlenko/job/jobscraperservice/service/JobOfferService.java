package com.github.npawlenko.job.jobscraperservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.npawlenko.job.jobmodel.dto.JobOfferDTO;
import com.github.npawlenko.job.jobmodel.enums.JobSystem;
import com.github.npawlenko.job.jobscraperservice.scraper.JobSystemScraper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;

@Service
@RequiredArgsConstructor
public class JobOfferService {

    private final JobSystemScraper scraper;
    private final Semaphore semaphore = new Semaphore(1);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void checkProcessingAvailability() {
        if (semaphore.availablePermits() == 0) {
            throw new RuntimeException("Other processing is already in progress");
        }
    }

    @Async
    public void scrapeJobOffers(List<JobSystem> jobSystems) {
        if (!semaphore.tryAcquire()) {
            throw new RuntimeException("Other processing is already in progress");
        }
        try {
            jobSystems.forEach(this::scrapeJobOffersForSystem);
        } finally {
            semaphore.release();
        }
    }

    private void scrapeJobOffersForSystem(JobSystem jobSystem) {
        Set<JobOfferDTO> jobOffers = scraper.scrape(jobSystem);

    }
}
