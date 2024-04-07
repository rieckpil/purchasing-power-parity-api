package de.rieckpil.ppp;

import java.util.Optional;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class PurchasePowerParityController {
  private final PurchasePowerParityService purchasePowerParityService;

  public PurchasePowerParityController(PurchasePowerParityService purchasePowerParityService) {
    this.purchasePowerParityService = purchasePowerParityService;
  }

  @GetMapping
  public ResponseEntity<PurchasePowerParityResponsePayload> getPpp(
      @RequestParam(value = "target", required = false) @NotEmpty String countryCode) {
    return purchasePowerParityService
        .getByCountryCode(countryCode)
        .map(ResponseEntity::ok)
        .or(() -> Optional.of(ResponseEntity.notFound().build()))
        .get();
  }
}
