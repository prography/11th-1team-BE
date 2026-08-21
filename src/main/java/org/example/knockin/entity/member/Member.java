package org.example.knockin.entity.member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.knockin.entity.agreement.MemberAgreement;
import org.example.knockin.entity.alarm.Alarm;
import org.example.knockin.entity.alarm.AlarmSetting;
import org.example.knockin.entity.alarm.Notification;
import org.example.knockin.entity.auth.Authentication;
import org.example.knockin.entity.board.RoommateBoard;
import org.example.knockin.entity.board.RoommateBoardDeclaration;
import org.example.knockin.entity.board.RoommateBoardInterest;
import org.example.knockin.entity.chat.ChatRoomMember;
import org.example.knockin.entity.inquiry.Inquiry;
import org.example.knockin.entity.inquiry.InquiryComment;
import org.example.knockin.entity.life.MemberLifePattern;
import org.example.knockin.entity.life.PreferenceCondition;
import org.example.knockin.entity.room.RoomProfile;
import org.example.knockin.entity.auth.LoginProviderType;
import org.example.knockin.global.entity.CreatedAtEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "member",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_member_provider",
                columnNames = {"provider_type", "provider_id"}
        )
)
public class Member extends CreatedAtEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type", nullable = false, length = 50)
    private LoginProviderType providerType;

    @Column(name = "provider_id", nullable = false, length = 50)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private MemberRole role;

    @Column(name = "is_delete", nullable = false, length = 5, comment = "삭제 여부")
    private boolean isDelete;

    @Column(name = "device_id", length = 50, comment = "기기 UUID")
    private String deviceId;

    @Column(name = "fcm_token", length = 512, comment = "Firebase Messaging이 생성한 실제 토큰")
    private String fcmToken;

    @Column(name = "email", length = 512, comment = "SSO 이메일")
    private String email;

    @Column(name = "name", comment = "SSO 이름")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", length = 50, comment = "디바이스 플랫폼")
    private DevicePlatform platform;

    @Column(name = "deleted_at", comment = "삭제 일시")
    private LocalDateTime deletedAt;

    public void delete() {
        this.isDelete = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void deleteRecover() {
        this.isDelete = false;
        this.deletedAt = null;
    }

    public void changeRole(MemberRole memberRole) {
        this.role = memberRole;
    }

    public void setFcmProps(String deviceId, String fcmToken, DevicePlatform platform) {
        this.deviceId = deviceId;
        this.fcmToken = fcmToken;
        this.platform = platform;
    }

    public void clearFcmProps() {
        this.deviceId = "";
        this.fcmToken = "";
        this.platform = null;
    }
}
