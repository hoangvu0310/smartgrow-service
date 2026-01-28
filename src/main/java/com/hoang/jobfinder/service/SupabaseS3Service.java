package com.hoang.jobfinder.service;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.dto.file.RequestFileUploadUrlDTO;
import com.hoang.jobfinder.property.SupabaseS3Properties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class SupabaseS3Service {
  private S3Client s3Client;

  private SupabaseS3Properties supabaseS3Properties;

  private S3Presigner s3Presigner;

  public String uploadFile(MultipartFile file, String folder, boolean isBucketPrivate) throws IOException {
    String key = folder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

    PutObjectRequest putRequest = PutObjectRequest.builder()
        .bucket(isBucketPrivate ? supabaseS3Properties.getPrivateBucketName() : supabaseS3Properties.getPublicBucketName())
        .key(key)
        .contentType(file.getContentType())
        .build();

    s3Client.putObject(putRequest, RequestBody.fromInputStream(
        file.getInputStream(), file.getSize()));
    return key;
  }

  public String updateFile(MultipartFile file, String key, boolean isBucketPrivate) throws IOException {
    PutObjectRequest putRequest = PutObjectRequest.builder()
        .bucket(isBucketPrivate ? supabaseS3Properties.getPrivateBucketName() : supabaseS3Properties.getPublicBucketName())
        .key(key)
        .contentType(file.getContentType())
        .build();

    s3Client.putObject(putRequest, RequestBody.fromInputStream(
        file.getInputStream(), file.getSize()));
    return key;
  }

  public String generatePublicGetUrl(String key) {
    return supabaseS3Properties.getPublicAssetLink() + "/" + key;
  }

  public String generateSignedUploadUrl(RequestFileUploadUrlDTO uploadUrlDTO) {
    PutObjectRequest putRequest = PutObjectRequest.builder()
        .bucket(uploadUrlDTO.isBucketPrivate() ? supabaseS3Properties.getPrivateBucketName() : supabaseS3Properties.getPublicBucketName())
        .key(uploadUrlDTO.getFileKey())
        .contentType(uploadUrlDTO.getFileType())
        .build();

    PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
        .putObjectRequest(putRequest)
        .signatureDuration(Duration.ofMinutes(Const.PRESIGNED_URL_DURATION))
        .build();

    PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(putObjectPresignRequest);
    log.info("Presigned put URL: [{}]", presignedRequest.url().toString());

    return presignedRequest.url().toExternalForm();

  }

  public String generateSignedGetUrl(String key) {
    GetObjectRequest getRequest = GetObjectRequest.builder()
        .bucket(supabaseS3Properties.getPrivateBucketName())
        .key(key)
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(Const.PRESIGNED_URL_DURATION))
        .getObjectRequest(getRequest)
        .build();

    PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
    log.info("Presigned get URL: [{}]", presignedRequest.url().toString());
    log.info("HTTP method: [{}]", presignedRequest.httpRequest().method());

    return presignedRequest.url().toExternalForm();

  }

  public void deleteFile(String objectKey, boolean isBucketPrivate) {
    DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
        .bucket(isBucketPrivate ? supabaseS3Properties.getPrivateBucketName() : supabaseS3Properties.getPublicBucketName())
        .key(objectKey)
        .build();

    s3Client.deleteObject(deleteRequest);
  }
}
