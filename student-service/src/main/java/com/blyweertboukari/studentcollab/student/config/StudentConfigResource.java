package com.blyweertboukari.studentcollab.student.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class StudentConfigResource {

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
    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/db-name")
    public String getDbName() {
        return dbName;
    }

    @GetMapping("/db-host")
    public String getDbHost() {
        return dbHost;
    }

    @GetMapping("/db-port")
    public String getDbPort() {
        return dbPort;
    }

    @GetMapping("/db-login")
    public String getDbLogin() {
        return dbLogin;
    }

    @GetMapping("/db-password")
    public String getDbPassword() {
        return dbPassword;
    }

    @GetMapping("/server-port")
    public String getServerPort() {
        return serverPort;
    }
}
