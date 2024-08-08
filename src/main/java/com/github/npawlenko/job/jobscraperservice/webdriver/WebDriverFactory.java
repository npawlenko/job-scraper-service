package com.github.npawlenko.job.jobscraperservice.webdriver;

import com.github.npawlenko.job.jobscraperservice.config.properties.WebDriverProperties;
import lombok.NonNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverFactory {
    public static WebDriver createWebDriver(@NonNull WebDriverProperties webDriverProperties) {
        WebDriverBrowser browser = webDriverProperties.getBrowser();
        if (browser == null) {
            throw new IllegalArgumentException("Used browser not defined");
        }

        return switch(webDriverProperties.getBrowser()) {
            case CHROME -> new ChromeDriver();
        };
    }
}
