package com.hoang.smartgrow.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "refresh_token")
public class RefreshToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "refresh_token_id", nullable = false)
  private Long refreshTokenId;

  @Column(name = "token", nullable = false)
  private String token;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive;

  @Column(name = "expiration_date", nullable = false)
  private Instant expirationDate;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "created_by", nullable = false)
  private String createdBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
