package com.hoang.jobfinder.dto.auth.response;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.entity.User;
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

  public static UserInfoDTO fromUser(User user) {
    return UserInfoDTO.builder()
        .userId(user.getUserId())
        .username(user.getUsername())
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .fullName(user.getFullName())
        .role(user.getRole())
        .build();
  }
}
