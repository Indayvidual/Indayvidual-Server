package com.indayvidual.server.domain.user.entity;

import com.indayvidual.server.common.BaseEntity;
import com.indayvidual.server.domain.user.entity.enums.Provider;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "user_provider")
public class UserProvider extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "provider_email")
    private String providerEmail;

    @Builder.Default
    private Boolean isActive = true;
}