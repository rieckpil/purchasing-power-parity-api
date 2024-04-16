package de.rieckpil.ppp;

public record CountryMeta(
    String countryCodeIsoAlpha2,
    String countryCodeIsoAlpha3,
    String currencyCode,
    String currencySymbol,
    String currencyName) {}
