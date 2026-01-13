package com.hoang.smartgrow.util;

import com.hoang.smartgrow.dto.auth.response.UserResponseDTO;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class UserUtil {
  public static UserResponseDTO getCurrentUser() {
    Object userDTO = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
    if (userDTO instanceof UserResponseDTO) {
      return (UserResponseDTO) userDTO;
    }
    return null;
  }
}
