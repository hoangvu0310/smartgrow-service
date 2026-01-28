package com.hoang.jobfinder.controller;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.dto.ApiResponse;
import com.hoang.jobfinder.dto.file.RequestFileUploadUrlDTO;
import com.hoang.jobfinder.service.SupabaseS3Service;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Const.API_PREFIX + "/file")
@AllArgsConstructor
public class FileUploadController {
  private SupabaseS3Service supabaseS3Service;

  @PostMapping("/uploadUrl")
  public ResponseEntity<ApiResponse<String>> generateUploadUrl(@RequestBody RequestFileUploadUrlDTO uploadUrlDTO) {
    return ResponseEntity.ok(ApiResponse.successResponse(supabaseS3Service.generateSignedUploadUrl(uploadUrlDTO)));
  }
}
