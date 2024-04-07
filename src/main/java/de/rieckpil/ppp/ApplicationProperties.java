package de.rieckpil.ppp;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {
  @NotEmpty private final String quandlApiKey;

  @NotEmpty private final String openExchangeRatesApiKey;

  public ApplicationProperties(String quandlApiKey, String openExchangeRatesApiKey) {
    this.quandlApiKey = quandlApiKey;
    this.openExchangeRatesApiKey = openExchangeRatesApiKey;
  }

  public String getQuandlApiKey() {
    return quandlApiKey;
  }

  public String getOpenExchangeRatesApiKey() {
    return openExchangeRatesApiKey;
  }
}
