package com.hoang.jobfinder.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadUrlResponseDTO {
  private String uploadUrl;
  private String key;
  private Integer expireDurationMinute;
}
