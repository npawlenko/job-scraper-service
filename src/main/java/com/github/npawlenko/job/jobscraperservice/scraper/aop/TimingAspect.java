package com.github.npawlenko.job.jobscraperservice.scraper.aop;

import com.github.npawlenko.job.jobmodel.enums.JobSystem;
import com.github.npawlenko.job.jobscraperservice.scraper.jobsystem.AbstractJobSystemScraper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Aspect
@Component
@Slf4j
public class TimingAspect {
    @Pointcut("execution(* com.github.npawlenko.job.jobscraperservice.scraper.jobsystem.AbstractJobSystemScraper.scrapeJobOffers(..))")
    public void scrapeThroughAllPagesPointcut() {
    }

    @Around("scrapeThroughAllPagesPointcut()")
    public Object aroundScrapeThroughAllPages(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object target = joinPoint.getTarget();
        JobSystem jobSystem = ((AbstractJobSystemScraper) target).getJobSystem();

        log.info("Starting scraping job offers for {}", jobSystem);
        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            Duration duration = Duration.of(endTime - startTime, ChronoUnit.MILLIS);

            log.info("Execution time of scraping {} is {}", jobSystem, duration.toString());
        }
    }
}
