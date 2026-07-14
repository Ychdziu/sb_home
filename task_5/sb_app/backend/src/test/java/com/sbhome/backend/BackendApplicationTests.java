package com.sbhome.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ConnectionTest {
    @Autowired
    DataSource dataSource;

    @Test
    void canConnect() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            assertTrue(conn.isValid(2));
        }
    }
}