package com.texoit.demo;


import com.texoit.demo.domain.ReportService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IntegrationWithCsvTests {

  @Autowired
  private ReportService reportService;

  @Test
  public void doTest() {
    final var data = reportService.getData();
    Assertions.assertThat(data)
        .isNotNull();
    final var min = data.getMin();
    Assertions.assertThat(min)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
    final var minItem1 = min.get(0);
    Assertions.assertThat(minItem1)
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasFieldOrPropertyWithValue("producer", "Joel Silver")
        .hasFieldOrPropertyWithValue("interval", 1)
        .hasFieldOrPropertyWithValue("previousWin", 1990)
        .hasFieldOrPropertyWithValue("followingWin", 1991);
    final var max = data.getMax();
    Assertions.assertThat(max)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
    final var maxItem1 = max.get(0);
    Assertions.assertThat(maxItem1)
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasFieldOrPropertyWithValue("producer", "Matthew Vaughn")
        .hasFieldOrPropertyWithValue("interval", 13)
        .hasFieldOrPropertyWithValue("previousWin", 2002)
        .hasFieldOrPropertyWithValue("followingWin", 2015);
  }

}
