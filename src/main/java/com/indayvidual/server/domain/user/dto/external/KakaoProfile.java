package com.indayvidual.server.domain.user.dto.external;

import lombok.Getter;

@Getter
public class KakaoProfile {               // /v2/user/me 응답 중 필요한 필드만
    private Long id;
    private KakaoAccount kakao_account;

    @Getter public static class KakaoAccount {
        private String email;
        private Profile profile;

        @Getter public static class Profile {
            private String nickname;
            private String profile_image_url;
        }
    }
}
