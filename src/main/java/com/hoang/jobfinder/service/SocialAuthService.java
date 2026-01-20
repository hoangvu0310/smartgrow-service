package com.hoang.jobfinder.service;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.dto.auth.request.SocialAuthRequestDTO;
import com.hoang.jobfinder.dto.auth.response.TokenResponseDTO;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface SocialAuthService {
  Enum.AuthType authType();
  TokenResponseDTO socialAccountAuthenticate(SocialAuthRequestDTO requestDTO) throws GeneralSecurityException, IOException;
}
