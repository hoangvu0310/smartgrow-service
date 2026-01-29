package com.hoang.jobfinder.entity.company;

import com.hoang.jobfinder.common.Enum;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "company_draft")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDraft {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long draftId;

  @Column(name = "company_id")
  private Long companyId;

  @Column(name = "payload", columnDefinition = "jsonb")
  private String payload;

  @Column(name = "status", length = 20)
  @Enumerated(EnumType.STRING)
  private Enum.EditStatus status;

  @Column(name = "edited_by")
  private String editedBy;

  @Column(name = "edited_at")
  private Instant editedAt;

  @Column(name = "handled_by")
  private String handledBy;

  @Column(name = "handled_at")
  private Instant handledAt;
}
