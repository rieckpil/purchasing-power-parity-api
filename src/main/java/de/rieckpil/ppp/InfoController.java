package de.rieckpil.ppp;

import io.micrometer.core.annotation.Counted;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/info")
public class InfoController {

  @GetMapping
  @Counted(value = "info.count", description = "How often the info endpoint was called")
  public String getInfo() {
    return "info";
  }
}
