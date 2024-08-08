package com.github.npawlenko.job.jobscraperservice.config;

import com.github.npawlenko.job.jobscraperservice.config.properties.WebDriverProperties;
import com.github.npawlenko.job.jobscraperservice.webdriver.setup.ChromeWebDriverSetupRunner;
import com.github.npawlenko.job.jobscraperservice.webdriver.setup.WebDriverSetupRunner;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@RequiredArgsConstructor
public class WebDriverConfig {

    private final WebDriverProperties webDriverProperties;

    @Bean
    public WebDriverSetupRunner webDriverSetupRunner() {
        return switch (webDriverProperties.getBrowser()) {
            case CHROME -> new ChromeWebDriverSetupRunner(webDriverProperties.getVersion());
        };
    }
}
