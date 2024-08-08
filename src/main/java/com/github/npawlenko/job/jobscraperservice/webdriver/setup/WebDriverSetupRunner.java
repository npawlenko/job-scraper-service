package com.github.npawlenko.job.jobscraperservice.webdriver.setup;

import com.github.npawlenko.job.jobscraperservice.exception.WebDriverException;
import com.github.npawlenko.job.jobscraperservice.webdriver.WebDriverBrowser;
import com.github.npawlenko.job.jobscraperservice.utils.file.WebDriverFileUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.io.File;

@Slf4j
@Getter
public abstract class WebDriverSetupRunner implements ApplicationRunner {
    private static final String OPTION_DOWNLOAD_WEBDRIVER = "download-webdriver";

    private final WebDriverBrowser browser;
    private final String version;
    private final File driverFile;

    public WebDriverSetupRunner(@NonNull WebDriverBrowser browser, @Nullable String version) {
        this.browser = browser;
        this.version = version;
        this.driverFile = new File(getDriverFilePath());
    }

    @Override
    public void run(ApplicationArguments args) throws WebDriverException {
        log.info("Initializing web driver");
        boolean downloadWebDriverEnabled = isDownloadWebDriverEnabled(args);

        if (!driverFile.exists()) {
            if (!downloadWebDriverEnabled) {
                throw new WebDriverException("Web driver file not present");
            }

            log.warn("Web driver was not found");
            File parentFile = getDriverFile().getParentFile();
            if (!parentFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                parentFile.mkdirs();
            }
            if (getVersion() == null) {
                throw new WebDriverException("Browser version was not specified");
            }

            log.warn("Downloading web driver...");
            getWebDriverFile();
        }

        log.info("Setting up web driver");
        setupWebDriver();
        log.info("Web driver initialization complete");
    }

    private boolean isDownloadWebDriverEnabled(ApplicationArguments args) {
        return args.getOptionNames().contains(OPTION_DOWNLOAD_WEBDRIVER);
    }

    protected byte[] downloadWebDriver(String uri) {
        RestClient restClient = RestClient.create();
        try {
            return restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(byte[].class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new RuntimeException("Web driver with specified version was not found");
            }
            throw e;
        }
    }

    protected abstract void setupWebDriver();

    protected void getWebDriverFile() {
        throw new UnsupportedOperationException("Downloading web driver not implemented");
    }

    protected String getDriverFilePath() {
        return WebDriverFileUtils.getDriverPath(this.browser.toString().toLowerCase() + "-", "-" + version);
    }

    protected File getDriverFile() {
        return driverFile;
    }
}
