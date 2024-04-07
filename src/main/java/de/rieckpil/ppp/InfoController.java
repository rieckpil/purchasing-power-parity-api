package de.rieckpil.ppp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/info")
public class InfoController {

  @GetMapping
  public String getInfo() {
    return "info";
  }
}
