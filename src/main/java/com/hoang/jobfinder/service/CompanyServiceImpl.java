package com.hoang.jobfinder.service;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.common.ErrorCode;
import com.hoang.jobfinder.dto.auth.response.AccountInfoDTO;
import com.hoang.jobfinder.dto.company.request.CompanyInfoPostRequestDTO;
import com.hoang.jobfinder.dto.company.response.CompanyAssetResponseDTO;
import com.hoang.jobfinder.dto.company.response.CompanyDTO;
import com.hoang.jobfinder.entity.HR;
import com.hoang.jobfinder.entity.company.*;
import com.hoang.jobfinder.exception.JobFinderException;
import com.hoang.jobfinder.repository.CompanyDraftRepository;
import com.hoang.jobfinder.repository.CompanyRepository;
import com.hoang.jobfinder.repository.DescriptionTagRepository;
import com.hoang.jobfinder.repository.HRRepository;
import com.hoang.jobfinder.util.FileUtil;
import com.hoang.jobfinder.util.UserUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CompanyServiceImpl implements CompanyService {
  private CompanyRepository companyRepository;

  private DescriptionTagRepository descriptionTagRepository;

  private CompanyDraftRepository companyDraftRepository;

  private HRRepository hrRepository;

  private SupabaseS3Service supabaseS3Service;

  private ModelMapper modelMapper;

  private ObjectMapper objectMapper;

  @Override
  @Transactional
  public CompanyDTO createCompany(CompanyInfoPostRequestDTO requestDTO) {
    if (companyRepository.existsCompanyByCompanyName(requestDTO.getCompanyName())) {
      throw new JobFinderException(ErrorCode.BAD_REQUEST, "Tên công ty đã tồn tại");
    }

    Company newCompany = Company.builder()
        .companyName(requestDTO.getCompanyName())
        .address(requestDTO.getAddress())
        .latitude(requestDTO.getLatitude())
        .longitude(requestDTO.getLongitude())
        .description(requestDTO.getDescription())
        .websiteUrl(requestDTO.getWebsiteUrl())
        .companySize(requestDTO.getCompanySize())
        .companyStatus(Enum.CompanyStatus.PENDING)
        .build();

    AccountInfoDTO hrInfo = UserUtil.getCurrentUser();
    Set<DescriptionTag> tagList = new HashSet<>(descriptionTagRepository.findAllById(requestDTO.getTagIds()));
    List<CompanyAsset> assetList = requestDTO.getCompanyAssets()
        .stream()
        .map(asset -> {
            FileUtil.validateImageFileType(asset.getFileType());

            return CompanyAsset.builder()
                .assetKey(asset.getAssetKey())
                .assetType(asset.getAssetType())
                .company(newCompany)
                .build();
          }
        )
        .toList();


    newCompany.getTags().addAll(tagList);
    newCompany.getHrList().add(hrRepository.findHRById(hrInfo.getUserId()));
    newCompany.getCompanyAssets().addAll(assetList);

    companyRepository.save(newCompany);

    return companyMapper(newCompany);
  }

  @Override
  @Transactional
  public CompanyDraftDTO editCompanyInfo(CompanyInfoPostRequestDTO requestDTO, Long companyId) {
    Company company = companyRepository.findCompanyByCompanyId(companyId).get(0);
    AccountInfoDTO hrInfo = UserUtil.getCurrentUser();
    HR hr = hrRepository.findHRById(hrInfo.getUserId());

    if (!company.getHrList().contains(hr) && !hrInfo.getRole().equals(Enum.Role.HR_ADMIN)) {
      throw new JobFinderException(ErrorCode.FORBIDDEN, "Tài khoản không có quyền chỉnh sửa", HttpStatus.FORBIDDEN);
    }

    CompanyDraft draft = CompanyDraft.builder()
        .companyId(companyId)
        .payload(objectMapper.writeValueAsString(requestDTO))
        .status(Enum.EditStatus.PENDING)
        .editedBy(hr.getEmail())
        .editedAt(Instant.now())
        .build();

    companyDraftRepository.save(draft);

    return modelMapper.map(draft, CompanyDraftDTO.class);
  }

  @Override
  public CompanyDTO getCompanyInfo(Long companyId) {
    Company company = companyRepository.findCompanyByCompanyId(companyId).get(0);

    return companyMapper(company);
  }

  private CompanyDTO companyMapper(Company company) {
    CompanyDTO dto = modelMapper.map(company, CompanyDTO.class);
    dto.setCompanyAssets(company.getCompanyAssets().stream().map(
        asset -> CompanyAssetResponseDTO.builder()
            .assetUrl(supabaseS3Service.generatePublicGetUrl(asset.getAssetKey()))
            .assetType(asset.getAssetType())
            .build()
        ).toList()
    );

    return dto;
  }
}
