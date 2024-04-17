package de.rieckpil.ppp;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.rieckpil.ppp.db.postgresql.tables.records.CountryMetaRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class NasdaqClient {

  private final WebClient webClient;
  private final ObjectMapper objectMapper;
  private final ApplicationProperties applicationProperties;

  public NasdaqClient(
      WebClient nasdaqWebClient,
      ObjectMapper objectMapper,
      ApplicationProperties applicationProperties) {
    this.webClient = nasdaqWebClient;
    this.objectMapper = objectMapper;
    this.applicationProperties = applicationProperties;
  }

  public Mono<NasdaqResponse> fetchPurchasePowerParity(CountryMetaRecord countryMetaRecord) {
    var countryCodeIsoAlpha3 = countryMetaRecord.getCountryCodeIsoAlpha3();

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
        .map(jsonNode -> this.objectMapper.convertValue(jsonNode, NasdaqResponse.class))
        .map(
            nasdaqResponse ->
                new NasdaqResponse(
                    new CountryMetaResponse(
                        countryMetaRecord.getCountryCodeIsoAlpha2(),
                        countryMetaRecord.getCountryCodeIsoAlpha3(),
                        countryMetaRecord.getCurrencyCode(),
                        countryMetaRecord.getCurrencySymbol(),
                        countryMetaRecord.getCurrencyName()),
                    nasdaqResponse.meta(),
                    nasdaqResponse.datatable()))
        .retry(3);
  }
}
