package org.example.knockin.meta.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.knockin.meta.dto.AuthEmailModifyDto;
import org.example.knockin.authentication.entity.AuthenticationType;
import org.example.knockin.global.entity.BaseEntity;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "auth_email")
public class AuthEmail extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 100)
    private String domain;

    @Column(nullable = false, length = 100)
    private String name;

    @Builder.Default
    @ColumnDefault(value = "false")
    private Boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    private AuthenticationType dtype;

    public void modifyAuthEmail(AuthEmailModifyDto.Request request) {
        this.domain = request.getDomain();
        this.name = request.getName();
        this.dtype = request.getType();
    }

    public void delete() {
        this.isDeleted = true;
    }
}
