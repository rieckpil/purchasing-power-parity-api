package de.rieckpil.ppp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PurchasePowerParityService {

  private static final Logger LOG = LoggerFactory.getLogger(PurchasePowerParityService.class);

  private final RestCountriesClient restCountriesClient;
  private final NasdaqClient nasdaqClient;
  private final OpenExchangeRatesClient openExchangeRatesClient;

  public PurchasePowerParityService(
      RestCountriesClient restCountriesClient,
      NasdaqClient nasdaqClient,
      OpenExchangeRatesClient openExchangeRatesClient) {
    this.restCountriesClient = restCountriesClient;
    this.nasdaqClient = nasdaqClient;
    this.openExchangeRatesClient = openExchangeRatesClient;
  }

  public Mono<PurchasePowerParityResponsePayload> getByCountryCodeIsoAlpha2(
      String countryCodeIsoAlpha2) {

    return restCountriesClient
        .fetchCountryMeta(countryCodeIsoAlpha2)
        .map(nasdaqClient::fetchPurchasePowerParity)
        .flatMap(ppp -> ppp)
        .flatMap(
            ppp -> openExchangeRatesClient.fetchExchangeRates(countryCodeIsoAlpha2, "DEU", ppp))
        .switchIfEmpty(Mono.empty())
        .onErrorResume(
            e -> {
              LOG.error(String.format("Error fetching PPP information: %s", e.getMessage()), e);
              return Mono.empty();
            });
  }
}
