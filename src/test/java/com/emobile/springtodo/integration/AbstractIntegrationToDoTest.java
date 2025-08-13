package com.emobile.springtodo.integration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class AbstractIntegrationToDoTest {

  static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:13.4")
      .withUsername("postgres")
      .withPassword("postgres")
      .withDatabaseName("postgresTest")
      .withExposedPorts(5432);

  static {
    container.start();
  }

  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
    dynamicPropertyRegistry.add("spring.datasource.url",container::getJdbcUrl);
    dynamicPropertyRegistry.add("spring.datasource.username",container::getUsername);
    dynamicPropertyRegistry.add("spring.datasource.password",container::getPassword);
  }
}
