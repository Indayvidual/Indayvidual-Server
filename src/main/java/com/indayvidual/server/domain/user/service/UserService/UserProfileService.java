package com.indayvidual.server.domain.user.service.UserService;

import com.indayvidual.server.domain.user.dto.request.DeleteAccountRequest;
import com.indayvidual.server.domain.user.dto.response.UserResponseDTO;
import com.indayvidual.server.domain.user.entity.User;
import com.indayvidual.server.domain.user.entity.UserProvider;
import com.indayvidual.server.domain.user.entity.enums.Provider;
import com.indayvidual.server.domain.user.entity.enums.Status;
import com.indayvidual.server.domain.user.repository.RefreshTokenRepository;
import com.indayvidual.server.domain.user.repository.UserProviderRepository;
import com.indayvidual.server.domain.user.repository.UserRepository;
import com.indayvidual.server.domain.user.service.AuthService.ReauthService;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import com.indayvidual.server.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserProviderRepository userProviderRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ReauthService reauthService;
    private final PasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;

    @Value("${app.profile.default-image-url:https://.../default_profile.png}")
    private String defaultProfileImageUrl;

    @Transactional(readOnly = true)
    public UserResponseDTO.Profile getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MYPAGE_USER_NOT_FOUND));

        String img = (user.getProfile_image() == null || user.getProfile_image().isBlank())
                ? defaultProfileImageUrl
                : user.getProfile_image();

        return UserResponseDTO.Profile.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .imageUrl(img)
                .build();
    }

    public void updateUsername(Long userId, String newUsername) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MYPAGE_USER_NOT_FOUND));
        String target = newUsername == null ? "" : newUsername.trim();
        if (target.isEmpty()) throw new GeneralException(ErrorStatus._BAD_REQUEST);
        // 동일 닉네임(대소문자/공백 무시)인 경우 변경 스킵
        if (user.getUsername() != null && user.getUsername().trim().equalsIgnoreCase(target)) {
            return;
        }
        // 타 유저가 사용 중이면 차단
        boolean exists = userRepository.existsByUsernameIgnoreCaseAndIdNot(target, userId);
        if (exists) throw new GeneralException(ErrorStatus.MYPAGE_USERNAME_DUPLICATED);
        user.changeUsername(target);
    }

    @Transactional(readOnly = true)
    public boolean isUsernameAvailable(String candidate, Long currentUserId) {
        String target = candidate == null ? "" : candidate.trim();
        if (target.isEmpty()) return false;
        if (currentUserId != null) {
            // 본인 닉네임은 중복으로 간주하지 않음
            boolean sameAsMine = userRepository.findById(currentUserId)
                    .map(u -> u.getUsername() != null && u.getUsername().trim().equalsIgnoreCase(target))
                    .orElse(false);
            if (sameAsMine) return true;
            return !userRepository.existsByUsernameIgnoreCaseAndIdNot(target, currentUserId);
        }
        return !userRepository.existsByUsernameIgnoreCase(target);
    }

    public void updatePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MYPAGE_USER_NOT_FOUND));

        // 비밀번호 기반 변경 불가(소셜-only 또는 LOCAL 미연동) 차단
        boolean hasLocalProvider = userProviderRepository
                .findByUserAndProvider(user, Provider.LOCAL)
                .filter(UserProvider::getIsActive)
                .isPresent();
        if (!hasLocalProvider || user.getPassword() == null) {
            throw new GeneralException(ErrorStatus.MYPAGE_PASSWORD_NOT_SUPPORTED);
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new GeneralException(ErrorStatus.MYPAGE_PASSWORD_MISMATCH);
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new GeneralException(ErrorStatus.MYPAGE_PASSWORD_SAME);
        }
        user.changePassword(passwordEncoder.encode(newPassword));
    }

    public String updateProfileImage(Long userId, MultipartFile image) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MYPAGE_USER_NOT_FOUND));

        if (image == null || image.isEmpty()) {
            throw new GeneralException(ErrorStatus.MYPAGE_IMAGE_EMPTY);
        }

        // 허용 형식 & 크기(최대 5MB)
        final long MAX = 5L * 1024 * 1024;
        String ctype = image.getContentType() == null ? "" : image.getContentType().toLowerCase();
        boolean allowed = ctype.startsWith("image/jpeg") || ctype.startsWith("image/png") || ctype.startsWith("image/webp");
        if (!allowed) throw new GeneralException(ErrorStatus.MYPAGE_IMAGE_TYPE_INVALID);
        if (image.getSize() > MAX) throw new GeneralException(ErrorStatus.MYPAGE_IMAGE_TOO_LARGE);

        String oldUrl = user.getProfile_image();

        // 업로드
        String newUrl;
        try {
            newUrl = s3Uploader.uploadProfileImage(userId, image);
        } catch (GeneralException ge) {
            // S3Uploader가 내부 에러를 래핑했을 수 있으니 그대로 전파
            throw ge;
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.MYPAGE_IMAGE_UPLOAD_FAILED);
        }
        user.changeProfileImage(newUrl);

        // 이전 이미지가 기본이미지가 아닌 경우 삭제
        if (oldUrl != null && !oldUrl.isBlank() && !oldUrl.equals(defaultProfileImageUrl)) {
            s3Uploader.deleteObjectByUrl(oldUrl);
        }

        return newUrl;
    }


    public void deleteMyAccount(Long userId, String reauthToken, DeleteAccountRequest req) {
        // 1) 재인증 토큰 검증 (하드/소프트 공통)
        reauthService.assertReauthOrThrow(userId, reauthToken, true); // 1회성 소비

        // 2) 유저 로드
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MYPAGE_USER_NOT_FOUND));

        // 3) RefreshToken 전부 무효화
        refreshTokenRepository.deleteAllByUserId(userId);

        // (선택) Access token 블랙리스트 등록: 남은 만료시간 만큼만 유지
        // blacklistService.add(accessToken, remainingTtl); // 구현해두면 더 안전

        // 4) 이미지 정리 (기본이미지 아닌 경우에만 삭제)
        String old = user.getProfile_image();
        boolean deletable = old != null && !old.isBlank() && !old.equals(defaultProfileImageUrl);

        if (req.isHard()) {
            // ---- 하드 삭제 ----
            if (deletable) s3Uploader.deleteObjectByUrl(old);
            userRepository.delete(user); // 연관 엔티티 orphanRemoval=true면 함께 제거
            return;
        }

        // ---- 소프트 삭제 ----
        if (deletable) s3Uploader.deleteObjectByUrl(old);

        // 민감 데이터 최소화 + 계정 비활성
        user.changeProfileImage(defaultProfileImageUrl);
        user.changeUsername("탈퇴한 사용자");
        user.changePassword(null); // 이메일 비번 로그인 불가(소셜만 있던 계정이면 그대로 null)

        user.setStatus(Status.DELETED); // 상태 전이 (필드가 enum Status에 포함돼야 함)
        // 필요 시 deletedAt 필드가 있으면 기록: user.setDeletedAt(LocalDateTime.now());

        // 이메일 처리 정책
        // 1) 유지: 아무것도 안 함
        // 2) 익명화: user.setEmail("deleted_" + user.getId() + "@example.invalid"); 또는 해시
        // 3) 재사용 허용: 익명화하여 unique 제약 충족

        // 사유 기록하고 싶으면 별도 테이블에 저장 (UserDeletionLog 등)
        // deletionLogRepository.save(UserDeletionLog.of(userId, req.getReason()));
    }
}
