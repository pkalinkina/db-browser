package com.db.browser.postgre.service;

import com.db.browser.core.service.ConnectionDetailsProvider;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class PostgreConnectionDetailsProvider implements ConnectionDetailsProvider {
    private static final String URL_PATTERN = "jdbc:postgresql://%s:%d/%s";
    private static final String URL_PATTERN_NO_PORT = "jdbc:postgresql://%s/%s";


    @Override
    public String getDriverName() {
        return "org.postgresql.Driver";
    }

    @Override
    public String getUrl(String hostName, Integer port, String databaseName) {
        return port == null ?
                format(URL_PATTERN_NO_PORT, hostName, databaseName) :
                format(URL_PATTERN, hostName, port, databaseName);
    }
}
