package com.github.npawlenko.job.jobscraperservice.scraper.jobsystem.justjoinit;

import com.github.npawlenko.job.jobmodel.dto.JobOfferDTO;
import com.github.npawlenko.job.jobmodel.enums.JobSystem;
import com.github.npawlenko.job.jobscraperservice.config.properties.WebDriverProperties;
import com.github.npawlenko.job.jobscraperservice.scraper.annotation.Scraper;
import com.github.npawlenko.job.jobscraperservice.scraper.jobsystem.AbstractUnauthenticatedJobSystemScraper;
import com.github.npawlenko.job.jobscraperservice.scraper.jobsystem.XPaths;
import com.github.npawlenko.job.jobscraperservice.utils.ScrapingUtils;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Scraper
@Slf4j
@Getter
public class JustJoinItScraper extends AbstractUnauthenticatedJobSystemScraper {
    private static final String BASE_URL = "https://justjoin.it";

    private final JobSystem jobSystem = JobSystem.JustJoinIT;
    private int activeOfferIndex = 0;

    public JustJoinItScraper(WebDriverProperties webDriverProperties) {
        super(webDriverProperties);
    }

    @Override
    protected void navigateToJobList(WebDriver driver) {
        activeOfferIndex = 0;
        driver.manage().window().maximize();
        log.debug("Navigating to %s...".formatted(BASE_URL));
        driver.get(BASE_URL);
        ScrapingUtils.waitForPageLoad(driver);
        acceptCookies(driver);
    }

    private void acceptCookies(WebDriver driver) {
        log.debug("Accepting cookies...");
        List<WebElement> acceptAllCookiesButtonList = driver.findElements(By.xpath(XPaths.JustJoinIt.ACCEPT_COOKIES_BUTTON));
        if (acceptAllCookiesButtonList.size() == 0) {
            return;
        }
        acceptAllCookiesButtonList.get(0).click();
    }

    @Override
    protected boolean navigateToNextPage(WebDriver driver) {
        return scrollToObtainNextJobOffers(driver);
    }

    private boolean scrollToObtainNextJobOffers(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        if (activeOfferIndex > 0) {
            Optional<WebElement> element = ScrapingUtils.getElementWithoutException(driver, d -> d.findElement(By.xpath(XPaths.JustJoinIt.getJobOfferWithIndexParam(activeOfferIndex-1))));
            element.ifPresentOrElse(
                    (el) -> {
                        log.debug("Scrolling to next page");
                        js.executeScript("arguments[0].scrollIntoView(true);", el);
                    },
                    () -> {
                        throw new RuntimeException("Recently processed element not found in DOM");
                    }
            );
        }

        try {
            WebElement jobOfferElement = ScrapingUtils.waitUntilElementIsPresent(driver,
                    () -> ScrapingUtils.getElementWithoutException(driver, (d) -> d.findElement(By.xpath(XPaths.JustJoinIt.getJobOfferWithIndexParam(activeOfferIndex)))));
            WebDriverWait waitUntilJobOfferLoads = new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS));
            waitUntilJobOfferLoads.until((ExpectedCondition<Boolean>) d -> {
                String classAttribute = jobOfferElement.getAttribute("class");
                return !classAttribute.contains("MuiSkeleton-wave");
            });
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    protected Set<JobOfferDTO> extractJobOffersFromPage(WebDriver driver) {
        List<WebElement> jobOfferElements = driver.findElements(By.xpath(XPaths.JustJoinIt.JOB_OFFER));
        if (jobOfferElements.isEmpty()) {
            log.warn("No job offers found for page");
            return Sets.newHashSet();
        }
        return jobOfferElements.stream()
                .filter(el -> Integer.parseInt(el.getAttribute("data-index")) >= activeOfferIndex)
                .map(this::scrapeJobOffer)
                .collect(Collectors.toSet());
    }


    private JobOfferDTO scrapeJobOffer(WebElement jobOfferElement) {
        String name = ScrapingUtils.getText(jobOfferElement.findElement(By.cssSelector("h2")));
        String jobOfferUrl = jobOfferElement.findElement(By.cssSelector("*[class*='offer_list_offer_link']")).getAttribute("href");
        URL url = null;
        try {
            url = new URL(jobOfferUrl);
        } catch (MalformedURLException e) {
            log.error("Invalid url: %s".formatted(jobOfferUrl), e);
        }

        activeOfferIndex++;

        JobOfferDTO jobOfferDTO = JobOfferDTO.builder()
                .jobSystem(getJobSystem())
                .url(url)
                .name(name)
                .build();
        log.debug(jobOfferDTO.toString());
        return jobOfferDTO;
    }
}
