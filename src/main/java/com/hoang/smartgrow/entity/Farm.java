package com.hoang.smartgrow.entity;

import com.hoang.smartgrow.entity.base.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@Table(name = "farm")
@SuperBuilder
@NoArgsConstructor
public class Farm extends BaseAuditableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "farm_id")
  private Long farmId;
}
