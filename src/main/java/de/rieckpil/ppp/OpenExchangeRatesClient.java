package de.rieckpil.ppp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

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
      NasdaqResponse pppInformation) {
    return this.webClient
        .get()
        .uri(
            "/api/latest.json?app_id={appId}",
            this.applicationProperties.getOpenExchangeRatesApiId())
        .retrieve()
        .bodyToMono(ExchangeRatesResponse.class)
        .map(this::mapRates)
        .map(exchangeRates -> mapExchangeRatesToPpp(exchangeRates, pppInformation));
  }

  public PurchasePowerParityResponsePayload mapExchangeRatesToPpp(
      Map<String, Double> exchangeRates, NasdaqResponse pppInformation) {

    String countryCodeIsoAlpha2 = pppInformation.countryMetaResponse().countryCodeIsoAlpha2();
    String countryCodeIsoAlpha3 = pppInformation.countryMetaResponse().countryCodeIsoAlpha3();

    String currencyCode = pppInformation.countryMetaResponse().currencyCode();
    String currencySymbol = pppInformation.countryMetaResponse().currencySymbol();
    String currencyName = pppInformation.countryMetaResponse().currencyName();

    Double exchangeRate = exchangeRates.getOrDefault(currencyCode, 1.0);
    Map<String, Object> currenciesCountry =
        Map.of(currencyCode, Map.of("name", currencyName, "symbol", currencySymbol));
    Double pppRaw = (Double) pppInformation.datatable().data().get(0).get(2);
    Double pppPercentage =
        BigDecimal.valueOf(pppRaw * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
    Double pppConversionFactor = pppRaw / exchangeRate;

    return new PurchasePowerParityResponsePayload(
        countryCodeIsoAlpha2,
        countryCodeIsoAlpha3,
        currenciesCountry,
        new CurrencyMain(exchangeRate, "USD", "$"),
        pppPercentage,
        pppConversionFactor);
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
