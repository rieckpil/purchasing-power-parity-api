# Purchasing Power Parity API

Successor backend for the [Purchasing Power Parity API](https://github.com/rwieruch/purchasing-power-parity) from [Robin Wieruch](https://github.com/rwieruch).

## Access the API

- `https://ppp-api.fly.dev/?target=<COUNTRY_CODE_ISO_3166_ALPHA_2>`, e.g. `https://ppp-api.fly.dev/?target=DE`

Response:

```json
{
  "countryCodeIsoAlpha2": "DE",
  "countryCodeIsoAlpha3": "DEU",
  "currenciesCountry": {
    "EUR": {
      "symbol": "€",
      "name": "Euro"
    }
  },
  "currencyMain": {
    "exchangeRate": 0.940223,
    "name": "USD",
    "symbol": "$"
  },
  "ppp": 74.0,
  "pppConversionFactor": 0.79
}
```
