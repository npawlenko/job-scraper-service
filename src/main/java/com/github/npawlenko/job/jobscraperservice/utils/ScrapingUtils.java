package com.github.npawlenko.job.jobscraperservice.utils;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public class ScrapingUtils {

    public static void waitForPageLoad(WebDriver wdriver) {
        log.debug("Waiting for page load...");
        WebDriverWait wait = new WebDriverWait(wdriver, Duration.of(30, ChronoUnit.SECONDS));
        Function<WebDriver, Boolean> pageLoaded =
                input -> ((JavascriptExecutor) input)
                        .executeScript("return document.readyState")
                        .equals("complete");
        wait.until(pageLoaded);
        log.debug("Page was loaded");
    }

    public static Optional<WebElement> getElementWithoutException(SearchContext searchContext, Function<SearchContext, WebElement> findFunction) {
        WebElement webElement = null;
        try {
            webElement = findFunction.apply(searchContext);
        } catch (NoSuchElementException ignored) {
        }

        return Optional.ofNullable(webElement);
    }

    public static WebElement waitUntilElementIsPresent(WebDriver driver, Supplier<Optional<WebElement>> elementSupplier) {
        WebDriverWait waitUntilElementIsVisible = new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS));
        waitUntilElementIsVisible.until((ExpectedCondition<Boolean>) d -> {
            Optional<WebElement> elementOptional = elementSupplier.get();
            return elementOptional.isPresent();
        });
        //noinspection OptionalGetWithoutIsPresent
        return elementSupplier.get().get();
    }

    public static String getText(@Nullable WebElement webElement) {
        return Optional.ofNullable(webElement)
                .map(WebElement::getText)
                .orElse("");
    }

    public static Integer getInteger(@Nullable WebElement webElement) {
        String text = transformWebElementText(webElement, ScrapingUtils::removeNonDigits);
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            log.error("Could not get integer from webElement text: '%s'".formatted(text), e);
            log.warn("Returning 0");
            return 0;
        }
    }

    private static String transformWebElementText(@Nullable WebElement webElement, Function<String, String> transformFunction) {
        return transformFunction.apply(getText(webElement));
    }

    private static String removeNonDigits(String s) {
        return s.replaceAll("\\D", "");
    }
}
