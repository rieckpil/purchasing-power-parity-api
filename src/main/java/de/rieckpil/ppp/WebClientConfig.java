package de.rieckpil.ppp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Bean
  public WebClient openExchangeRatesWebClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder.baseUrl("https://openexchangerates.org/api").build();
  }

  @Bean
  public WebClient quandlWebClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder.baseUrl("https://www.quandl.com/api").build();
  }

  @Bean
  public WebClient restCountriesWebClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder.baseUrl("https://restcountries.com/api").build();
  }
}
