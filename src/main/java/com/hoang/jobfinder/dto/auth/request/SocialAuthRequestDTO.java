package com.hoang.jobfinder.dto.auth.request;

import com.hoang.jobfinder.common.Enum;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SocialAuthRequestDTO {
  private String idToken;

  private Enum.AuthType authType;

  @NotBlank(message = "Id của thiết bị không được để trống")
  private String deviceId;

  @Nullable
  private Enum.Platform platform;
}
