package com.github.npawlenko.job.jobscraperservice.webdriver.setup;

import com.github.npawlenko.job.jobscraperservice.utils.file.CompressionUtils;
import com.github.npawlenko.job.jobscraperservice.utils.file.WebDriverFileUtils;
import com.github.npawlenko.job.jobscraperservice.webdriver.WebDriverBrowser;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Slf4j
public class ChromeWebDriverSetupRunner extends WebDriverSetupRunner {
    protected static final String chromeDriverUrl = "https://storage.googleapis.com/chrome-for-testing-public/%1$s/%2$s/chromedriver-%2$s.zip";
    public static final String CHROMEDRIVER_FILE_NAME = "chromedriver";

    public ChromeWebDriverSetupRunner(String version) {
        super(WebDriverBrowser.CHROME, version);
    }

    @Override
    protected void setupWebDriver() {
        System.setProperty("webdriver.chrome.driver", getDriverFilePath());
    }

    @Override
    protected void getWebDriverFile() {
        File webdriverArchive = downloadWebdriverZipArchive();
        CompressionUtils.extractFileFromZipArchiveFirstMatch(
                webdriverArchive,
                CHROMEDRIVER_FILE_NAME + WebDriverFileUtils.getDriverFileExtension(),
                getDriverFile());
    }

    private File downloadWebdriverZipArchive() {
        byte[] data = downloadWebDriver(String.format(
                chromeDriverUrl,
                getVersion(),
                WebDriverFileUtils.getPlatform()));

        try {
            log.info("Uncompressing webdriver");
            Path tempFile = Files.createTempFile("chrome-webdriver-download", null);
            Files.write(tempFile, data, StandardOpenOption.WRITE);
            return new File(tempFile.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
