package com.indayvidual.server.domain.user.entity;

import com.indayvidual.server.common.BaseEntity;
import com.indayvidual.server.domain.user.entity.enums.Provider;
import com.indayvidual.server.domain.user.entity.enums.Role;
import com.indayvidual.server.domain.user.entity.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Role role;  // 초기값: ROLE_USER

    @Enumerated(EnumType.STRING)
    private Provider provider;
    private String provider_id;

    private String username;
    private String profile_image;
    private String phone_number;

}
