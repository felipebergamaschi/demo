package com.texoit.demo.config;

import com.texoit.demo.domain.Award;
import com.texoit.demo.domain.AwardRepository;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat.Builder;
import org.apache.commons.csv.CSVRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional
@Profile("!test")
public class CSVDatabaseLoader {

  public enum Headers {
    year, title, studios, producers, winner
  }

  public static final String MOVIELIST_CSV = "classpath:csv/movielist-original.csv";
  public static final String STRING_VALUE_BOOLEAN_TRUE = "yes";
  public static final String DELIMITER = ";";
  private final AwardRepository awardRepository;

  @PostConstruct
  public void load() {
    log.info("Loading CSV into database...");
    final var records = extractedFromCsv();
    log.info("Found {} records", records.size());
    final var entities = mapRecordToEntity(records);
    log.info("Saving records to database...");
    awardRepository.saveAllAndFlush(entities);
  }

  private List<CSVRecord> extractedFromCsv() {
    log.info("CSV headers={}", Arrays.stream(Headers.class.getEnumConstants()).toList());
    log.info("CSV delimiter={}", DELIMITER);
    final var csvFormat = Builder.create()
        .setHeader(Headers.class)
        .setSkipHeaderRecord(true)
        .setDelimiter(DELIMITER)
        .build();

    log.info("Loading CSV file from {}", MOVIELIST_CSV);
    try (var in = new FileReader(ResourceUtils.getFile(MOVIELIST_CSV))) {
      final var csvParser = csvFormat.parse(in);
      log.info("Retriving records from CSV file...");
      return csvParser.getRecords();
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  private List<Award> mapRecordToEntity(List<CSVRecord> records) {
    log.info("Mapping {} records to Award class", records.size());
    return records.stream()
        .flatMap(record -> {
          final var producers = record.get(Headers.producers)
              .replace(" and ", ", ")
              .split(", ");

          return Arrays.stream(producers).map(s -> Award.builder()
              .year(Integer.valueOf(record.get(Headers.year)))
              .title(record.get(Headers.title))
              .producers(s)
              .studios(record.get(Headers.studios))
              .winner(STRING_VALUE_BOOLEAN_TRUE.equalsIgnoreCase(record.get(Headers.winner)))
              .build());
        })
        .toList();
  }

}
