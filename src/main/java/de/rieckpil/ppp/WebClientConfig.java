package de.rieckpil.ppp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Bean
  public WebClient openExchangeRatesWebClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder.baseUrl("https://openexchangerates.org").build();
  }

  @Bean
  public WebClient nasdaqWebClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder.baseUrl("https://data.nasdaq.com").build();
  }

  @Bean
  public WebClient restCountriesWebClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder.baseUrl("https://restcountries.com").build();
  }
}
