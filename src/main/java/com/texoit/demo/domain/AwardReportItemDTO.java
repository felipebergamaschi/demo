package com.texoit.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AwardReportItemDTO {

  private String producer;
  private Integer interval;
  private Integer previousWin;
  private Integer followingWin;

}
