package com.indayvidual.server.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UserResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Profile {
        private Long userId;
        private String email;
        private String username;
        private String imageUrl; // user.profile_image 매핑
    }
}