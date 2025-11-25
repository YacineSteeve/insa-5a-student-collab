package com.blyweertboukari.studentcollab.helprequest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelpRequestConfigResource {

    @Value("${db.host}")
    private String dbHost;
    @Value("${db.port}")
    private String dbPort;
    @Value("${db.name}")
    private String dbName;
    @Value("${db.login}")
    private String dbLogin;
    @Value("${db.password}")
    private String dbPassword;


    public String getDbName() {
        return dbName;
    }

    public String getDbHost() {
        return dbHost;
    }

    public String getDbPort() {
        return dbPort;
    }

    public String getDbLogin() {
        return dbLogin;
    }

    public String getDbPassword() {
        return dbPassword;
    }
}
