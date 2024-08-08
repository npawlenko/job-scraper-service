package com.github.npawlenko.job.jobscraperservice.utils.file;

import org.apache.commons.lang.SystemUtils;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WebDriverFileUtils {

    public static final String DRIVER_FILE_NAME = "webdriver";
    public static final Path DRIVER_ROOT_PATH;

    static {
        try {
            DRIVER_ROOT_PATH = Paths.get(WebDriverFileUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI()).resolve("driver").toAbsolutePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPlatform() {
        String platform = "";
        if(SystemUtils.IS_OS_WINDOWS) {
            platform += "win";
            if(SystemUtils.OS_ARCH.contains("64")) {
                platform += "64";
            }
            else {
                platform += "32";
            }
        }
        else if(SystemUtils.IS_OS_LINUX) {
            platform += "linux";
            if(SystemUtils.OS_ARCH.contains("64")) {
                platform += "64";
            }
            else {
                platform += "32";
            }
        }
        else if(SystemUtils.IS_OS_MAC) {
            platform += "mac-";
            if(SystemUtils.OS_ARCH.equals("aarch64")) {
                platform += "arm64";
            }
            else {
                platform += "x64";
            }
        }
        else {
            throw new RuntimeException("Unsupported OS");
        }
        return platform;
    }

    public static String getDriverPath(String fileNamePrefix, String fileNameSuffix) {
        return
                DRIVER_ROOT_PATH +
                        SystemUtils.FILE_SEPARATOR +
                        fileNamePrefix +
                        DRIVER_FILE_NAME +
                        fileNameSuffix +
                        getDriverFileExtension();
    }

    public static String getDriverFileExtension() {
        if(SystemUtils.IS_OS_WINDOWS) {
            return ".exe";
        }
        else {
            return "";
        }
    }
}
