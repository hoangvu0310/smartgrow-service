package com.hoang.jobfinder.entity.company;

import com.hoang.jobfinder.common.Enum;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "company_asset")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyAsset {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "company_asset_id")
  private Long companyAssetId;

  @Column(name = "asset_key")
  private String assetKey;

  @Column(name = "assetType", length = 20)
  @Enumerated(EnumType.STRING)
  private Enum.CompanyAssetType assetType;

  @Column(name = "file_type", length = 20)
  private String fileType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "companyId", nullable = false)
  private Company company;
}
