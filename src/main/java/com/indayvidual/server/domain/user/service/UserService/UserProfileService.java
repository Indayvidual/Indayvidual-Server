package com.indayvidual.server.domain.user.service.UserService;

import com.indayvidual.server.domain.user.dto.response.UserResponseDTO;
import com.indayvidual.server.domain.user.entity.User;
import com.indayvidual.server.domain.user.repository.UserRepository;
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
    private final PasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;

    @Value("${app.profile.default-image-url:https://.../default_profile.png}")
    private String defaultProfileImageUrl;

    @Transactional(readOnly = true)
    public UserResponseDTO.Profile getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

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
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.changeUsername(newUsername);
    }

    public void updatePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("기존 비밀번호와 동일합니다.");
        }
        user.changePassword(passwordEncoder.encode(newPassword));
    }

    public String updateProfileImage(Long userId, MultipartFile image) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String oldUrl = user.getProfile_image();

        // 업로드
        String newUrl = s3Uploader.uploadProfileImage(userId, image);
        user.changeProfileImage(newUrl);

        // 이전 이미지가 기본이미지가 아닌 경우 삭제
        if (oldUrl != null && !oldUrl.isBlank() && !oldUrl.equals(defaultProfileImageUrl)) {
            s3Uploader.deleteObjectByUrl(oldUrl);
        }
        return newUrl;
    }
}
