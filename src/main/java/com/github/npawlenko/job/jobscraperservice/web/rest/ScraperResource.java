package com.github.npawlenko.job.jobscraperservice.web.rest;

import com.github.npawlenko.job.jobmodel.enums.JobSystem;
import com.github.npawlenko.job.jobscraperservice.service.JobOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/scraper")
@RequiredArgsConstructor
public class ScraperResource {

    private final JobOfferService service;

    @PostMapping("/force/all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getJobOffers() {
        service.checkProcessingAvailability();
        service.scrapeJobOffers(
                Arrays.stream(JobSystem.values()).toList());
    }

    @PostMapping("/force/selected")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getJobOffers(@RequestBody List<JobSystem> jobSystemList) {
        service.checkProcessingAvailability();
        service.scrapeJobOffers(jobSystemList);
    }

    @PostMapping("/force/selected/{jobSystem}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getJobOffers(@PathVariable JobSystem jobSystem) {
        service.checkProcessingAvailability();
        service.scrapeJobOffers(List.of(jobSystem));
    }
}
