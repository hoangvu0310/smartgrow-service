package com.hoang.jobfinder.service;

import com.hoang.jobfinder.dto.FileTypeDTO;
import com.hoang.jobfinder.dto.UploadUrlResponseDTO;
import com.hoang.jobfinder.dto.profile.request.UserProfileEditRequestDTO;
import com.hoang.jobfinder.dto.profile.response.UserProfileResponseDTO;

public interface UserProfileService {
  UserProfileResponseDTO findProfileByUserId();
  UserProfileResponseDTO editProfile(UserProfileEditRequestDTO editRequestDTO);
  UploadUrlResponseDTO generateAvatarUploadUrl(FileTypeDTO fileTypeDTO);
}
