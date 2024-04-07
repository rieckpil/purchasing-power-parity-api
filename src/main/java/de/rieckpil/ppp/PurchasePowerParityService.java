package de.rieckpil.ppp;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class PurchasePowerParityService {
  public Optional<PurchasePowerParityResponsePayload> getByCountryCode(String countryCode) {
    return Optional.empty();
  }
}
