package de.rieckpil.ppp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class Application implements CommandLineRunner {

  private final Environment environment;

  public Application(Environment environment) {
    this.environment = environment;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    System.out.println(environment.getProperty("SUPABASE_PPP_API_PASSWORD"));
    System.out.println(environment.getProperty("OPEN_EXCHANGE_RATES_API_KEY"));
    System.out.println(environment.getProperty("QUANDL_API_KEY"));
  }
}
