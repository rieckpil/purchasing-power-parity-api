package de.rieckpil.ppp;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationIT {

  @Autowired private WebTestClient webTestClient;

  @Container @ServiceConnection
  static PostgreSQLContainer<?> postgresContainer =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.3-alpine"));

  @DynamicPropertySource
  static void configure(DynamicPropertyRegistry dynamicPropertyRegistry) {
    dynamicPropertyRegistry.add(
        "spring.r2dbc.url", () -> "r2dbc:" + postgresContainer.getJdbcUrl());
    dynamicPropertyRegistry.add("spring.r2dbc.username", () -> postgresContainer.getUsername());
    dynamicPropertyRegistry.add("spring.r2dbc.password", () -> postgresContainer.getPassword());
    dynamicPropertyRegistry.add("spring.flyway.url", () -> postgresContainer.getJdbcUrl());
    dynamicPropertyRegistry.add("spring.flyway.enabled", () -> false);
  }

  @Test
  void hitHealth() {
    webTestClient.get().uri("/actuator/health").exchange().expectStatus().isOk();
  }

  @Test
  @Disabled
  void hitPurchasePowerParityApi() {
    webTestClient.get().uri("/?target=DE").exchange().expectStatus().isOk();
  }

  @Test
  void rejectPurchasePowerParityApi() {
    webTestClient.get().uri("/").exchange().expectStatus().isBadRequest();
  }

  @Test
  void notFoundPurchasePowerParityApi() {
    webTestClient.get().uri("/?target=NOT_FOUND").exchange().expectStatus().isNotFound();
  }
}
