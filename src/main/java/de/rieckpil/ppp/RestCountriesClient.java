package de.rieckpil.ppp;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class RestCountriesClient {

  private final WebClient restCountriesWebClient;

  public RestCountriesClient(WebClient restCountriesWebClient) {
    this.restCountriesWebClient = restCountriesWebClient;
  }

  public Object fetchCountryMeta(String countryCodeIsoAlpha2) {
    return this.restCountriesWebClient
        .get()
        .uri("/alpha/{countryCode}", countryCodeIsoAlpha2)
        .retrieve()
        .bodyToMono(Object.class)
        .block();
  }
}
