package com.texoit.demo.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReportService {

  public static final int MIN_AWARDS = 2;
  private final AwardRepository awardRepository;

  public AwardReportDTO getData() {
    log.info("Retriving awards for report...");
    final var winners = awardRepository.findAll(
        Example.of(Award.builder().winner(Boolean.TRUE).build()));
    if (winners.size() == 0) {
      log.info("No winners found.");
      return AwardReportDTO.builder().build();
    }
    log.info("Found {} winners of awards.", winners.size());
    log.info("Grouping awards by producers");
    final var awardsByProducersMap = winners.stream()
        .collect(Collectors.groupingBy(Award::getProducers));
    log.info("Total winner producers {}", awardsByProducersMap.keySet().size());
    log.info("Transforming result in pre report data.");
    final var unfilteredReportData =
        transformToReportItem(awardsByProducersMap);

    return AwardReportDTO.builder()
        .min(filterAwardsWithMinIntervals(unfilteredReportData))
        .max(filterAwardsWithMaxIntervals(unfilteredReportData))
        .build();
  }

  private static List<AwardReportItemDTO> transformToReportItem(
      Map<String, List<Award>> awardsByProducersMap) {
    log.warn("Only producers with minimal of two awards will be accepted.");
    return awardsByProducersMap.entrySet().stream()
        .filter(entry -> entry.getValue().size() >= MIN_AWARDS)
        .flatMap(entry -> {
          final var iterator = entry.getValue().stream()
              .mapToInt(Award::getYear)
              .sorted()
              .iterator();
          final List<AwardReportItemDTO> itens = new ArrayList<>();
          var previous = iterator.next();
          while (iterator.hasNext()) {
            final var next = iterator.next();
            final var itemDTO = AwardReportItemDTO.builder()
                .interval(next - previous)
                .previousWin(previous)
                .followingWin(next)
                .producer(entry.getKey())
                .build();
            itens.add(itemDTO);
            log.info("ReportItem={}", itemDTO);
            previous = next;
          }
          return itens.stream();
        })
        .collect(Collectors.toList());
  }

  private List<AwardReportItemDTO> filterAwardsWithMinIntervals(
      List<AwardReportItemDTO> unfilteredReportData) {
    log.info("Filtering awards with min interval");

    final var minInterval = unfilteredReportData.stream()
        .mapToInt(AwardReportItemDTO::getInterval)
        .min()
        .orElseThrow();

    log.info("Min interval found {}", minInterval);

    return unfilteredReportData.stream()
        .filter(item -> item.getInterval().equals(minInterval))
        .sorted(Comparator.comparing(AwardReportItemDTO::getPreviousWin))
        .collect(Collectors.toList());
  }

  private List<AwardReportItemDTO> filterAwardsWithMaxIntervals(
      List<AwardReportItemDTO> unfilteredReportData) {
    log.info("Filtering awards with max interval");

    final var maxInterval = unfilteredReportData.stream()
        .mapToInt(AwardReportItemDTO::getInterval)
        .max()
        .orElseThrow();

    log.info("Max interval found {}", maxInterval);

    return unfilteredReportData.stream()
        .filter(item -> item.getInterval().equals(maxInterval))
        .sorted(Comparator.comparing(AwardReportItemDTO::getPreviousWin))
        .collect(Collectors.toList());
  }

}
