package de.rieckpil.ppp;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OpenExchangeRatesClient {

  private final WebClient webClient;
  private final ApplicationProperties applicationProperties;

  public OpenExchangeRatesClient(
      WebClient openExchangeRatesWebClient, ApplicationProperties applicationProperties) {
    this.webClient = openExchangeRatesWebClient;
    this.applicationProperties = applicationProperties;
  }

  public Object fetchExchangeRates(Object pppInformation) {
    return this.webClient
        .get()
        .uri(
            "/latest.json?app_id={apiKey}", this.applicationProperties.getOpenExchangeRatesApiKey())
        .retrieve()
        .bodyToMono(ExchangeRatesResponse.class)
        .map(response -> mapExchangeRatesToPppInformation(pppInformation, response));
  }

  private Object mapExchangeRatesToPppInformation(
      Object pppInformation, ExchangeRatesResponse response) {
    // Assuming that ExchangeRatesResponse contains a Map<String, Double> of currency codes to rates
    Map<String, Double> rates = response.getRates();
    Double exchangeRate =
        rates.getOrDefault(
            "USD", 1.0); // Assuming USD as default; adjust based on your requirements.

    // Here, you might want to adjust how you integrate the exchange rate into your PppInformation.
    // This is a placeholder implementation. You need to tailor it to your specific requirements.
    // pppInformation.setPppConversionFactor(exchangeRate); // Example usage

    return pppInformation;
  }

  public static class ExchangeRatesResponse {
    private Map<String, Double> rates;

    public Map<String, Double> getRates() {
      return rates;
    }

    public void setRates(Map<String, Double> rates) {
      this.rates = rates;
    }
  }
}
