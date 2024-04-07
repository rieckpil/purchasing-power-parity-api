package de.rieckpil.ppp;

import java.util.Map;

public record PurchasePowerParityResponsePayload(
    String countryCodeIsoAlpha2,
    String countryCodeIsoAlpha3,
    Map<String, Object> currenciesCountry,
    Double ppp,
    Double pppConversionFactor) {}
