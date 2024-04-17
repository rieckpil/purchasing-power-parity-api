package de.rieckpil.ppp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import de.rieckpil.ppp.db.postgresql.tables.records.CountryMetaRecord;
import de.rieckpil.ppp.db.postgresql.tables.records.PppRecord;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static de.rieckpil.ppp.db.postgresql.Tables.EXCHANGE_RATES;
import static de.rieckpil.ppp.db.postgresql.tables.CountryMeta.COUNTRY_META;
import static de.rieckpil.ppp.db.postgresql.tables.Ppp.PPP;

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

    return maybeFetchCountryMeta(countryCodeIsoAlpha2)
        .map(this::maybeFetchPurchasePowerParity)
        .flatMap(mono -> mono)
        .flatMap(this::maybeFetchExchangeRates)
        .map(
            exchangeRate ->
                new PurchasePowerParityResponsePayload(
                    exchangeRate.pppRecord().getCountryCodeIsoAlpha2(),
                    exchangeRate.pppRecord().getCountryCodeIsoAlpha3(),
                    Map.of(
                        exchangeRate.pppRecord().getCurrencyCode(),
                        Map.of(
                            "name",
                            exchangeRate.pppRecord().getCurrencyName(),
                            "symbol",
                            exchangeRate.pppRecord().getCurrencySymbol())),
                    new CurrencyMain(exchangeRate.exchangeRate().doubleValue(), "USD", "$"),
                    exchangeRate
                        .pppRecord()
                        .getPpp()
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue(),
                    exchangeRate
                        .pppRecord()
                        .getPpp()
                        .divide(exchangeRate.exchangeRate(), 2, RoundingMode.HALF_UP)
                        .doubleValue()))
        .switchIfEmpty(Mono.empty())
        .onErrorResume(
            e -> {
              LOG.error(String.format("Error fetching PPP information: %s", e.getMessage()), e);
              return Mono.empty();
            });
  }

  private Mono<OpenExchangeRatesClient.ExchangeRateResult> maybeFetchExchangeRates(
      PppRecord pppRecord) {
    return Mono.from(
            dslContext
                .selectFrom(EXCHANGE_RATES)
                .where(
                    EXCHANGE_RATES.COUNTRY_CODE_ISO_ALPHA3.eq(pppRecord.getCountryCodeIsoAlpha3())))
        .map(
            record ->
                new OpenExchangeRatesClient.ExchangeRateResult(pppRecord, record.getExchangeRate()))
        .switchIfEmpty(
            openExchangeRatesClient
                .fetchExchangeRates(pppRecord)
                .flatMap(
                    exchangeRateResult -> {
                      var record = dslContext.newRecord(EXCHANGE_RATES);
                      record.setExchangeRate(exchangeRateResult.exchangeRate());
                      record.setCurrencyName(pppRecord.getCurrencyName());
                      record.setCurrencySymbol(pppRecord.getCurrencySymbol());
                      record.setCurrencyCode(pppRecord.getCurrencyCode());
                      record.setCountryCodeIsoAlpha2(pppRecord.getCountryCodeIsoAlpha2());
                      record.setCountryCodeIsoAlpha3(pppRecord.getCountryCodeIsoAlpha3());
                      return Mono.from(
                          dslContext.insertInto(EXCHANGE_RATES).set(record).returning());
                    })
                .map(
                    record ->
                        new OpenExchangeRatesClient.ExchangeRateResult(
                            pppRecord, record.getExchangeRate())));
  }

  private Mono<PppRecord> maybeFetchPurchasePowerParity(CountryMetaRecord countryMetaRecord) {
    return Mono.from(
            dslContext
                .selectFrom(PPP)
                .where(PPP.COUNTRY_CODE_ISO_ALPHA3.eq(countryMetaRecord.getCountryCodeIsoAlpha3())))
        .switchIfEmpty(
            this.nasdaqClient
                .fetchPurchasePowerParity(countryMetaRecord)
                .map(
                    nasdaqResponse -> {
                      var record = dslContext.newRecord(PPP);
                      record.setCountryCodeIsoAlpha2(
                          nasdaqResponse.countryMetaResponse().countryCodeIsoAlpha2());
                      record.setCountryCodeIsoAlpha3(
                          nasdaqResponse.countryMetaResponse().countryCodeIsoAlpha3());
                      record.setCurrencyCode(nasdaqResponse.countryMetaResponse().currencyCode());
                      record.setCurrencySymbol(
                          nasdaqResponse.countryMetaResponse().currencySymbol());
                      record.setCurrencyName(nasdaqResponse.countryMetaResponse().currencyName());
                      record.setPpp(
                          BigDecimal.valueOf(
                              (double) nasdaqResponse.datatable().data().get(0).get(2)));
                      return record;
                    })
                .map(pppRecord -> Mono.from(dslContext.insertInto(PPP).set(pppRecord).returning()))
                .flatMap(record -> record));
  }

  private Mono<CountryMetaRecord> maybeFetchCountryMeta(String countryCodeIsoAlpha2) {
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
