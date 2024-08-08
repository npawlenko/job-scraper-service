package com.github.npawlenko.job.jobscraperservice.config.properties;

import com.github.npawlenko.job.jobscraperservice.webdriver.WebDriverBrowser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.webdriver")
@Getter
@Setter
public class WebDriverProperties {
    private WebDriverBrowser browser;
    private String version;
}
