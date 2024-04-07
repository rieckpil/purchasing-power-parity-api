package de.rieckpil.ppp;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PurchasePowerParityService {

  private static final Logger LOG = LoggerFactory.getLogger(PurchasePowerParityService.class);

  private final RestCountriesClient restCountriesClient;
  private final QuandlClient quandlClient;
  private final OpenExchangeRatesClient openExchangeRatesClient;

  public PurchasePowerParityService(
      RestCountriesClient restCountriesClient,
      QuandlClient quandlClient,
      OpenExchangeRatesClient openExchangeRatesClient) {
    this.restCountriesClient = restCountriesClient;
    this.quandlClient = quandlClient;
    this.openExchangeRatesClient = openExchangeRatesClient;
  }

  public Mono<PurchasePowerParityResponsePayload> getByCountryCodeIsoAlpha2(
      String countryCodeIsoAlpha2) {
    PurchasePowerParityResponsePayload pppInformation = new PurchasePowerParityResponsePayload();

    if (countryCodeIsoAlpha2.equals("DE")) {
      return Mono.justOrEmpty(
          new PurchasePowerParityResponsePayload("DE", "DEU", Map.of("EUR", "Germany"), 1.0, 1.0));
    }

    return restCountriesClient
        .fetchCountryMeta(countryCodeIsoAlpha2)
        .flatMap(quandlClient::fetchPurchasePowerParity)
        .flatMap(openExchangeRatesClient::fetchExchangeRates)
        .onErrorResume(
            e -> {
              LOG.error(String.format("Error fetching PPP information: %s", e.getMessage()), e);
              return Mono.empty();
            });
  }
}
