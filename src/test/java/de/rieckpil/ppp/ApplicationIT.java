package de.rieckpil.ppp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
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
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:15:1"));

  @Test
  void hitHealth() {
    webTestClient.get().uri("/actuator/health").exchange().expectStatus().isOk();
  }

  @Test
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
