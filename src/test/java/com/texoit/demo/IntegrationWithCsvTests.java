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
        .hasSize(2);
    final var minItem1 = min.get(0);
    Assertions.assertThat(minItem1)
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasFieldOrPropertyWithValue("producer", "Allan Carr")
        .hasFieldOrPropertyWithValue("interval", 1)
        .hasFieldOrPropertyWithValue("previousWin", 1982)
        .hasFieldOrPropertyWithValue("followingWin", 1983);
    final var minItem2 = min.get(1);
    Assertions.assertThat(minItem2)
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasFieldOrPropertyWithValue("producer", "Todd Garner, Jack Giarraputo and Adam Sandler")
        .hasFieldOrPropertyWithValue("interval", 1)
        .hasFieldOrPropertyWithValue("previousWin", 2011)
        .hasFieldOrPropertyWithValue("followingWin", 2012);
    final var max = data.getMax();
    Assertions.assertThat(max)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
    final var maxItem1 = max.get(0);
    Assertions.assertThat(maxItem1)
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasFieldOrPropertyWithValue("producer", "Bill Cosby")
        .hasFieldOrPropertyWithValue("interval", 9)
        .hasFieldOrPropertyWithValue("previousWin", 1995)
        .hasFieldOrPropertyWithValue("followingWin", 2004);
  }

}
