package com.example.be.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public abstract class IntegrationTestSupport {
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }

}
