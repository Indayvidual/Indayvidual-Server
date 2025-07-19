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
import com.indayvidual.server.global.config.security.JwtTokenProvider;
import com.indayvidual.server.global.config.security.JwtValidationType;
import com.indayvidual.server.global.config.security.UserAuthentication;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

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
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
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
    public LoginResponseDTO loginWithEmailAndPassword(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        boolean hasLocalProvider = userProviderRepository
                .findByUserAndProvider(user, Provider.LOCAL)
                .filter(UserProvider::getIsActive)
                .isPresent();

        // UserProvider에서 LOCAL provider 확인
        if (!hasLocalProvider) {
            throw new IllegalArgumentException("소셜 로그인 유저입니다. 이메일 로그인 불가");
        }

        // 비밀번호가 null인 경우 (소셜 로그인만 있는 경우)
        if (user.getPassword() == null) {
            throw new IllegalArgumentException("소셜 로그인 유저입니다. 이메일 로그인 불가");
        }

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
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