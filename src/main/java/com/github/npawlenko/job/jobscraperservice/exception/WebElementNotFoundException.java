package com.github.npawlenko.job.jobscraperservice.exception;

public class WebElementNotFoundException extends RuntimeException {
    public WebElementNotFoundException() {
        super();
    }

    public WebElementNotFoundException(String message) {
        super(message);
    }
}
