package de.rieckpil.ppp;

import java.math.BigDecimal;
import java.util.Map;

import de.rieckpil.ppp.db.postgresql.tables.records.PppRecord;
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

  public Mono<ExchangeRateResult> fetchExchangeRates(PppRecord pppRecord) {
    return this.webClient
        .get()
        .uri(
            "/api/latest.json?app_id={appId}",
            this.applicationProperties.getOpenExchangeRatesApiId())
        .retrieve()
        .bodyToMono(ExchangeRatesResponse.class)
        .map(
            exchangeRates ->
                new ExchangeRateResult(
                    pppRecord,
                    BigDecimal.valueOf(
                        exchangeRates.rates().getOrDefault(pppRecord.getCurrencyCode(), 1.0))));
  }

  public record ExchangeRatesResponse(Map<String, Double> rates) {}

  public record ExchangeRateResult(PppRecord pppRecord, BigDecimal exchangeRate) {}
}
