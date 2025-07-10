package com.indayvidual.server.domain.user.service.UserService;

import com.indayvidual.server.domain.user.dto.response.LoginResponseDTO;
import com.indayvidual.server.domain.user.dto.request.SignupRequestDTO;
import com.indayvidual.server.domain.user.entity.User;
import com.indayvidual.server.domain.user.entity.enums.Provider;
import com.indayvidual.server.domain.user.entity.enums.Role;
import com.indayvidual.server.domain.user.entity.enums.Status;
import com.indayvidual.server.domain.user.repository.UserRepository;
import com.indayvidual.server.global.config.security.JwtTokenProvider;
import com.indayvidual.server.global.config.security.UserAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDTO signupWithEmail(SignupRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = userRepository.save(User.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .username(request.getNickname())
                .status(Status.ACTIVE)
                .role(Role.ROLE_USER)
                .provider(Provider.LOCAL)
                .build());

        return createLoginResponse(user);
    }

    @Override
    public LoginResponseDTO loginWithEmailAndPassword(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (user.getProvider() != Provider.LOCAL) {
            throw new IllegalArgumentException("소셜 로그인 유저입니다. 이메일 로그인 불가");
        }

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return createLoginResponse(user);
    }

    private LoginResponseDTO createLoginResponse(User user) {
        Authentication authentication = new UserAuthentication(user.getId().toString(), null, null);
        String token = jwtTokenProvider.generateToken(authentication);
        return new LoginResponseDTO(user.getId(), user.getUsername(), user.getEmail(), token);
    }
}