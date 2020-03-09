package com.db.browser.core.service;


public interface ConnectionDetailsProvider {
    /**
     * Implementations should return driver's class.
     */
    String getDriverName();

    /**
     * Implementations should return valid connection url.
     */
    String getUrl(String hostName, Integer port, String databaseName);
}
