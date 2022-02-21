package com.silenteight.aedemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class AeController {

  private final AeService aeService;

  AeController(AeService aeService) { this.aeService = aeService; }

  @GetMapping("alerts")
  String createAlert() {
    return aeService.createAlerts();
  }

  @GetMapping("matches")
  String createMatches() {
    return aeService.createMatches();
  }

  @GetMapping("dataset")
  String createDataset() {
    return aeService.createDataset();
  }

  @GetMapping("analysis")
  String createAnalysis() {
    return aeService.createAnalysis();
  }

  @GetMapping("addAnalysis")
  String addDataset() {return aeService.addDataSet();}

  @GetMapping("recommendation")
  String getRecommendation() {
    return aeService.getRecommendation();
  }
}
