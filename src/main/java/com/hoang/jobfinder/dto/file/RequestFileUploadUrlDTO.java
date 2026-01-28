package com.hoang.jobfinder.dto.file;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestFileUploadUrlDTO {
  private String fileKey;
  private String fileType;
  private boolean isBucketPrivate;
}
