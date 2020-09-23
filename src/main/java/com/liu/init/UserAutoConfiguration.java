package com.liu.init;

import com.liu.init.db.UserDbInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class UserAutoConfiguration {

    @Autowired
    private UserDbInit userDbInit;

    @PostConstruct
    public void init() {
        userDbInit.checkOrCreateInitDB();
    }
}
