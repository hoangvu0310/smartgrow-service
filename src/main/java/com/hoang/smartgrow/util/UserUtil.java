package com.hoang.smartgrow.util;

import com.hoang.smartgrow.dto.auth.response.UserInfoDTO;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class UserUtil {
  public static UserInfoDTO getCurrentUser() {
    Object userDTO = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
    if (userDTO instanceof UserInfoDTO) {
      return (UserInfoDTO) userDTO;
    }
    return null;
  }
}
