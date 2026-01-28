package com.hoang.jobfinder.service;

import com.hoang.jobfinder.dto.FileTypeDTO;
import com.hoang.jobfinder.dto.UploadUrlResponseDTO;
import com.hoang.jobfinder.dto.profile.request.HRProfileEditRequestDTO;
import com.hoang.jobfinder.dto.profile.response.HRProfileResponseDTO;

public interface HRProfileService {
  HRProfileResponseDTO findProfileByHRId();
  HRProfileResponseDTO editProfile(HRProfileEditRequestDTO editRequestDTO);
  UploadUrlResponseDTO generateAvatarUploadUrl(FileTypeDTO fileTypeDTO);
}
