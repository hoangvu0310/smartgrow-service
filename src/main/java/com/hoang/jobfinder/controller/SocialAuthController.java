package com.hoang.jobfinder.controller;

import com.hoang.jobfinder.SocialAuthRouter;
import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.dto.ApiResponse;
import com.hoang.jobfinder.dto.auth.request.SocialAuthRequestDTO;
import com.hoang.jobfinder.dto.auth.response.TokenResponseDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping(Const.API_PREFIX + "/socialAuth")
@AllArgsConstructor
public class SocialAuthController {
  private final SocialAuthRouter router;

  @PostMapping("/authenticate")
  public ResponseEntity<ApiResponse<TokenResponseDTO>> socialAuthenticate(@RequestBody @Valid SocialAuthRequestDTO socialAuthRequestDTO) throws GeneralSecurityException, IOException {
    ApiResponse<TokenResponseDTO> response = ApiResponse.successResponse(
        router.get(socialAuthRequestDTO.getAuthType()).socialAccountAuthenticate(socialAuthRequestDTO)
    );
    return ResponseEntity.ok(response);
  }

}
