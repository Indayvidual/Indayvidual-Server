package com.indayvidual.server.domain.user.service.UserService;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import java.io.IOException;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import com.indayvidual.server.global.exception.GeneralException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Uploader {

    private final AmazonS3 amazonS3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @PostConstruct
    void check() {
        log.info("S3 bucket='{}'", bucket);
        try {
            // 존재/권한/리전 확인 (HeadBucket 권한 필요)
            amazonS3Client.headBucket(new com.amazonaws.services.s3.model.HeadBucketRequest(bucket));
            String loc = amazonS3Client.getBucketLocation(bucket);
            log.info("✅ S3 bucket OK. location={}", loc);
        } catch (com.amazonaws.services.s3.model.AmazonS3Exception e) {
            log.error("❌ S3 check failed: code={}, status={}, msg={}", e.getErrorCode(), e.getStatusCode(), e.getMessage());
            // 부팅을 살리고 싶으면 예외를 다시 던지지 마세요.
        } catch (com.amazonaws.SdkClientException e) {
            log.error("❌ S3 SDK client error: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("❌ Unexpected error in S3 check", e);
        }
    }

    // 확장자 추출 유틸
    private String extOf(String originalFileName) {
        if (originalFileName == null) return "";
        int idx = originalFileName.lastIndexOf('.');
        return (idx != -1) ? originalFileName.substring(idx + 1) : "";
    }

    public String uploadProfileImage(Long userId, MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        try {
            String ext = extOf(file.getOriginalFilename());
            String key = String.format("profiles/%d/%s.%s",
                    userId, UUID.randomUUID(), (ext.isBlank() ? "jpg" : ext));

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            // 공개 URL이 필요하다면 퍼블릭 리드 ACL (버킷 정책과 보안정책에 따라 조정)
            PutObjectRequest req = new PutObjectRequest(bucket, key, file.getInputStream(), metadata);

            amazonS3Client.putObject(req);
            String url = amazonS3Client.getUrl(bucket, key).toString();

            return generateReadUrl(bucket, key, Duration.ofHours(24));

        } catch (IOException e) {
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteObjectByUrl(String url) {
        if (url == null || url.isBlank()) return;
        try {
            // url → key 변환 (버킷 호스트 제거)
            URI uri = URI.create(url);
            String key = uri.getPath().startsWith("/") ? uri.getPath().substring(1) : uri.getPath();
            amazonS3Client.deleteObject(bucket, key);
        } catch (Exception e) {
            log.warn("S3 delete skip. url={}, err={}", url, e.getMessage());
        }
    }

    public String generateReadUrl(String bucket, String key, Duration ttl) {
        var expires = Date.from(Instant.now().plus(ttl));
        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(bucket, key)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expires);
        URL signedUrl = amazonS3Client.generatePresignedUrl(req);
        return signedUrl.toString();
    }
}

