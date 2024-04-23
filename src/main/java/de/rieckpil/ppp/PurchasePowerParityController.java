package de.rieckpil.ppp;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
public class PurchasePowerParityController {
  private final PurchasePowerParityService purchasePowerParityService;

  public PurchasePowerParityController(PurchasePowerParityService purchasePowerParityService) {
    this.purchasePowerParityService = purchasePowerParityService;
  }

  @GetMapping
  public Mono<ResponseEntity<PurchasePowerParityResponsePayload>> getPpp(
      @RequestParam(value = "target", required = false) @NotEmpty String countryCodeIsoAlpha2) {
    return purchasePowerParityService
        .getByCountryCodeIsoAlpha2(countryCodeIsoAlpha2)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }
}
