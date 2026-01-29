package com.hoang.jobfinder.entity.company;

import com.hoang.jobfinder.common.Enum;
import lombok.Data;

import java.time.Instant;

@Data
public class CompanyDraftDTO {
  private Long companyId;
  private String payload;
  private Enum.EditStatus status;
  private String editedBy;
  private Instant editedAt;
  private String handledBy;
  private Instant handledAt;
}
