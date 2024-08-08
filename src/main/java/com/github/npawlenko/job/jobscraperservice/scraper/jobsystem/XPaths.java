package com.github.npawlenko.job.jobscraperservice.scraper.jobsystem;

public class XPaths {
    public static String ELEMENT_WITH_CLASS = "//*[contains(concat(' ', normalize-space(@class), ' '), ' %s ')]";

    public static String getElementWithClass(String className) {
        return ELEMENT_WITH_CLASS.formatted(className);
    }

    public static class JustJoinIt {
        public static final String ACCEPT_COOKIES_BUTTON = "//*[@id=\"cookiescript_accept\"]";
        public static final String JOB_OFFER = "//div[@data-virtuoso-scroller='true']//div[@data-index]";
        public static final String JOB_OFFER_WITH_INDEX_PARAM = "//div[@data-virtuoso-scroller='true']//div[@data-index='%d']";

        public static String getJobOfferWithIndexParam(Integer index) {
            return JOB_OFFER_WITH_INDEX_PARAM.formatted(index);
        }
    }
}
