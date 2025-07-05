package com.indayvidual.server.domain.temp.entity;

import com.indayvidual.server.common.BaseEntity;
import com.indayvidual.server.domain.temp.entity.enums.TempStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Temp extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TempStatus status;

    public void updateStatus(TempStatus status) {
        this.status = status;
    }

    public void updateDescription(String description) {
        this.description = description;
    }
}
