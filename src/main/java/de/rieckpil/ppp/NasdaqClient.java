package de.rieckpil.ppp;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class NasdaqClient {

  private final WebClient webClient;
  private final ApplicationProperties applicationProperties;

  public NasdaqClient(WebClient nasdaqWebClient, ApplicationProperties applicationProperties) {
    this.webClient = nasdaqWebClient;
    this.applicationProperties = applicationProperties;
  }

  public Mono<JsonNode> fetchPurchasePowerParity(String countryCodeIsoAlpha3) {
    return this.webClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("api/v3/datatables/QDL/ODA.json")
                    .queryParam("date", String.format("%s-12-31", LocalDate.now().getYear()))
                    .queryParam("indicator", String.format("%s_PPPEX", countryCodeIsoAlpha3))
                    .queryParam("api_key", this.applicationProperties.getQuandlApiKey())
                    .build(countryCodeIsoAlpha3))
        .retrieve()
        .bodyToMono(JsonNode.class)
        .retry(3);
  }
}
