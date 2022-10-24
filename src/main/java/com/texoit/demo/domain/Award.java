package com.texoit.demo.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Entity
@Table(name = "award")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class Award {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "AW_ID", nullable = false)
  private Long id;

  @Column(name = "AW_YEAR", nullable = false)
  private Integer year;

  @Column(name = "AW_TITLE", nullable = false)
  private String title;

  @Column(name = "AW_STUDIOS", nullable = false)
  private String studios;

  @Column(name = "AW_PRODUCERS", nullable = false)
  private String producers;

  @Column(name = "AW_WINNER", nullable = false)
  private Boolean winner;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Award award = (Award) o;
    return id != null && Objects.equals(id, award.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
