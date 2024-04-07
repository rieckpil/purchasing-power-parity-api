package de.rieckpil.ppp;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class PurchasePowerParityService {
  public Optional<PurchasePowerParityResponsePayload> getByCountryCode(String countryCode) {

    if (countryCode.equals("DE")) {
      return Optional.of(
          new PurchasePowerParityResponsePayload("DE", "DEU", Map.of("EUR", "Germany"), 1.0, 1.0));
    }
    return Optional.empty();
  }
}
