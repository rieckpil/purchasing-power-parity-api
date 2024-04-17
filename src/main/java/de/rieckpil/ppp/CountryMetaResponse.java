package de.rieckpil.ppp;

public record CountryMetaResponse(
    String countryCodeIsoAlpha2,
    String countryCodeIsoAlpha3,
    String currencyCode,
    String currencySymbol,
    String currencyName) {}
