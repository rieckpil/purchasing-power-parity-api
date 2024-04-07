package de.rieckpil.ppp;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {
  @NotEmpty private final String quandlApiKey;
  @NotEmpty private final String openExchangeRatesApiKey;
  @NotEmpty private final String openExchangeRatesApiId;

  public ApplicationProperties(
      String quandlApiKey, String openExchangeRatesApiKey, String openExchangeRatesApiId) {
    this.quandlApiKey = quandlApiKey;
    this.openExchangeRatesApiKey = openExchangeRatesApiKey;
    this.openExchangeRatesApiId = openExchangeRatesApiId;
  }

  public String getOpenExchangeRatesApiId() {
    return openExchangeRatesApiId;
  }

  public String getQuandlApiKey() {
    return quandlApiKey;
  }

  public String getOpenExchangeRatesApiKey() {
    return openExchangeRatesApiKey;
  }
}
