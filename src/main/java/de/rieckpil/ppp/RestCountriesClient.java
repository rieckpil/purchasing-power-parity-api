package de.rieckpil.ppp;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class RestCountriesClient {

  private final WebClient restCountriesWebClient;
  private final ObjectMapper objectMapper;

  public RestCountriesClient(
      WebClient restCountriesWebClient, CountryMapper countryMapper, ObjectMapper objectMapper) {
    this.restCountriesWebClient = restCountriesWebClient;
    this.objectMapper = objectMapper;
  }

  public Mono<CountryMeta> fetchCountryMeta(String countryCodeIsoAlpha2) {
    return this.restCountriesWebClient
        .get()
        .uri("/v3.1/alpha/{countryCode}", countryCodeIsoAlpha2)
        .retrieve()
        .onStatus(
            status -> status.is4xxClientError() || status.is5xxServerError(),
            clientResponse ->
                Mono.error(new RuntimeException("Error fetching country meta - not found")))
        .bodyToMono(JsonNode.class)
        .map(
            jsonNode -> {
              Map<String, Currency> currencies =
                  objectMapper.convertValue(
                      jsonNode.get(0).get("currencies"),
                      new TypeReference<Map<String, Currency>>() {});
              return new CountryMeta(
                  countryCodeIsoAlpha2,
                  jsonNode.get(0).get("cca3").asText(),
                  currencies.keySet().stream().findFirst().get(),
                  currencies.values().stream().findFirst().get().symbol(),
                  currencies.values().stream().findFirst().get().name());
            })
        .retry(3)
        .onErrorResume(e -> Mono.empty());
  }

  record Currency(String name, String symbol) {}
}
