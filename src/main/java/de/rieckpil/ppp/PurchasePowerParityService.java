package de.rieckpil.ppp;

import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static de.rieckpil.ppp.db.postgresql.tables.CountryMeta.COUNTRY_META;

@Service
public class PurchasePowerParityService {

  private static final Logger LOG = LoggerFactory.getLogger(PurchasePowerParityService.class);

  private final RestCountriesClient restCountriesClient;
  private final NasdaqClient nasdaqClient;
  private final OpenExchangeRatesClient openExchangeRatesClient;
  private final DSLContext dslContext;

  public PurchasePowerParityService(
      RestCountriesClient restCountriesClient,
      NasdaqClient nasdaqClient,
      OpenExchangeRatesClient openExchangeRatesClient,
      DSLContext dslContext) {
    this.restCountriesClient = restCountriesClient;
    this.nasdaqClient = nasdaqClient;
    this.openExchangeRatesClient = openExchangeRatesClient;
    this.dslContext = dslContext;
  }

  public Mono<PurchasePowerParityResponsePayload> getByCountryCodeIsoAlpha2(
      String countryCodeIsoAlpha2) {

    Flux.from(
            dslContext
                .insertInto(COUNTRY_META)
                .columns(
                    COUNTRY_META.COUNTRY_CODE_ISO_ALPHA2,
                    COUNTRY_META.COUNTRY_CODE_ISO_ALPHA3,
                    COUNTRY_META.CURRENCY_CODE,
                    COUNTRY_META.CURRENCY_NAME,
                    COUNTRY_META.CURRENCY_SYMBOL)
                .values("DE", "DEU", "EUR", "Euro", "€")
                .returningResult(COUNTRY_META.ID))
        .subscribe();

    return restCountriesClient
        .fetchCountryMeta(countryCodeIsoAlpha2)
        .map(nasdaqClient::fetchPurchasePowerParity)
        .flatMap(ppp -> ppp)
        .flatMap(ppp -> openExchangeRatesClient.fetchExchangeRates(countryCodeIsoAlpha2, ppp))
        .switchIfEmpty(Mono.empty())
        .onErrorResume(
            e -> {
              LOG.error(String.format("Error fetching PPP information: %s", e.getMessage()), e);
              return Mono.empty();
            });
  }
}
