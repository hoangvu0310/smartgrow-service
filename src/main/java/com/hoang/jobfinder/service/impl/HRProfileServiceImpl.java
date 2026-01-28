package com.hoang.jobfinder.service.impl;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.common.ResultCode;
import com.hoang.jobfinder.dto.FileTypeDTO;
import com.hoang.jobfinder.dto.UploadUrlResponseDTO;
import com.hoang.jobfinder.dto.auth.response.AccountInfoDTO;
import com.hoang.jobfinder.dto.file.RequestFileUploadUrlDTO;
import com.hoang.jobfinder.dto.profile.request.HRProfileEditRequestDTO;
import com.hoang.jobfinder.dto.profile.response.HRProfileResponseDTO;
import com.hoang.jobfinder.entity.HRProfile;
import com.hoang.jobfinder.exception.JobFinderException;
import com.hoang.jobfinder.repository.HRProfileRepository;
import com.hoang.jobfinder.service.HRProfileService;
import com.hoang.jobfinder.service.SupabaseS3Service;
import com.hoang.jobfinder.util.FileUtil;
import com.hoang.jobfinder.util.UserUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class HRProfileServiceImpl implements HRProfileService {
  private HRProfileRepository hrProfileRepository;

  private ModelMapper modelMapper;

  private SupabaseS3Service supabaseS3Service;

  @Override
  public HRProfileResponseDTO findProfileByHRId() {
    AccountInfoDTO info = UserUtil.getCurrentUser();

    HRProfile profile = hrProfileRepository.findHRProfileByUserId(info.getUserId());
    if (profile == null) {
      throw new JobFinderException(ResultCode.NOT_FOUND);
    }

    HRProfileResponseDTO responseDTO = modelMapper.map(profile, HRProfileResponseDTO.class);
    responseDTO.setAvatarUrl(supabaseS3Service.generatePublicGetUrl(profile.getAvatarUrlKey()));
    return responseDTO;
  }

  @Override
  @Transactional
  public HRProfileResponseDTO editProfile(HRProfileEditRequestDTO editRequestDTO) {
    AccountInfoDTO info = UserUtil.getCurrentUser();

    HRProfile hrProfile = hrProfileRepository.findHRProfileByUserId(info.getUserId());
    if (hrProfile == null) {
      throw new JobFinderException(ResultCode.NOT_FOUND);
    }

    hrProfile.setAvatarUrlKey(editRequestDTO.getAvatarUrlKey());
    hrProfile.setDescription(editRequestDTO.getDescription());
    hrProfile.setTitle(editRequestDTO.getTitle());
    hrProfile.setFullName(editRequestDTO.getFullName());
    hrProfile.setPhoneNumber(editRequestDTO.getPhoneNumber());

    return modelMapper.map(hrProfile, HRProfileResponseDTO.class);
  }

  @Override
  public UploadUrlResponseDTO generateAvatarUploadUrl(FileTypeDTO fileTypeDTO) {
    FileUtil.validateImageFileType(fileTypeDTO.getFileType());

    AccountInfoDTO infoDTO = UserUtil.getCurrentUser();

    String key = Const.StorageBucketFolder.HR_AVATAR + "/hr-" + infoDTO.getUserId() + "-profile-avatar";
    RequestFileUploadUrlDTO fileUploadUrlDTO = RequestFileUploadUrlDTO.builder()
        .fileType(fileTypeDTO.getFileType())
        .fileKey(key)
        .isBucketPrivate(false)
        .build();

    return UploadUrlResponseDTO.builder()
        .uploadUrl(supabaseS3Service.generateSignedUploadUrl(fileUploadUrlDTO))
        .key(key)
        .expireDurationMinute(Const.PRESIGNED_URL_DURATION)
        .build();
  }
}
