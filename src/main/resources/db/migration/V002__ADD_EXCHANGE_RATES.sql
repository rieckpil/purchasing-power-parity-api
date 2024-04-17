CREATE TABLE exchange_rates
(
  id                      SERIAL PRIMARY KEY,                               -- auto-incrementing primary key
  country_code_iso_alpha2 CHAR(2)        NOT NULL,                          -- ISO 3166-1 alpha-2
  country_code_iso_alpha3 CHAR(3)        NOT NULL,                          -- ISO 3166-1 alpha-3
  currency_code           CHAR(3)        NOT NULL,                          -- ISO 4217 currency codes
  currency_symbol         VARCHAR(10),                                      -- Currency symbol, length varies
  currency_name           VARCHAR(50),                                      -- Currency name  year
  exchange_rate           DECIMAL(10, 6) NOT NULL,                          -- Exchange rate to USD
  created_at              TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP -- UNIX timestamp when the record was added
);

ALTER TABLE exchange_rates
  ADD UNIQUE (country_code_iso_alpha2);
ALTER TABLE exchange_rates
  ADD UNIQUE (country_code_iso_alpha3);
ALTER TABLE exchange_rates
  ADD UNIQUE (currency_code);
