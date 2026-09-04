package org.example.knockin.meta.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.knockin.global.entity.BaseEntity;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "app_version")
public class AppVersion extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 30)
    private String version;

    @Enumerated(EnumType.STRING)
    private PlatformType platformType;

    @Enumerated(EnumType.STRING)
    private UpdateType updateType;

    @Column(nullable = false, length = 30)
    private String minVersion;

    @Builder.Default
    @ColumnDefault(value = "false")
    private Boolean isDeleted = false;

    public void deleteAppVersion() {
        this.isDeleted = true;
    }

    public void modifyVersion(String version, String minVersion, UpdateType updateType, PlatformType platformType) {
        this.version = version;
        this.minVersion = minVersion;
        this.updateType = updateType;
        this.platformType = platformType;
    }
}
