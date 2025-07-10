package com.indayvidual.server.domain.user.service.UserService;

import com.indayvidual.server.domain.user.dto.response.LoginResponseDTO;
import com.indayvidual.server.domain.user.dto.request.SignupRequestDTO;

public interface UserAuthService {
    LoginResponseDTO signupWithEmail(SignupRequestDTO request);
    LoginResponseDTO loginWithEmailAndPassword(String email, String password);
}