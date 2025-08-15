package com.emobile.springtodo.config;

import com.emobile.springtodo.integration.AbstractIntegrationToDoTest;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class HikariCPTest extends AbstractIntegrationToDoTest {
  @Autowired
  private DataSource dataSource;

  @Test
  void hikariDataSource_isConfigured() throws Exception {
    assertNotNull(dataSource, "DataSource должен быть не null");
    assertTrue(dataSource instanceof HikariDataSource, "DataSource должен быть HikariDataSource");

    HikariDataSource hikari = (HikariDataSource) dataSource;

    assertNotNull(hikari.getHikariPoolMXBean(), "Пул должен быть инициализирован");
    assertTrue(hikari.getPoolName().contains("Hikari"), "Имя пула должно содержать Hikari");
    assertTrue(hikari.getMaximumPoolSize() > 0, "Максимальный размер пула должен быть > 0");

    try (var connection = hikari.getConnection()) {
      assertNotNull(connection, "Должно быть возвращено соединение");
      assertFalse(connection.isClosed(), "Соединение должно быть открыто");
    }
  }
}

