package de.rieckpil.ppp;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class RestCountriesClient {

  private final WebClient restCountriesWebClient;
  private final CountryMapper countryMapper;

  public RestCountriesClient(WebClient restCountriesWebClient, CountryMapper countryMapper) {
    this.restCountriesWebClient = restCountriesWebClient;
    this.countryMapper = countryMapper;
  }

  public Mono<String> fetchCountryMeta(String countryCodeIsoAlpha2) {
    return countryMapper
        .getIsoCodeIsoAlpha3(countryCodeIsoAlpha2)
        .orElse(
            this.restCountriesWebClient
                .get()
                .uri("/alpha/{countryCode}", countryCodeIsoAlpha2)
                .retrieve()
                .onStatus(
                    status -> status.is4xxClientError() || status.is5xxServerError(),
                    clientResponse ->
                        Mono.error(new RuntimeException("Error fetching country meta - not found")))
                .bodyToMono(String.class)
                .retry(3)
                .onErrorResume(e -> Mono.empty()));
  }
}
