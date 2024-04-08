package de.rieckpil.ppp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class Application implements CommandLineRunner {

  private static final Logger LOG = LoggerFactory.getLogger(Application.class);

  private final Environment environment;

  public Application(Environment environment) {
    this.environment = environment;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    LOG.info("Up- and running with profile(s): {}", String.join(",", environment.getActiveProfiles()));
  }
}
