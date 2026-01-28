package com.hoang.jobfinder.controller.hr;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.dto.ApiResponse;
import com.hoang.jobfinder.dto.FileTypeDTO;
import com.hoang.jobfinder.dto.UploadUrlResponseDTO;
import com.hoang.jobfinder.dto.profile.request.HRProfileEditRequestDTO;
import com.hoang.jobfinder.dto.profile.response.HRProfileResponseDTO;
import com.hoang.jobfinder.service.HRProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Const.HR_API_PREFIX + "/profile")
@AllArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_HR_ADMIN', 'ROLE_HR')")
public class HRProfileController {
  private HRProfileService hrProfileService;

  @GetMapping()
  public ResponseEntity<ApiResponse<HRProfileResponseDTO>> getProfileById() {
    return ResponseEntity.ok(ApiResponse.successResponse(hrProfileService.findProfileByHRId()));
  }

  @PatchMapping()
  public ResponseEntity<ApiResponse<HRProfileResponseDTO>> editProfile(@RequestBody HRProfileEditRequestDTO editRequestDTO) {
    return ResponseEntity.ok(ApiResponse.successResponse(hrProfileService.editProfile(editRequestDTO)));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<UploadUrlResponseDTO>> generateAvatarUploadUrl(@RequestBody FileTypeDTO fileTypeDTO) {
    return ResponseEntity.ok(ApiResponse.successResponse(hrProfileService.generateAvatarUploadUrl(fileTypeDTO)));
  }
}
