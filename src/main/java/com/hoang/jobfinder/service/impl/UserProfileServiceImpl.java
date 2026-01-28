package com.hoang.jobfinder.service.impl;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.common.ErrorCode;
import com.hoang.jobfinder.common.ResultCode;
import com.hoang.jobfinder.dto.FileTypeDTO;
import com.hoang.jobfinder.dto.UploadUrlResponseDTO;
import com.hoang.jobfinder.dto.auth.response.AccountInfoDTO;
import com.hoang.jobfinder.dto.file.RequestFileUploadUrlDTO;
import com.hoang.jobfinder.dto.profile.request.UserProfileEditRequestDTO;
import com.hoang.jobfinder.dto.profile.response.UserProfileResponseDTO;
import com.hoang.jobfinder.entity.user.EducationInfo;
import com.hoang.jobfinder.entity.user.UserProfile;
import com.hoang.jobfinder.entity.user.WorkExperience;
import com.hoang.jobfinder.exception.JobFinderException;
import com.hoang.jobfinder.repository.UserProfileRepository;
import com.hoang.jobfinder.service.SupabaseS3Service;
import com.hoang.jobfinder.service.UserProfileService;
import com.hoang.jobfinder.util.FileUtil;
import com.hoang.jobfinder.util.UserUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserProfileServiceImpl implements UserProfileService {
  private UserProfileRepository userProfileRepository;

  private SupabaseS3Service supabaseS3Service;

  private ModelMapper modelMapper;

  @Override
  public UserProfileResponseDTO findProfileByUserId() throws JobFinderException {
    AccountInfoDTO info = UserUtil.getCurrentUser();

    UserProfile userProfile = userProfileRepository.findUserProfileByUserId(info.getUserId());
    if (userProfile == null) {
      throw new JobFinderException(ResultCode.NOT_FOUND);
    }

    UserProfileResponseDTO responseDTO = modelMapper.map(userProfile, UserProfileResponseDTO.class);
    responseDTO.setAvatarUrl(supabaseS3Service.generatePublicGetUrl(userProfile.getAvatarUrlKey()));

    return responseDTO;
  }

  @Override
  @Transactional
  public UserProfileResponseDTO editProfile(UserProfileEditRequestDTO editRequestDTO) {
    AccountInfoDTO info = UserUtil.getCurrentUser();

    UserProfile userProfile = userProfileRepository.findUserProfileByUserId(info.getUserId());
    if (userProfile == null) {
      throw new JobFinderException(ResultCode.NOT_FOUND);
    }

    userProfile.setFullName(editRequestDTO.getFullName());
    userProfile.setPhoneNumber(editRequestDTO.getPhoneNumber());
    userProfile.setAvatarUrlKey(editRequestDTO.getAvatarUrlKey());
    userProfile.setAddress(editRequestDTO.getAddress());
    userProfile.setDescription(editRequestDTO.getDescription());

    if (editRequestDTO.getEducationInfoList() != null) {
      List<EducationInfo> educationInfos = userProfile.getEducationInfoList();
      Map<Long, EducationInfo> educationInfoMap = educationInfos.stream()
          .collect(Collectors.toMap(EducationInfo::getId, educationInfo -> educationInfo));
      educationInfos.clear();

      editRequestDTO.getEducationInfoList().forEach(
          educationInfoDTO -> {
            EducationInfo educationInfo = educationInfoDTO.getId() != null
                ? Optional.ofNullable(educationInfoMap.get(educationInfoDTO.getId()))
                .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy thông tin học vấn với id được gửi"))
                : new EducationInfo();

            educationInfo.setMajor(educationInfoDTO.getMajor());
            educationInfo.setFacility(educationInfoDTO.getFacility());
            educationInfo.setStartTime(educationInfoDTO.getStartTime());
            educationInfo.setEndTime(educationInfoDTO.getEndTime());
            educationInfo.setPositionInList(educationInfoDTO.getPositionInList());
            educationInfo.setUserProfile(userProfile);
          }
      );
    }

    if (editRequestDTO.getWorkExperienceList() != null) {
      List<WorkExperience> workExperiences = userProfile.getWorkExperienceList();
      Map<Long, WorkExperience> workExperienceMap = workExperiences.stream()
          .collect(Collectors.toMap(WorkExperience::getId, workExperience -> workExperience));
      workExperiences.clear();

      editRequestDTO.getWorkExperienceList().forEach(
          workExperienceDTO -> {
            WorkExperience workExperience = workExperienceDTO.getId() != null
                ? Optional.ofNullable(workExperienceMap.get(workExperienceDTO.getId()))
                .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy thông tin kinh nghiệm làm việc với id được gửi"))
                : new WorkExperience();

            workExperience.setCompany(workExperienceDTO.getCompany());
            workExperience.setJobTitle(workExperienceDTO.getJobTitle());
            workExperience.setDescription(workExperienceDTO.getDescription());
            workExperience.setStartTime(workExperienceDTO.getStartTime());
            workExperience.setEndTime(workExperienceDTO.getEndTime());
            workExperience.setPositionInList(workExperienceDTO.getPositionInList());
            workExperience.setUserProfile(userProfile);
          }
      );
    }

    return modelMapper.map(userProfile, UserProfileResponseDTO.class);
  }

  @Override
  public UploadUrlResponseDTO generateAvatarUploadUrl(FileTypeDTO fileTypeDTO) {
    FileUtil.validateImageFileType(fileTypeDTO.getFileType());

    AccountInfoDTO infoDTO = UserUtil.getCurrentUser();

    String key = Const.StorageBucketFolder.USER_AVATAR + "/user-" + infoDTO.getUserId() + "-profile-avatar";
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
