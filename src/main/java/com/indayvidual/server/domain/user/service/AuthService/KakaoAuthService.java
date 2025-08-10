package com.indayvidual.server.domain.user.service.AuthService;

import com.indayvidual.server.domain.user.dto.external.KakaoProfile;
import com.indayvidual.server.domain.user.dto.response.LoginResponseDTO;
import com.indayvidual.server.domain.user.entity.User;
import com.indayvidual.server.domain.user.entity.UserProvider;
import com.indayvidual.server.domain.user.entity.enums.Provider;
import com.indayvidual.server.domain.user.entity.enums.Role;
import com.indayvidual.server.domain.user.entity.enums.Status;
import com.indayvidual.server.domain.user.repository.UserProviderRepository;
import com.indayvidual.server.domain.user.repository.UserRepository;
import com.indayvidual.server.domain.user.service.UserService.RefreshTokenService;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import com.indayvidual.server.global.client.KakaoApiClient;
import com.indayvidual.server.global.config.security.JwtTokenProvider;
import com.indayvidual.server.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final KakaoApiClient kakaoApiClient;
    private final UserRepository userRepository;
    private final UserProviderRepository userProviderRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public LoginResponseDTO loginWithKakao(String kakaoAccessToken) {
        KakaoProfile profile;
        try {
            // 1. 카카오 access token 검증 + 프로필 조회
            profile = kakaoApiClient.fetchProfile(kakaoAccessToken);
            } catch (GeneralException e) {
                throw e; // 이미 매핑된 ErrorStatus
            } catch (Exception e) {
                throw new GeneralException(ErrorStatus.AUTH_OAUTH_PROVIDER_ERROR);
        }

        // 2. 우리 서비스의 User 조회 or 신규 생성
        User user = userProviderRepository
                .findByProviderAndProviderId(Provider.KAKAO, profile.getId().toString())
                .map(UserProvider::getUser)
                .orElseGet(() -> signUpKakaoUser(profile));

        // 3. Access/Refresh Token 발급
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = refreshTokenService.saveRefreshToken(user);  // 변경된 구조 활용

        // 4. LoginResponseDTO 구성
        return LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    private User signUpKakaoUser(KakaoProfile profile) {
        String email = Optional.ofNullable(profile.getKakao_account())
                .map(KakaoProfile.KakaoAccount::getEmail)
                .orElse(null); // 이메일 동의 X 케이스 고려
        String nickname = Optional.ofNullable(profile.getKakao_account())
                .map(KakaoProfile.KakaoAccount::getProfile)
                .map(KakaoProfile.KakaoAccount.Profile::getNickname)
                .orElse("카카오 사용자");
        String image = Optional.ofNullable(profile.getKakao_account())
                .map(KakaoProfile.KakaoAccount::getProfile)
                .map(KakaoProfile.KakaoAccount.Profile::getProfile_image_url)
                .orElse(null);

        User newUser = User.builder()
                .email(email)
                .username(nickname)
                .profile_image(image)
                .status(Status.ACTIVE)
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(newUser);

        userProviderRepository.save(UserProvider.builder()
                .user(newUser)
                .provider(Provider.KAKAO)
                .providerId(profile.getId().toString())
                .providerEmail(email)
                .isActive(true)
                .build());

        return newUser;
    }
}
