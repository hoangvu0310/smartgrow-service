package com.hoang.smartgrow.dto.auth.response;

import com.hoang.smartgrow.common.Enum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDTO {
  private Long userId;
  private String username;
  private String email;
  private String phoneNumber;
  private String fullName;
  private Enum.Role role;
}
