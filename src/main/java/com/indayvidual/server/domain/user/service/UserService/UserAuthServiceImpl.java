package com.indayvidual.server.domain.user.service.UserService;

import com.indayvidual.server.domain.user.dto.response.LoginResponseDTO;
import com.indayvidual.server.domain.user.dto.request.SignupRequestDTO;
import com.indayvidual.server.domain.user.dto.response.SignupResponseDTO;
import com.indayvidual.server.domain.user.entity.RefreshToken;
import com.indayvidual.server.domain.user.entity.User;
import com.indayvidual.server.domain.user.entity.UserProvider;
import com.indayvidual.server.domain.user.entity.enums.Provider;
import com.indayvidual.server.domain.user.entity.enums.Role;
import com.indayvidual.server.domain.user.entity.enums.Status;
import com.indayvidual.server.domain.user.repository.RefreshTokenRepository;
import com.indayvidual.server.domain.user.repository.UserProviderRepository;
import com.indayvidual.server.domain.user.repository.UserRepository;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import com.indayvidual.server.global.config.security.JwtTokenProvider;
import com.indayvidual.server.global.exception.GeneralException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;


@Service
@RequiredArgsConstructor
@Transactional
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;
    private final UserProviderRepository userProviderRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public SignupResponseDTO signupWithEmail(SignupRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new GeneralException(ErrorStatus.AUTH_EMAIL_DUPLICATED);
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .username(request.getUsername())
                .phone_number(request.getPhoneNumber())
                .status(Status.ACTIVE)
                .role(Role.ROLE_USER)
                .build();

        User savedUser = userRepository.save(user);

        UserProvider userProvider = UserProvider.builder()
                .user(savedUser)
                .provider(Provider.LOCAL)
                .providerId(savedUser.getEmail()) // 로컬의 경우 이메일을 providerId로 사용
                .providerEmail(savedUser.getEmail())
                .build();

        userProviderRepository.save(userProvider);

        return SignupResponseDTO.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .username(savedUser.getUsername())
                .message("회원가입이 완료되었습니다.")
                .build();
    }

    @Override
    @Transactional
    public LoginResponseDTO loginWithEmailAndPassword(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.AUTH_INVALID_CREDENTIALS));

        boolean hasLocalProvider = userProviderRepository
                .findByUserAndProvider(user, Provider.LOCAL)
                .filter(UserProvider::getIsActive)
                .isPresent();

        // UserProvider에서 LOCAL provider 확인
        if (!hasLocalProvider || user.getPassword() == null) {
            throw new GeneralException(ErrorStatus.AUTH_LOCAL_NOT_AVAILABLE);
        }

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new GeneralException(ErrorStatus.AUTH_INVALID_CREDENTIALS);
        }

        return createLoginResponse(user);
    }

    private LoginResponseDTO createLoginResponse(User user) {
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        refreshTokenRepository.save(RefreshToken.of(
                user.getId(),
                claims.getId(),
                refreshToken,
                Duration.between(Instant.now(), claims.getExpiration().toInstant())
        ));

        return LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public void logoutWithRefreshToken(String refreshToken) {
        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        refreshTokenRepository.findByTokenId(claims.getId())
                .ifPresent(RefreshToken::revoke);
    }
}