package com.hoang.jobfinder.dto.company.response;

import com.hoang.jobfinder.common.Enum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyAssetResponseDTO {
  private String assetUrl;
  private Enum.CompanyAssetType assetType;
}
