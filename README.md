# Purchasing Power Parity API

Successor backend for the [Purchasing Power Parity API](https://github.com/rwieruch/purchasing-power-parity) from [Robin Wieruch](https://github.com/rwieruch).

Exchange rates and purchase power parity is fetched once per day for each country and then cached for the rest of the day.

## Access the API


- [Swagger UI](https://ppp-api.fly.dev/swagger-doc/swagger-ui.html)
- [API docs](https://ppp-api.fly.dev/swagger-doc/v3/api-docs)

Sample:

```shell
$ curl -v https://ppp-api.fly.dev/?target=DE

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
