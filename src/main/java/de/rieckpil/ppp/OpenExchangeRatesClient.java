package de.rieckpil.ppp;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class OpenExchangeRatesClient {

  private final WebClient webClient;
  private final ApplicationProperties applicationProperties;

  public OpenExchangeRatesClient(
      WebClient openExchangeRatesWebClient, ApplicationProperties applicationProperties) {
    this.webClient = openExchangeRatesWebClient;
    this.applicationProperties = applicationProperties;
  }

  public Mono<PurchasePowerParityResponsePayload> fetchExchangeRates(
      String countryCodeIsoAlpha2, String countryCodeIsoAlpha3, JsonNode pppInformation) {
    return this.webClient
        .get()
        .uri(
            "/api/latest.json?app_id={appId}",
            this.applicationProperties.getOpenExchangeRatesApiId())
        .retrieve()
        .bodyToMono(ExchangeRatesResponse.class)
        .map(this::mapRates)
        .map(
            exchangeRates ->
                mapExchangeRatesToPpp(
                    exchangeRates, pppInformation, countryCodeIsoAlpha2, countryCodeIsoAlpha3));
  }

  public PurchasePowerParityResponsePayload mapExchangeRatesToPpp(
      Map<String, Double> exchangeRates,
      JsonNode pppInformation,
      String countryCodeIsoAlpha2,
      String countryCodeIsoAlpha3) {
    Map<String, Object> currenciesCountry = Map.of("DUE", exchangeRates.getOrDefault("DEU", 1.0));
    Double ppp = 1.0;
    Double pppConversionFactor = 1.0;

    return new PurchasePowerParityResponsePayload(
        countryCodeIsoAlpha2, countryCodeIsoAlpha3, currenciesCountry, ppp, pppConversionFactor);
  }

  public Map<String, Double> mapRates(ExchangeRatesResponse response) {
    Map<String, Double> exchangeRates = new HashMap<>();
    for (String key : response.rates.keySet()) {
      exchangeRates.put(key, response.rates.get(key));
    }

    return exchangeRates;
  }

  public record ExchangeRatesResponse(Map<String, Double> rates) {}
}
