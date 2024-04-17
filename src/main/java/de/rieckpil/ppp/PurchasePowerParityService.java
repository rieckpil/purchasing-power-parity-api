package de.rieckpil.ppp;

import de.rieckpil.ppp.db.postgresql.tables.records.CountryMetaRecord;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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

    return getCountryMeta(countryCodeIsoAlpha2)
        .map(nasdaqClient::fetchPurchasePowerParity)
        .flatMap(ppp -> ppp)
        .flatMap(openExchangeRatesClient::fetchExchangeRates)
        .switchIfEmpty(Mono.empty())
        .onErrorResume(
            e -> {
              LOG.error(String.format("Error fetching PPP information: %s", e.getMessage()), e);
              return Mono.empty();
            });
  }

  private Mono<CountryMetaRecord> getCountryMeta(String countryCodeIsoAlpha2) {
    return Mono.from(
            dslContext
                .selectFrom(COUNTRY_META)
                .where(COUNTRY_META.COUNTRY_CODE_ISO_ALPHA2.eq(countryCodeIsoAlpha2)))
        .switchIfEmpty(
            this.restCountriesClient
                .fetchCountryMeta(countryCodeIsoAlpha2)
                .map(
                    countryMetaResponse -> {
                      var record = dslContext.newRecord(COUNTRY_META);
                      record.setCountryCodeIsoAlpha2(countryMetaResponse.countryCodeIsoAlpha2());
                      record.setCountryCodeIsoAlpha3(countryMetaResponse.countryCodeIsoAlpha3());
                      record.setCurrencyCode(countryMetaResponse.currencyCode());
                      record.setCurrencyName(countryMetaResponse.currencyName());
                      record.setCurrencySymbol(countryMetaResponse.currencySymbol());
                      return record;
                    })
                .map(
                    countryMetaRecord ->
                        Mono.from(
                            dslContext.insertInto(COUNTRY_META).set(countryMetaRecord).returning()))
                .flatMap(record -> record));
  }
}
