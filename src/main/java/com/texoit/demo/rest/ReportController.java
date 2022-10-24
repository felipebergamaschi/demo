package com.texoit.demo.rest;

import com.texoit.demo.domain.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;

  @GetMapping("/report")
  public ResponseEntity<?> report() {
    final var data = reportService.getData();

    return ResponseEntity.ok(data);
  }

}
