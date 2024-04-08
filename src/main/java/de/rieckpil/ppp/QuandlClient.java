package de.rieckpil.ppp;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class QuandlClient {

  private final WebClient quandlWebClient;
  private final ApplicationProperties applicationProperties;

  public QuandlClient(WebClient quandlWebClient, ApplicationProperties applicationProperties) {
    this.quandlWebClient = quandlWebClient;
    this.applicationProperties = applicationProperties;
  }

  public Mono<JsonNode> fetchPurchasePowerParity(String countryCodeIsoAlpha3) {
    return this.quandlWebClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/datasets/ODA/{countryCodeIsoAlpha3}_PPPEX.json")
                    .queryParam("start_date", "2023-01-01")
                    .queryParam("end_date", "2023-12-31")
                    .queryParam("api_key", this.applicationProperties.getQuandlApiKey())
                    .build(countryCodeIsoAlpha3))
        .retrieve()
        .bodyToMono(JsonNode.class)
        .retry(3);
  }
}
