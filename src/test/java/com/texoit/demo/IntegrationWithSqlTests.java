package com.texoit.demo;


import static org.assertj.core.api.Assertions.assertThat;

import com.texoit.demo.domain.Award;
import com.texoit.demo.domain.AwardReportDTO;
import com.texoit.demo.domain.AwardRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class IntegrationWithSqlTests {

  @Autowired
  private AwardRepository awardRepository;
  @LocalServerPort
  private int port;
  private final TestRestTemplate testRestTemplate = new TestRestTemplate();

  @Test
  public void doTest() {

    awardRepository.saveAllAndFlush(List.of(
        Award.builder().year(1999).title("title 1").producers("producer 1").studios("studio 1")
            .winner(Boolean.FALSE).build(),
        Award.builder().year(1999).title("title 2").producers("producer 2").studios("studio 2")
            .winner(Boolean.FALSE).build(),
        Award.builder().year(1999).title("title 3").producers("producer 3").studios("studio 3")
            .winner(Boolean.TRUE).build(),
        Award.builder().year(2000).title("title 1").producers("producer 1").studios("studio 1")
            .winner(Boolean.TRUE).build(),
        Award.builder().year(2000).title("title 2").producers("producer 2").studios("studio 2")
            .winner(Boolean.FALSE).build(),
        Award.builder().year(2000).title("title 3").producers("producer 3").studios("studio 3")
            .winner(Boolean.FALSE).build(),
        Award.builder().year(2001).title("title 1").producers("producer 1").studios("studio 1")
            .winner(Boolean.TRUE).build(),
        Award.builder().year(2001).title("title 2").producers("producer 2").studios("studio 2")
            .winner(Boolean.FALSE).build(),
        Award.builder().year(2001).title("title 3").producers("producer 3").studios("studio 3")
            .winner(Boolean.FALSE).build(),
        Award.builder().year(2002).title("title 1").producers("producer 1").studios("studio 1")
            .winner(Boolean.FALSE).build(),
        Award.builder().year(2002).title("title 2").producers("producer 2").studios("studio 2")
            .winner(Boolean.TRUE).build(),
        Award.builder().year(2002).title("title 3").producers("producer 3").studios("studio 3")
            .winner(Boolean.FALSE).build(),
        Award.builder().year(2003).title("title 1").producers("producer 1").studios("studio 1")
            .winner(Boolean.FALSE).build(),
        Award.builder().year(2003).title("title 2").producers("producer 2").studios("studio 2")
            .winner(Boolean.TRUE).build(),
        Award.builder().year(2003).title("title 3").producers("producer 3").studios("studio 3")
            .winner(Boolean.FALSE).build(),
        Award.builder().year(2004).title("title 1").producers("producer 1").studios("studio 1")
            .winner(Boolean.FALSE).build(),
        Award.builder().year(2004).title("title 2").producers("producer 2").studios("studio 2")
            .winner(Boolean.FALSE).build(),
        Award.builder().year(2004).title("title 3").producers("producer 3").studios("studio 3")
            .winner(Boolean.TRUE).build()
    ));

    final var responseEntity = testRestTemplate
        .getForEntity("http://localhost:" + port + "/report", AwardReportDTO.class);

    assertThat(responseEntity).isNotNull();
    assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);

    final var data = responseEntity.getBody();

    assertThat(data)
        .isNotNull();
    final var min = data.getMin();
    assertThat(min)
        .isNotNull()
        .isNotEmpty()
        .hasSize(2);
    final var minItem1 = min.get(0);
    assertThat(minItem1)
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasFieldOrPropertyWithValue("producer", "producer 1")
        .hasFieldOrPropertyWithValue("interval", 1)
        .hasFieldOrPropertyWithValue("previousWin", 2000)
        .hasFieldOrPropertyWithValue("followingWin", 2001);
    final var minItem2 = min.get(1);
    assertThat(minItem2)
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasFieldOrPropertyWithValue("producer", "producer 2")
        .hasFieldOrPropertyWithValue("interval", 1)
        .hasFieldOrPropertyWithValue("previousWin", 2002)
        .hasFieldOrPropertyWithValue("followingWin", 2003);
    final var max = data.getMax();
    assertThat(max)
        .isNotNull()
        .isNotEmpty()
        .hasSize(1);
    final var maxItem1 = max.get(0);
    assertThat(maxItem1)
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasFieldOrPropertyWithValue("producer", "producer 3")
        .hasFieldOrPropertyWithValue("interval", 5)
        .hasFieldOrPropertyWithValue("previousWin", 1999)
        .hasFieldOrPropertyWithValue("followingWin", 2004);
  }

}
