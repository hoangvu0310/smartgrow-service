package com.hoang.smartgrow.dto.auth.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponseDTO {
  private String accessToken;

  private String refreshToken;

  private UserInfoDTO user;
}
