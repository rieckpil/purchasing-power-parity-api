package de.rieckpil.ppp;

import java.util.HashMap;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CountryMapper {
  private static final HashMap<String, String> ISO_COUNTRY_CODES = new HashMap<>();
  private static final Logger LOG = LoggerFactory.getLogger(CountryMapper.class);

  static {
    ISO_COUNTRY_CODES.put("AF", "AFG");
    ISO_COUNTRY_CODES.put("AL", "ALB");
    ISO_COUNTRY_CODES.put("DZ", "DZ");
    ISO_COUNTRY_CODES.put("AO", "AGO");
    ISO_COUNTRY_CODES.put("AR", "ARG");
    ISO_COUNTRY_CODES.put("AU", "AUS");
    ISO_COUNTRY_CODES.put("AT", "AUT");
    ISO_COUNTRY_CODES.put("AZ", "AZE");
    ISO_COUNTRY_CODES.put("BS", "BHS");
    ISO_COUNTRY_CODES.put("BG", "BGR");
    ISO_COUNTRY_CODES.put("BN", "BN");
    ISO_COUNTRY_CODES.put("BO", "BOL");
    ISO_COUNTRY_CODES.put("BR", "BRA");
    ISO_COUNTRY_CODES.put("BW", "BWA");
    ISO_COUNTRY_CODES.put("BY", "BYR");
    ISO_COUNTRY_CODES.put("CA", "CAN");
    ISO_COUNTRY_CODES.put("CH", "CHE");
    ISO_COUNTRY_CODES.put("CL", "CHL");
    ISO_COUNTRY_CODES.put("CN", "CHN");
    ISO_COUNTRY_CODES.put("CO", "COL");
    ISO_COUNTRY_CODES.put("CR", "CRI");
    ISO_COUNTRY_CODES.put("CU", "CUB");
    ISO_COUNTRY_CODES.put("CY", "CYP");
    ISO_COUNTRY_CODES.put("CZ", "CZ");
    ISO_COUNTRY_CODES.put("DE", "DEU");
    ISO_COUNTRY_CODES.put("DK", "DNK");
    ISO_COUNTRY_CODES.put("DO", "DOM");
    ISO_COUNTRY_CODES.put("EC", "ECV");
    ISO_COUNTRY_CODES.put("EG", "EGY");
    ISO_COUNTRY_CODES.put("ER", "ERI");
    ISO_COUNTRY_CODES.put("ET", "ETH");
    ISO_COUNTRY_CODES.put("FI", "FIN");
    ISO_COUNTRY_CODES.put("FR", "FRA");
    ISO_COUNTRY_CODES.put("GA", "GHA");
    ISO_COUNTRY_CODES.put("GM", "GMB");
    ISO_COUNTRY_CODES.put("GR", "GRC");
    ISO_COUNTRY_CODES.put("GT", "GTM");
    ISO_COUNTRY_CODES.put("HK", "HKG");
    ISO_COUNTRY_CODES.put("HR", "HRV");
    ISO_COUNTRY_CODES.put("HU", "HUN");
    ISO_COUNTRY_CODES.put("ID", "IDN");
    ISO_COUNTRY_CODES.put("IE", "IRL");
    ISO_COUNTRY_CODES.put("IL", "ISR");
    ISO_COUNTRY_CODES.put("IT", "ITA");
    ISO_COUNTRY_CODES.put("JM", "JAM");
    ISO_COUNTRY_CODES.put("JO", "JOR");
    ISO_COUNTRY_CODES.put("JP", "JPN");
    ISO_COUNTRY_CODES.put("KE", "KEN");
    ISO_COUNTRY_CODES.put("KG", "KGZ");
    ISO_COUNTRY_CODES.put("KI", "KIR");
    ISO_COUNTRY_CODES.put("KW", "KWT");
    ISO_COUNTRY_CODES.put("KY", "KYC");
    ISO_COUNTRY_CODES.put("KZ", "KAZ");
    ISO_COUNTRY_CODES.put("LA", "LAO");
    ISO_COUNTRY_CODES.put("LV", "LVA");
    ISO_COUNTRY_CODES.put("LT", "LTU");
    ISO_COUNTRY_CODES.put("LU", "LUX");
    ISO_COUNTRY_CODES.put("LY", "LBY");
    ISO_COUNTRY_CODES.put("MA", "MAR");
    ISO_COUNTRY_CODES.put("MC", "MCO");
    ISO_COUNTRY_CODES.put("MD", "MDA");
    ISO_COUNTRY_CODES.put("ME", "MNE");
    ISO_COUNTRY_CODES.put("MG", "MAD");
    ISO_COUNTRY_CODES.put("MH", "MHL");
    ISO_COUNTRY_CODES.put("MK", "MKD");
    ISO_COUNTRY_CODES.put("ML", "MLT");
    ISO_COUNTRY_CODES.put("MN", "MNG");
    ISO_COUNTRY_CODES.put("MO", "MOZ");
    ISO_COUNTRY_CODES.put("MR", "MRU");
    ISO_COUNTRY_CODES.put("MT", "MTN");
    ISO_COUNTRY_CODES.put("MU", "MUS");
    ISO_COUNTRY_CODES.put("MV", "MWI");
    ISO_COUNTRY_CODES.put("MY", "MYA");
    ISO_COUNTRY_CODES.put("NA", "NAM");
    ISO_COUNTRY_CODES.put("NE", "NER");
    ISO_COUNTRY_CODES.put("NG", "NGA");
    ISO_COUNTRY_CODES.put("NI", "NIC");
    ISO_COUNTRY_CODES.put("NO", "NOR");
    ISO_COUNTRY_CODES.put("NP", "NPL");
    ISO_COUNTRY_CODES.put("NZ", "NZL");
    ISO_COUNTRY_CODES.put("OM", "OMA");
    ISO_COUNTRY_CODES.put("PA", "PAN");
    ISO_COUNTRY_CODES.put("PE", "PER");
    ISO_COUNTRY_CODES.put("PH", "PHL");
    ISO_COUNTRY_CODES.put("PL", "POL");
    ISO_COUNTRY_CODES.put("PT", "POR");
    ISO_COUNTRY_CODES.put("QA", "QAT");
    ISO_COUNTRY_CODES.put("RO", "ROU");
    ISO_COUNTRY_CODES.put("RS", "SRB");
    ISO_COUNTRY_CODES.put("RU", "RUS");
    ISO_COUNTRY_CODES.put("RW", "RWA");
    ISO_COUNTRY_CODES.put("SA", "SAU");
    ISO_COUNTRY_CODES.put("SD", "SDN");
    ISO_COUNTRY_CODES.put("SE", "SEN");
    ISO_COUNTRY_CODES.put("SG", "SGP");
    ISO_COUNTRY_CODES.put("SK", "SVK");
    ISO_COUNTRY_CODES.put("SL", "LKA");
    ISO_COUNTRY_CODES.put("SM", "SMR");
    ISO_COUNTRY_CODES.put("SN", "SWA");
    ISO_COUNTRY_CODES.put("SO", "SOM");
    ISO_COUNTRY_CODES.put("SR", "SRB");
    ISO_COUNTRY_CODES.put("ST", "STP");
    ISO_COUNTRY_CODES.put("SV", "SVK");
    ISO_COUNTRY_CODES.put("SY", "SYR");
    ISO_COUNTRY_CODES.put("TH", "THA");
    ISO_COUNTRY_CODES.put("TR", "TUR");
    ISO_COUNTRY_CODES.put("TZ", "TZA");
    ISO_COUNTRY_CODES.put("UA", "UKR");
    ISO_COUNTRY_CODES.put("UG", "UGA");
    ISO_COUNTRY_CODES.put("US", "USA");
    ISO_COUNTRY_CODES.put("UY", "URY");
    ISO_COUNTRY_CODES.put("UZ", "UZB");
    ISO_COUNTRY_CODES.put("VA", "VCT");
    ISO_COUNTRY_CODES.put("VI", "VIR");
    ISO_COUNTRY_CODES.put("VN", "VNM");
    ISO_COUNTRY_CODES.put("YE", "YEM");
    ISO_COUNTRY_CODES.put("ZA", "ZAF");
    ISO_COUNTRY_CODES.put("ZM", "ZMB");
    ISO_COUNTRY_CODES.put("ZW", "ZWE");
  }

  public Optional<Mono<String>> getIsoCodeIsoAlpha3(String isoCodeIsoAlpha2) {
    String ISO_ALPHA_3 = ISO_COUNTRY_CODES.get(isoCodeIsoAlpha2);

    if (ISO_ALPHA_3 == null || ISO_ALPHA_3.isBlank()) {
      LOG.warn("No ISO-Alpha-3 for ISO-Alpha-2 {}", isoCodeIsoAlpha2);
      return Optional.empty();
    }

    return Optional.of(Mono.justOrEmpty(ISO_ALPHA_3));
  }
}
