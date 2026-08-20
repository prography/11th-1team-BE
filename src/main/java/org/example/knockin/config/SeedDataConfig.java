package org.example.knockin.config;

import lombok.RequiredArgsConstructor;
import org.example.knockin.dto.MessageType;
import org.example.knockin.entity.agreement.Agreement;
import org.example.knockin.entity.agreement.AgreementLog;
import org.example.knockin.entity.agreement.AgreementType;
import org.example.knockin.entity.agreement.MemberAgreement;
import org.example.knockin.entity.alarm.Alarm;
import org.example.knockin.entity.alarm.AlarmSetting;
import org.example.knockin.entity.alarm.AlarmSettingType;
import org.example.knockin.entity.alarm.Notification;
import org.example.knockin.entity.alarm.NotificationAlarm;
import org.example.knockin.entity.auth.ApproveType;
import org.example.knockin.entity.auth.Authentication;
import org.example.knockin.entity.auth.AuthenticationApprove;
import org.example.knockin.entity.auth.AuthenticationType;
import org.example.knockin.entity.auth.LoginProviderType;
import org.example.knockin.entity.board.Faq;
import org.example.knockin.entity.board.RoommateBoard;
import org.example.knockin.entity.board.RoommateBoardDeclaration;
import org.example.knockin.entity.board.RoommateBoardFile;
import org.example.knockin.entity.board.RoommateBoardInterest;
import org.example.knockin.entity.board.RoommateBoardOption;
import org.example.knockin.entity.chat.ChatRoomFile;
import org.example.knockin.entity.chat.ChatRoomMember;
import org.example.knockin.entity.chat.ChatRoomMessage;
import org.example.knockin.entity.chat.ChattingRequired;
import org.example.knockin.entity.chat.ChattingRequiredAlarm;
import org.example.knockin.entity.chat.ChattingRequiredStatus;
import org.example.knockin.entity.chat.ChattingRoom;
import org.example.knockin.entity.chat.ChattingScore;
import org.example.knockin.entity.file.BasicInformationFile;
import org.example.knockin.entity.file.File;
import org.example.knockin.entity.file.FileType;
import org.example.knockin.entity.inquiry.Inquiry;
import org.example.knockin.entity.inquiry.InquiryCategory;
import org.example.knockin.entity.inquiry.InquiryComment;
import org.example.knockin.entity.life.*;
import org.example.knockin.entity.member.BasicInformation;
import org.example.knockin.entity.member.Block;
import org.example.knockin.entity.member.Gender;
import org.example.knockin.entity.member.Member;
import org.example.knockin.entity.member.MemberDeclaration;
import org.example.knockin.entity.member.MemberInterest;
import org.example.knockin.entity.member.MemberPrivacy;
import org.example.knockin.entity.member.MemberPrivacyType;
import org.example.knockin.entity.member.MemberRole;
import org.example.knockin.entity.member.MemberState;
import org.example.knockin.entity.member.Search;
import org.example.knockin.entity.member.State;
import org.example.knockin.entity.payment.Payment;
import org.example.knockin.entity.payment.PaymentStatus;
import org.example.knockin.entity.payment.PaymentType;
import org.example.knockin.entity.payment.Point;
import org.example.knockin.entity.payment.PointLog;
import org.example.knockin.entity.payment.VarianceType;
import org.example.knockin.entity.room.ExcludeRoommateCalendar;
import org.example.knockin.entity.room.MyRoommate;
import org.example.knockin.entity.room.OfferRoomType;
import org.example.knockin.entity.room.Region;
import org.example.knockin.entity.room.RepeatRoommateCalendar;
import org.example.knockin.entity.room.RepeatType;
import org.example.knockin.entity.room.RoomExtraOption;
import org.example.knockin.entity.room.RoomExtraOptionFile;
import org.example.knockin.entity.room.RoomOfferProfile;
import org.example.knockin.entity.room.RoomSeekerProfile;
import org.example.knockin.entity.room.RoomSeekerProfileRegion;
import org.example.knockin.entity.room.RoomType;
import org.example.knockin.entity.room.RoomTypeFile;
import org.example.knockin.entity.room.RoommateCalendar;
import org.example.knockin.entity.room.RoommateCalendarAlarm;
import org.example.knockin.entity.room.RoommateCalendarCategory;
import org.example.knockin.entity.room.RoommateCalendarMember;
import org.example.knockin.entity.room.RoommateHouseRule;
import org.example.knockin.entity.room.RoommateMatchingRequired;
import org.example.knockin.entity.room.RoommateMatchingRequiredAlarm;
import org.example.knockin.entity.room.RoommateRequiredStatus;
import org.example.knockin.entity.room.RoommateScore;
import org.example.knockin.entity.room.SeekerRoomType;
import org.example.knockin.entity.utils.AppVersion;
import org.example.knockin.entity.utils.AuthEmail;
import org.example.knockin.entity.utils.PlatformType;
import org.example.knockin.entity.utils.UpdateType;
import org.example.knockin.global.entity.DeclarationType;
import org.example.knockin.repository.agreement.AgreementLogRepository;
import org.example.knockin.repository.agreement.AgreementRepository;
import org.example.knockin.repository.agreement.AgreementTypeRepository;
import org.example.knockin.repository.agreement.MemberAgreementRepository;
import org.example.knockin.repository.alarm.AlarmRepository;
import org.example.knockin.repository.alarm.AlarmSettingRepository;
import org.example.knockin.repository.alarm.NotificationAlarmRepository;
import org.example.knockin.repository.alarm.NotificationRepository;
import org.example.knockin.repository.auth.AuthenticationApproveRepository;
import org.example.knockin.repository.auth.AuthenticationRepository;
import org.example.knockin.repository.board.FaqRepository;
import org.example.knockin.repository.board.RoommateBoardDeclarationRepository;
import org.example.knockin.repository.board.RoommateBoardFileRepository;
import org.example.knockin.repository.board.RoommateBoardInterestRepository;
import org.example.knockin.repository.board.RoommateBoardOptionRepository;
import org.example.knockin.repository.board.RoommateBoardRepository;
import org.example.knockin.repository.chat.ChatRoomFileRepository;
import org.example.knockin.repository.chat.ChatRoomMemberRepository;
import org.example.knockin.repository.chat.ChatRoomMessageRepository;
import org.example.knockin.repository.chat.ChattingRequiredAlarmRepository;
import org.example.knockin.repository.chat.ChattingRequiredRepository;
import org.example.knockin.repository.chat.ChattingRoomRepository;
import org.example.knockin.repository.chat.ChattingScoreRepository;
import org.example.knockin.repository.file.BasicInformationFileRepository;
import org.example.knockin.repository.file.FileRepository;
import org.example.knockin.repository.inquiry.InquiryCategoryRepository;
import org.example.knockin.repository.inquiry.InquiryCommentRepository;
import org.example.knockin.repository.inquiry.InquiryRepository;
import org.example.knockin.repository.life.*;
import org.example.knockin.repository.member.BasicInformationRepository;
import org.example.knockin.repository.member.BlockRepository;
import org.example.knockin.repository.member.MemberDeclarationRepository;
import org.example.knockin.repository.member.MemberInterestRepository;
import org.example.knockin.repository.member.MemberPrivacyRepository;
import org.example.knockin.repository.member.MemberRepository;
import org.example.knockin.repository.member.SearchRepository;
import org.example.knockin.repository.member.StateRepository;
import org.example.knockin.repository.payment.PaymentRepository;
import org.example.knockin.repository.payment.PointLogRepository;
import org.example.knockin.repository.payment.PointRepository;
import org.example.knockin.repository.room.ExcludeRoommateCalendarRepository;
import org.example.knockin.repository.room.MyRoommateRepository;
import org.example.knockin.repository.room.OfferRoomTypeRepository;
import org.example.knockin.repository.room.RegionRepository;
import org.example.knockin.repository.room.RepeatRoommateCalendarRepository;
import org.example.knockin.repository.room.RoomExtraOptionFileRepository;
import org.example.knockin.repository.room.RoomExtraOptionRepository;
import org.example.knockin.repository.room.RoomOfferProfileRepository;
import org.example.knockin.repository.room.RoomSeekerProfileRegionRepository;
import org.example.knockin.repository.room.RoomSeekerProfileRepository;
import org.example.knockin.repository.room.RoomTypeFileRepository;
import org.example.knockin.repository.room.RoomTypeRepository;
import org.example.knockin.repository.room.RoommateCalendarAlarmRepository;
import org.example.knockin.repository.room.RoommateCalendarCategoryRepository;
import org.example.knockin.repository.room.RoommateCalendarMemberRepository;
import org.example.knockin.repository.room.RoommateCalendarRepository;
import org.example.knockin.repository.room.RoommateHouseRuleRepository;
import org.example.knockin.repository.room.RoommateMatchingRequiredAlarmRepository;
import org.example.knockin.repository.room.RoommateMatchingRequiredRepository;
import org.example.knockin.repository.room.RoommateScoreRepository;
import org.example.knockin.repository.room.SeekerRoomTypeRepository;
import org.example.knockin.repository.utils.AppVersionRepository;
import org.example.knockin.repository.utils.AuthEmailRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("prod")
public class SeedDataConfig implements CommandLineRunner {
    private final AgreementRepository agreementRepository;
    private final AgreementLogRepository agreementLogRepository;
    private final LifePatternRepository lifePatternRepository;
    private final LifePatternInformationRepository lifePatternInformationRepository;
    private final RegionRepository regionRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomExtraOptionRepository roomExtraOptionRepository;
    private final InquiryCategoryRepository inquiryCategoryRepository;
    private final AgreementTypeRepository agreementTypeRepository;

    private final AppVersionRepository appVersionRepository;
    private final AuthEmailRepository authEmailRepository;
    private final FaqRepository faqRepository;
    private final MemberRepository memberRepository;
    private final BasicInformationRepository basicInformationRepository;
    private final AuthenticationRepository authenticationRepository;
    private final AuthenticationApproveRepository authenticationApproveRepository;
    private final RoommateBoardRepository roommateBoardRepository;
    private final RoommateBoardOptionRepository roommateBoardOptionRepository;
    private final SearchRepository searchRepository;
    private final InquiryRepository inquiryRepository;
    private final InquiryCommentRepository inquiryCommentRepository;

    private final MemberAgreementRepository memberAgreementRepository;
    private final MemberPrivacyRepository memberPrivacyRepository;
    private final MemberLifePatternRepository memberLifePatternRepository;
    private final PreferenceConditionRepository preferenceConditionRepository;
    private final PreferenceConditionWeightRepository preferenceConditionWeightRepository;
    private final StateRepository stateRepository;
    private final BlockRepository blockRepository;
    private final MemberDeclarationRepository memberDeclarationRepository;
    private final MemberInterestRepository memberInterestRepository;
    private final FileRepository fileRepository;
    private final BasicInformationFileRepository basicInformationFileRepository;
    private final RoommateBoardFileRepository roommateBoardFileRepository;
    private final RoomOfferProfileRepository roomOfferProfileRepository;
    private final RoomSeekerProfileRepository roomSeekerProfileRepository;
    private final OfferRoomTypeRepository offerRoomTypeRepository;
    private final SeekerRoomTypeRepository seekerRoomTypeRepository;
    private final ChattingRequiredRepository chattingRequiredRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatRoomMessageRepository chatRoomMessageRepository;
    private final RoommateMatchingRequiredRepository roommateMatchingRequiredRepository;
    private final MyRoommateRepository myRoommateRepository;
    private final RoommateHouseRuleRepository roommateHouseRuleRepository;
    private final RoommateCalendarCategoryRepository roommateCalendarCategoryRepository;
    private final RoommateCalendarRepository roommateCalendarRepository;
    private final RoommateCalendarMemberRepository roommateCalendarMemberRepository;
    private final RoommateBoardInterestRepository roommateBoardInterestRepository;
    private final RoommateBoardDeclarationRepository roommateBoardDeclarationRepository;
    private final AlarmSettingRepository alarmSettingRepository;
    private final AlarmRepository alarmRepository;
    private final NotificationRepository notificationRepository;

    private final RoomSeekerProfileRegionRepository roomSeekerProfileRegionRepository;
    private final RoomTypeFileRepository roomTypeFileRepository;
    private final RoomExtraOptionFileRepository roomExtraOptionFileRepository;
    private final LifePatternFileRepository lifePatternFileRepository;
    private final ChatRoomFileRepository chatRoomFileRepository;
    private final MemberLifePatternLogRepository memberLifePatternLogRepository;
    private final PreferenceConditionLogRepository preferenceConditionLogRepository;
    private final PreferenceConditionWeightLogRepository preferenceConditionWeightLogRepository;
    private final RoommateScoreRepository roommateScoreRepository;
    private final NotificationAlarmRepository notificationAlarmRepository;
    private final ChattingRequiredAlarmRepository chattingRequiredAlarmRepository;
    private final RoommateMatchingRequiredAlarmRepository roommateMatchingRequiredAlarmRepository;
    private final RoommateCalendarAlarmRepository roommateCalendarAlarmRepository;
    private final RepeatRoommateCalendarRepository repeatRoommateCalendarRepository;
    private final ExcludeRoommateCalendarRepository excludeRoommateCalendarRepository;
    private final ChattingScoreRepository chattingScoreRepository;
    private final PaymentRepository paymentRepository;
    private final PointRepository pointRepository;
    private final PointLogRepository pointLogRepository;
    private final MemberLifePatternLogDegreeRepository memberLifePatternLogDegreeRepository;
    private final PreferenceConditionLogDegreeRepository preferenceConditionLogDegreeRepository;
    private final PreferenceConditionWeightLogDegreeRepository preferenceConditionWeightLogDegreeRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (agreementRepository.count() > 0) {
            return;
        }

        AgreementType agreementType1 = AgreementType.builder().name("서비스 이용약관").build();
        AgreementType agreementType2 = AgreementType.builder().name("개인정보 처리방침").build();
        agreementTypeRepository.saveAll(List.of(agreementType1, agreementType2));

        Agreement termsOfService1 = Agreement.builder().title("서비스 이용약관").contents("상세 내용...").isDeleted(false).isRequired(true).type(agreementType1).build();
        Agreement termsOfService2 = Agreement.builder().title("서비스 이용약관").contents("상세 내용... 수정1").isDeleted(false).isRequired(true).type(agreementType1).build();
        Agreement termsOfService3 = Agreement.builder().title("서비스 이용약관").contents("상세 내용... 수정2").isDeleted(false).isRequired(true).type(agreementType1).build();
        Agreement privacyPolicy = Agreement.builder().title("개인정보 처리방침").contents("상세 내용...").isDeleted(false).isRequired(true).type(agreementType2).build();
        agreementRepository.saveAll(List.of(termsOfService1, termsOfService2, termsOfService3, privacyPolicy));

        AgreementLog termsLog1 = AgreementLog.builder().agreement(termsOfService1).isCurrent(true).build();
        AgreementLog termsLog2 = AgreementLog.builder().agreement(termsOfService2).isCurrent(false).build();
        AgreementLog termsLog3 = AgreementLog.builder().agreement(termsOfService3).isCurrent(false).build();
        AgreementLog privacyLog = AgreementLog.builder().agreement(privacyPolicy).isCurrent(true).build();
        agreementLogRepository.saveAll(List.of(termsLog1, termsLog2, termsLog3, privacyLog));

        LifePattern bedtime = LifePattern.builder().name("취침시간").dtype(LifePatternType.SCALE).lifePatternDescription("평소 취침 시간은 어떤 편인가요?").preferenceDescription("평소 취침 시간은 어떤 편인가요?").isDeleted(false).sort(1).build();
        LifePattern cleanlinessSensitivity = LifePattern.builder().name("청결 민감도").dtype(LifePatternType.SCALE).lifePatternDescription("청결에 얼마나 민감하시나요?").preferenceDescription("청결에 얼마나 민감하시나요?").isDeleted(false).sort(2).build();
        LifePattern noiseSensitivity = LifePattern.builder().name("소음 민감도").dtype(LifePatternType.SCALE).lifePatternDescription("소음에 얼마나 민감하시나요?").preferenceDescription("소음에 얼마나 민감하시나요?").isDeleted(false).sort(3).build();
        LifePattern smoking = LifePattern.builder().name("흡연여부").dtype(LifePatternType.SINGLE_CHOICE).lifePatternDescription("흡연을 하시나요?").preferenceDescription("원하는 룸메이트의 흡연여부를 선택해주세요.").isDeleted(false).sort(4).build();
        LifePattern visitorFrequency = LifePattern.builder().name("방문객 빈도").dtype(LifePatternType.SCALE).lifePatternDescription("주로 얼마나 자주 방문객이 오시나요?").preferenceDescription("원하는 룸메이트의 방문객 빈도를 선택해주세요.").isDeleted(false).sort(5).build();
        LifePattern pet = LifePattern.builder().name("반려동물 여부").dtype(LifePatternType.SINGLE_CHOICE).lifePatternDescription("반려동물을 키우고 있나요?").preferenceDescription("원하는 룸메이트의 반려동물 여부를 선택해주세요.").isDeleted(false).sort(6).build();
        LifePattern personalSpaceImportance = LifePattern.builder().name("개인 공간 중요도").dtype(LifePatternType.SCALE).lifePatternDescription("개인 공간을 얼마나 중요하게 생각하시나요?").preferenceDescription("원하는 룸메이트의 개인 공간 중요도를 선택해주세요.").isDeleted(false).sort(7).build();
        LifePattern personalityStyle = LifePattern.builder().name("성격 스타일").dtype(LifePatternType.SCALE).lifePatternDescription("나의 성격은 어떤 편인가요?").preferenceDescription("원하는 룸메이트의 성격을 선택해주세요.").isDeleted(false).sort(8).build();
        lifePatternRepository.saveAll(List.of(
                bedtime,
                visitorFrequency,
                smoking,
                pet,
                cleanlinessSensitivity,
                noiseSensitivity,
                personalSpaceImportance,
                personalityStyle
        ));

        List<LifePatternInformation> lifePatternInformationList = new ArrayList<>();
        lifePatternInformationList.addAll(createLifePatternInformation(bedtime,
                "매우 일찍", "일찍", "보통", "늦게", "매우 늦게"));
        lifePatternInformationList.addAll(createLifePatternInformation(visitorFrequency,
                "거의 안 옴", "가끔", "보통", "자주", "매우 자주"));
        lifePatternInformationList.addAll(createLifePatternInformation(smoking,
                "하지 않아요", "하고 있어요"));
        lifePatternInformationList.addAll(createLifePatternInformation(pet,
                "키우지 않아요", "키우고 있어요"));
        lifePatternInformationList.addAll(createLifePatternInformation(cleanlinessSensitivity,
                "전혀 민감하지 않음", "민감하지 않음", "보통", "민감함", "매우 민감함"));
        lifePatternInformationList.addAll(createLifePatternInformation(noiseSensitivity,
                "전혀 민감하지 않음", "민감하지 않음", "보통", "민감함", "매우 민감함"));
        lifePatternInformationList.addAll(createLifePatternInformation(personalSpaceImportance,
                "전혀 중요하지 않음", "중요하지 않음", "보통", "중요함", "매우 중요함"));
        lifePatternInformationList.addAll(createLifePatternInformation(personalityStyle,
                "매우 내향적", "내향적", "보통", "외향적", "매우 외향적"));
        lifePatternInformationRepository.saveAll(lifePatternInformationList);

        Region seoul = Region.builder().name("서울특별시").scope(1).parent(null).build();
        Region gyeonggi = Region.builder().name("경기도").scope(1).parent(null).build();
        regionRepository.saveAll(List.of(seoul, gyeonggi));

        List<Region> level2Districts = new ArrayList<>();
        List<Region> level3Towns = new ArrayList<>();

        String[] seoulGuList = {"강남구", "마포구", "송파구", "서초구", "성동구", "종로구", "영등포구", "용산구"};
        String[][] seoulDongList = {
                {"역삼동", "삼성동", "청담동", "논현동"},
                {"서교동", "합정동", "망원동", "연남동"},
                {"잠실동", "문정동", "가락동", "방이동"},
                {"반포동", "방배동", "서초동", "양재동"},
                {"성수동", "옥수동", "왕십리동", "마장동"},
                {"혜화동", "명륜동", "삼청동", "평창동"},
                {"여의도동", "당산동", "문래동", "신길동"},
                {"이태원동", "한남동", "이촌동", "후암동"}
        };

        for (int i = 0; i < seoulGuList.length; i++) {
            Region gu = Region.builder().name(seoulGuList[i]).scope(2).parent(seoul).build();
            level2Districts.add(gu);
        }

        String[] gyeonggiSiList = {"수원시 영통구", "성남시 분당구", "고양시 일산동구", "용인시 수지구", "안양시 동안구", "부천시", "남양주시", "화성시"};
        String[][] gyeonggiDongList = {
                {"영통동", "망포동", "매탄동", "이의동"},
                {"삼평동", "서현동", "정자동", "야탑동"},
                {"장항동", "마두동", "백석동", "식사동"},
                {"풍덕천동", "죽전동", "동천동", "상현동"},
                {"범계동", "평촌동", "관양동", "호계동"},
                {"중동", "상동", "심곡동", "소사본동"},
                {"다산동", "별내동", "와부읍", "진접읍"},
                {"동탄동", "향남읍", "봉담읍", "새솔동"}
        };

        for (int i = 0; i < gyeonggiSiList.length; i++) {
            Region si = Region.builder().name(gyeonggiSiList[i]).scope(2).parent(gyeonggi).build();
            level2Districts.add(si);
        }
        regionRepository.saveAll(level2Districts);

        for (int i = 0; i < seoulGuList.length; i++) {
            Region parentGu = level2Districts.get(i);
            for (int j = 0; j < 4; j++) {
                level3Towns.add(Region.builder().name(seoulDongList[i][j]).scope(3).parent(parentGu).build());
            }
        }
        for (int i = 0; i < gyeonggiSiList.length; i++) {
            Region parentSi = level2Districts.get(i + seoulGuList.length);
            for (int j = 0; j < 4; j++) {
                level3Towns.add(Region.builder().name(gyeonggiDongList[i][j]).scope(3).parent(parentSi).build());
            }
        }
        regionRepository.saveAll(level3Towns);

        RoomType oneRoom = RoomType.builder().name("원룸").isDeleted(false).build();
        RoomType twoRoom = RoomType.builder().name("투룸").isDeleted(false).build();
        RoomType threeRoomPlus = RoomType.builder().name("쓰리룸+").isDeleted(false).build();
        RoomType officetel = RoomType.builder().name("오피스텔").isDeleted(false).build();
        RoomType apartment = RoomType.builder().name("아파트").isDeleted(false).build();
        roomTypeRepository.saveAll(List.of(oneRoom, twoRoom, threeRoomPlus, officetel, apartment));

        RoomExtraOption fullOption = RoomExtraOption.builder().name("풀옵션").isDeleted(false).build();
        RoomExtraOption elevator = RoomExtraOption.builder().name("엘레베이터").isDeleted(false).build();
        RoomExtraOption parking = RoomExtraOption.builder().name("주차가능").isDeleted(false).build();
        RoomExtraOption veranda = RoomExtraOption.builder().name("베란다/발코니").isDeleted(false).build();
        RoomExtraOption petAvailable = RoomExtraOption.builder().name("반려동물 협의").isDeleted(false).build();
        RoomExtraOption securityCctv = RoomExtraOption.builder().name("보안/CCTV").isDeleted(false).build();

        roomExtraOptionRepository.saveAll(List.of(fullOption, elevator, parking, veranda, petAvailable, securityCctv));

        InquiryCategory catAccount = InquiryCategory.builder().title("계정/인증").isDeleted(false).build();
        InquiryCategory catRoom = InquiryCategory.builder().title("방 등록/매칭").isDeleted(false).build();
        InquiryCategory catAbuse = InquiryCategory.builder().title("불량유저 신고").isDeleted(false).build();
        InquiryCategory catEtc = InquiryCategory.builder().title("기타 문의").isDeleted(false).build();
        InquiryCategory feedBack = InquiryCategory.builder().title("의견 남기기").isDeleted(false).build();

        inquiryCategoryRepository.saveAll(List.of(catAccount, catRoom, catAbuse, catEtc, feedBack));

        // ==================== 전수 JPA 엔티티 E2E 덤프 데이터 생성 ====================

        // 1. 앱 버전 (AppVersion)
        AppVersion androidVersion = AppVersion.builder()
                .platformType(PlatformType.ANDROID)
                .version("1.0.0")
                .minVersion("1.0.0")
                .updateType(UpdateType.SELECT)
                .isDeleted(false)
                .build();
        AppVersion iosVersion = AppVersion.builder()
                .platformType(PlatformType.IOS)
                .version("1.0.0")
                .minVersion("1.0.0")
                .updateType(UpdateType.SELECT)
                .isDeleted(false)
                .build();
        appVersionRepository.saveAll(List.of(androidVersion, iosVersion));

        // 2. 인증 이메일 도메인 (AuthEmail)
        AuthEmail univEmail = AuthEmail.builder()
                .domain("univ.ac.kr")
                .name("대학교 공통")
                .dtype(AuthenticationType.STUDENT)
                .isDeleted(false)
                .build();
        AuthEmail companyEmail = AuthEmail.builder()
                .domain("company.com")
                .name("일반 기업")
                .dtype(AuthenticationType.COMPANY)
                .isDeleted(false)
                .build();
        authEmailRepository.saveAll(List.of(univEmail, companyEmail));

        // 3. 회원 (Member) 및 기본정보 (BasicInformation) & 회원 상태 (State) & 공개 설정 (MemberPrivacy)
        Member adminMember = Member.builder()
                .providerType(LoginProviderType.KAKAO)
                .providerId("admin_provider_123")
                .role(MemberRole.ADMIN)
                .isDelete(false)
                .build();
        Member user1 = Member.builder()
                .providerType(LoginProviderType.KAKAO)
                .providerId("user1_provider_456")
                .role(MemberRole.USER)
                .isDelete(false)
                .build();
        Member user2 = Member.builder()
                .providerType(LoginProviderType.APPLE)
                .providerId("user2_provider_789")
                .role(MemberRole.USER)
                .isDelete(false)
                .build();
        Member user3 = Member.builder()
                .providerType(LoginProviderType.APPLE)
                .providerId("user2_provider_886")
                .role(MemberRole.USER)
                .isDelete(false)
                .build();
        memberRepository.saveAll(List.of(adminMember, user1, user2, user3));

        stateRepository.saveAll(List.of(
                State.builder().member(adminMember).states(MemberState.ACTIVE).build(),
                State.builder().member(user1).states(MemberState.ACTIVE).build(),
                State.builder().member(user2).states(MemberState.ACTIVE).build()
        ));

        memberPrivacyRepository.saveAll(List.of(
                MemberPrivacy.builder().member(user1).type(MemberPrivacyType.PUBLIC).build(),
                MemberPrivacy.builder().member(user2).type(MemberPrivacyType.PUBLIC).build()
        ));

        BasicInformation adminInfo = BasicInformation.builder()
                .member(adminMember)
                .name("관리자")
                .birth(LocalDate.of(1990, 1, 1))
                .gender(Gender.MALE)
                .email("admin@knockin.com")
                .build();
        BasicInformation user1Info = BasicInformation.builder()
                .member(user1)
                .name("김노크")
                .birth(LocalDate.of(2000, 5, 15))
                .gender(Gender.FEMALE)
                .email("user1@univ.ac.kr")
                .build();
        BasicInformation user2Info = BasicInformation.builder()
                .member(user2)
                .name("이인인")
                .birth(LocalDate.of(1998, 8, 20))
                .gender(Gender.MALE)
                .email("user2@company.com")
                .build();
        basicInformationRepository.saveAll(List.of(adminInfo, user1Info, user2Info));

        // 4. 회원 약관 동의 (MemberAgreement)
        memberAgreementRepository.saveAll(List.of(
                MemberAgreement.builder().member(user1).agreementLog(termsLog1).isAgreed(true).build(),
                MemberAgreement.builder().member(user1).agreementLog(privacyLog).isAgreed(true).build(),
                MemberAgreement.builder().member(user2).agreementLog(termsLog1).isAgreed(true).build(),
                MemberAgreement.builder().member(user2).agreementLog(privacyLog).isAgreed(true).build()
        ));

        // 5. 회원 생활패턴 (MemberLifePattern) & 선호조건 (PreferenceCondition & Weight)
        if (!lifePatternInformationList.isEmpty()) {
            memberLifePatternRepository.saveAll(List.of(
                    MemberLifePattern.builder().member(user1).lifePatternInformation(lifePatternInformationList.get(0)).build(),
                    MemberLifePattern.builder().member(user2).lifePatternInformation(lifePatternInformationList.get(0)).build()
            ));

            preferenceConditionRepository.saveAll(List.of(
                    PreferenceCondition.builder().member(user1).lifePatternInformation(lifePatternInformationList.get(0)).build(),
                    PreferenceCondition.builder().member(user2).lifePatternInformation(lifePatternInformationList.get(0)).build()
            ));

            preferenceConditionWeightRepository.saveAll(List.of(
                    PreferenceConditionWeight.builder().member(user1).lifePattern(bedtime).build(),
                    PreferenceConditionWeight.builder().member(user2).lifePattern(cleanlinessSensitivity).build()
            ));
        }

        // 6. 회원 관심 상대 (MemberInterest) & 차단 (Block) & 회원 신고 (MemberDeclaration)
        memberInterestRepository.save(MemberInterest.builder().sender(user1).receiver(user2).isDeleted(false).build());
        blockRepository.save(Block.builder().blocker(user1).blocked(user2).isDeleted(false).build());
        memberDeclarationRepository.save(MemberDeclaration.builder().reporter(user1).reported(user2).reason("불쾌한 채팅 메세지").declarationType(DeclarationType.PENDING).build());

        // 7. 자주 묻는 질문 (FAQ)
        Faq faq1 = Faq.builder()
                .title("룸메이트 매칭 서비스 이용 방법")
                .contents("룸메이트 게시글을 작성하고 원하는 조건의 룸메이트와 채팅을 신청해보세요.")
                .member(adminMember)
                .sort(1)
                .isDeleted(false)
                .build();
        Faq faq2 = Faq.builder()
                .title("신원 인증은 어떻게 하나요?")
                .contents("학생증 또는 재직증명서를 첨부하여 인증 신청 시 관리자 확인 후 인증이 완료됩니다.")
                .member(adminMember)
                .sort(2)
                .isDeleted(false)
                .build();
        faqRepository.saveAll(List.of(faq1, faq2));

        // 8. 학생/직장인 신원 인증 (Authentication & AuthenticationApprove)
        Authentication authUser1 = Authentication.builder()
                .member(user1)
                .type(AuthenticationType.STUDENT)
                .email("user1@univ.ac.kr")
                .code("123456")
                .isAccepted(true)
                .build();
        Authentication authUser2 = Authentication.builder()
                .member(user2)
                .type(AuthenticationType.COMPANY)
                .email("user2@company.com")
                .code("654321")
                .isAccepted(true)
                .build();
        authenticationRepository.saveAll(List.of(authUser1, authUser2));

        AuthenticationApprove approveUser1 = AuthenticationApprove.builder()
                .authentication(authUser1)
                .status(ApproveType.ACCEPTED)
                .build();
        AuthenticationApprove approveUser2 = AuthenticationApprove.builder()
                .authentication(authUser2)
                .status(ApproveType.ACCEPTED)
                .build();
        authenticationApproveRepository.saveAll(List.of(approveUser1, approveUser2));

        // 9. 파일 메타데이터 (File) & 프로필 사진 (BasicInformationFile)
        File profilePic = File.builder()
                .type(FileType.USER_PROFILE_IMAGE)
                .originalFileName("https://velog.velcdn.com/images/sdb016/post/47181c7c-1156-4182-a638-e0ad0b03a3d3/test.png")
                .savedFileName("https://velog.velcdn.com/images/sdb016/post/47181c7c-1156-4182-a638-e0ad0b03a3d3/test.png")
                .fileExt("png")
                .isDeleted(false)
                .build();
        File boardPic = File.builder()
                .type(FileType.ROOMMATE_BOARD_IMAGE)
                .originalFileName("https://velog.velcdn.com/images/sdb016/post/47181c7c-1156-4182-a638-e0ad0b03a3d3/test.png")
                .savedFileName("https://velog.velcdn.com/images/sdb016/post/47181c7c-1156-4182-a638-e0ad0b03a3d3/test.png")
                .fileExt("png")
                .isDeleted(false)
                .build();
        File roomTypePic = File.builder()
                .type(FileType.ETC)
                .originalFileName("https://velog.velcdn.com/images/sdb016/post/47181c7c-1156-4182-a638-e0ad0b03a3d3/test.png")
                .savedFileName("https://velog.velcdn.com/images/sdb016/post/47181c7c-1156-4182-a638-e0ad0b03a3d3/test.png")
                .fileExt("png")
                .isDeleted(false)
                .build();
        File extraOptionPic = File.builder()
                .type(FileType.ETC)
                .originalFileName("https://velog.velcdn.com/images/sdb016/post/47181c7c-1156-4182-a638-e0ad0b03a3d3/test")
                .savedFileName("https://velog.velcdn.com/images/sdb016/post/47181c7c-1156-4182-a638-e0ad0b03a3d3/test.png")
                .fileExt("png")
                .isDeleted(false)
                .build();
        File lifePatternPic = File.builder()
                .type(FileType.ETC)
                .originalFileName("https://velog.velcdn.com/images/sdb016/post/47181c7c-1156-4182-a638-e0ad0b03a3d3/test.png")
                .savedFileName("https://velog.velcdn.com/images/sdb016/post/47181c7c-1156-4182-a638-e0ad0b03a3d3/test.png")
                .fileExt("png")
                .isDeleted(false)
                .build();
        File chatRoomPic = File.builder()
                .type(FileType.CHAT_ROOM_IMAGE)
                .originalFileName("https://velog.velcdn.com/images/sdb016/post/47181c7c-1156-4182-a638-e0ad0b03a3d3/test.png")
                .savedFileName("https://velog.velcdn.com/images/sdb016/post/47181c7c-1156-4182-a638-e0ad0b03a3d3/test.png")
                .fileExt("png")
                .isDeleted(false)
                .build();
        fileRepository.saveAll(List.of(profilePic, boardPic, roomTypePic, extraOptionPic, lifePatternPic, chatRoomPic));

        basicInformationFileRepository.save(BasicInformationFile.builder().basicInformation(user1Info).file(profilePic).build());

        roomTypeFileRepository.save(RoomTypeFile.builder().roomType(oneRoom).file(roomTypePic).build());
        roomExtraOptionFileRepository.save(RoomExtraOptionFile.builder().roomExtraOption(fullOption).file(extraOptionPic).build());
        lifePatternFileRepository.save(LifePatternFile.builder().lifePattern(bedtime).file(lifePatternPic).build());

        // 10. 방 프로필 (RoomOfferProfile, RoomSeekerProfile, OfferRoomType, SeekerRoomType, SeekerProfileRegion)
        RoomOfferProfile offerProfile = RoomOfferProfile.builder()
                .member(user1)
                .region(seoul)
                .deposit(500)
                .monthlyRent(50)
                .comeableAt(LocalDateTime.now().plusDays(7))
                .isComeableAtNegotiable(true)
                .build();
        RoomSeekerProfile seekerProfile = RoomSeekerProfile.builder()
                .member(user2)
                .minDeposit(100)
                .maxDeposit(1000)
                .minMonthlyRent(30)
                .maxMonthlyRent(80)
                .comeableAt(LocalDateTime.now().plusDays(7))
                .isComeableAtNegotiable(true)
                .build();
        roomOfferProfileRepository.save(offerProfile);
        roomSeekerProfileRepository.save(seekerProfile);

        offerRoomTypeRepository.save(OfferRoomType.builder().roomOfferProfile(offerProfile).roomType(oneRoom).build());
        seekerRoomTypeRepository.save(SeekerRoomType.builder().roomSeekerProfile(seekerProfile).roomType(oneRoom).build());
        roomSeekerProfileRegionRepository.save(RoomSeekerProfileRegion.builder().roomSeekerProfile(seekerProfile).region(seoul).build());

        // 11. 인기 검색어 덤프 (Search)
        Search search1 = Search.builder().member(user1).keyword("강남역").build();
        Search search2 = Search.builder().member(user1).keyword("원룸").build();
        Search search3 = Search.builder().member(user2).keyword("강남역").build();
        Search search4 = Search.builder().member(user2).keyword("판교").build();
        Search search5 = Search.builder().member(user2).keyword("역삼동").build();
        searchRepository.saveAll(List.of(search1, search2, search3, search4, search5));

        // 12. 룸메이트 게시글, 옵션 및 첨부파일 (RoommateBoard, Option, File, Interest, Declaration)
        Region firstDong = level3Towns.isEmpty() ? seoul : level3Towns.get(0);
        RoommateBoard board1 = RoommateBoard.builder()
                .member(user1)
                .title("강남역 인근 깔끔한 원룸 룸메 구합니다!")
                .contents("채광 좋고 조용한 원룸입니다. 함께 깨끗하게 쓰실 분 환영해요.")
                .deposit(500)
                .monthlyRent(50)
                .managementCost(5)
                .roomType(oneRoom)
                .region(firstDong)
                .comeableDateNegotiable(true)
                .comeableDate(LocalDateTime.now().plusDays(7))
                .isDeleted(false)
                .hits(10L)
                .build();
        roommateBoardRepository.save(board1);

        RoommateBoardOption boardOption1 = RoommateBoardOption.builder()
                .roommateBoard(board1)
                .roomExtraOption(fullOption)
                .build();
        RoommateBoardOption boardOption2 = RoommateBoardOption.builder()
                .roommateBoard(board1)
                .roomExtraOption(elevator)
                .build();
        roommateBoardOptionRepository.saveAll(List.of(boardOption1, boardOption2));

        roommateBoardFileRepository.save(RoommateBoardFile.builder().roommateBoard(board1).file(boardPic).isThumbnail(true).build());
        roommateBoardInterestRepository.save(RoommateBoardInterest.builder().member(user2).roommateBoard(board1).isDeleted(false).build());
        roommateBoardDeclarationRepository.save(RoommateBoardDeclaration.builder().member(user2).roommateBoard(board1).reason("허위 매물 등록 의심").declarationType(DeclarationType.PENDING).build());

        // 12-1. 추가 테스트용
        RoommateBoard board2 = RoommateBoard.builder()
                .member(user1)
                .title("강남역 인근 깔끔한 원룸 룸메 구합니다! (테스트용, member.id 변경)")
                .contents("채광 좋고 조용한 원룸입니다. 함께 깨끗하게 쓰실 분 환영해요.")
                .deposit(500)
                .monthlyRent(50)
                .managementCost(5)
                .roomType(oneRoom)
                .region(firstDong)
                .comeableDateNegotiable(true)
                .comeableDate(LocalDateTime.now().plusDays(7))
                .isDeleted(false)
                .hits(10L)
                .build();
        roommateBoardRepository.save(board2);

        RoommateBoardOption boardOption3 = RoommateBoardOption.builder()
                .roommateBoard(board2)
                .roomExtraOption(fullOption)
                .build();
        RoommateBoardOption boardOption4 = RoommateBoardOption.builder()
                .roommateBoard(board2)
                .roomExtraOption(elevator)
                .build();

        roommateBoardOptionRepository.saveAll(List.of(boardOption3, boardOption4));
        roommateBoardDeclarationRepository.save(RoommateBoardDeclaration.builder().member(user2).roommateBoard(board2).reason("허위 매물 등록 의심 테스트").declarationType(DeclarationType.PENDING).build());

        // 13. 1:1 문의 및 답변 (Inquiry & InquiryComment)
        Inquiry inquiry1 = Inquiry.builder()
                .member(user1)
                .inquiryCategory(catAccount)
                .title("신원 인증 처리 기간이 궁금합니다.")
                .contents("학생 인증 신청을 했는데 승인까지 보통 얼마나 걸리나요?")
                .isDeleted(false)
                .build();
        inquiryRepository.save(inquiry1);

        InquiryComment comment1 = InquiryComment.builder()
                .member(adminMember)
                .inquiry(inquiry1)
                .contents("안녕하세요 김노크님! 신원 인증은 제출 후 영업일 기준 1~2일 이내에 검토 완료됩니다.")
                .isDeleted(false)
                .build();
        inquiryCommentRepository.save(comment1);

        // 14. 채팅 신청, 채팅방, 채팅 멤버 및 채팅 메세지, 채팅방 이미지 (ChattingRequired, ChattingRoom, Member, Message, ChatRoomFile)
        ChattingRequired chatReq = ChattingRequired.builder()
                .requester(user2)
                .requestee(user1)
                .roommateBoard(board1)
                .status(ChattingRequiredStatus.ACCEPTED)
                .build();
        chattingRequiredRepository.save(chatReq);

        ChattingRoom chatRoom = ChattingRoom.builder()
                .chattingRequired(chatReq)
                .build();
        chattingRoomRepository.save(chatRoom);

        ChatRoomMember mem1 = ChatRoomMember.of(chatRoom, user1);
        ChatRoomMember mem2 = ChatRoomMember.of(chatRoom, user2);
        chatRoomMemberRepository.saveAll(List.of(mem1, mem2));

        ChatRoomMessage msg1 = ChatRoomMessage.builder()
                .chattingRoom(chatRoom)
                .member(user2)
                .contents("안녕하세요! 강남역 게시글 보고 문의드립니다.")
                .type(MessageType.TEXT)
                .isRead(true)
                .build();
        ChatRoomMessage msg2 = ChatRoomMessage.builder()
                .chattingRoom(chatRoom)
                .member(user1)
                .contents("네 안녕하세요! 언제 보러 오시겠어요?")
                .type(MessageType.TEXT)
                .isRead(false)
                .build();
        chatRoomMessageRepository.saveAll(List.of(msg1, msg2));

        // 15. 이력 로그 및 궁합 점수 (MemberLifePatternLog, PreferenceConditionLog, WeightLog)
        PreferenceConditionLogDegree preferenceConditionLogDegree = preferenceConditionLogDegreeRepository.save(PreferenceConditionLogDegree.builder().degree(1L).build());
        MemberLifePatternLogDegree memberLifePatternLogDegree = memberLifePatternLogDegreeRepository.save(MemberLifePatternLogDegree.builder().degree(1L).build());
        PreferenceConditionWeightLogDegree preferenceConditionWeightLogDegree = preferenceConditionWeightLogDegreeRepository.save(PreferenceConditionWeightLogDegree.builder().degree(1L).build());
        MemberLifePatternLog mlpLog = memberLifePatternLogRepository.save(MemberLifePatternLog.builder().member(user1).lifePatternInformation(lifePatternInformationList.get(0)).memberLifePatternLogDegree(memberLifePatternLogDegree).build());
        PreferenceConditionLog pcLog = preferenceConditionLogRepository.save(PreferenceConditionLog.builder().member(user1).lifePatternInformation(lifePatternInformationList.get(0)).preferenceConditionLogDegree(preferenceConditionLogDegree).build());
        PreferenceConditionWeightLog pcwLog = preferenceConditionWeightLogRepository.save(PreferenceConditionWeightLog.builder().member(user1).lifePattern(bedtime).preferenceConditionWeightLogDegree(preferenceConditionWeightLogDegree).build());

        chatRoomFileRepository.save(ChatRoomFile.builder().chatRoomMessage(msg1).file(chatRoomPic).build());
        ChattingScore chattingScore = chattingScoreRepository.save(ChattingScore.builder().chattingRequired(chatReq).memberLifePatternLogDegree(memberLifePatternLogDegree).preferenceConditionLogDegree(preferenceConditionLogDegree).preferenceConditionWeightLogDegree(preferenceConditionWeightLogDegree).score(90).build());

        // 16. 룸메이트 매칭 확정 및 내 룸메이트 (RoommateMatchingRequired, MyRoommate)
        RoommateMatchingRequired matchingReq = RoommateMatchingRequired.builder()
                .requester(user2)
                .requestee(user1)
                .chattingRoom(chatRoom)
                .status(RoommateRequiredStatus.ACCEPTED)
                .build();
        roommateMatchingRequiredRepository.save(matchingReq);

        MyRoommate myRoommate = MyRoommate.builder()
                .roommateMatchingRequired(matchingReq)
                .isDeleted(false)
                .build();
        myRoommateRepository.save(myRoommate);

        roommateScoreRepository.save(RoommateScore.builder()
                .myRoommate(myRoommate)
                .chattingScore(chattingScore)
                .build());

        // 17. 룸메이트 하우스 룰 (RoommateHouseRule)
        RoommateHouseRule rule1 = RoommateHouseRule.builder()
                .member(user1)
                .myRoommate(myRoommate)
                .title("청소 규칙")
                .contents("화장실 청소는 매주 토요일 교대로 진행합니다.")
                .isDeleted(false)
                .build();
        RoommateHouseRule rule2 = RoommateHouseRule.builder()
                .member(user1)
                .myRoommate(myRoommate)
                .title("소음 지정")
                .contents("밤 11시 이후 이어폰 착용 필수!")
                .isDeleted(false)
                .build();
        roommateHouseRuleRepository.saveAll(List.of(rule1, rule2));

        // 18. 룸메이트 캘린더 카테고리, 캘린더, 캘린더 참여자, 반복 및 예외 (CalendarCategory, Calendar, Member, Repeat, Exclude)
        RoommateCalendarCategory calCat = RoommateCalendarCategory.builder()
                .name("집안일")
                .build();
        roommateCalendarCategoryRepository.save(calCat);

        RoommateCalendar calendar = RoommateCalendar.builder()
                .myRoommate(myRoommate)
                .member(user1)
                .roommateCalendarCategory(calCat)
                .title("거실 및 주방 청소")
                .contents("주말 맞이 대청소")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusHours(2))
                .isDeleted(false)
                .build();
        roommateCalendarRepository.save(calendar);

        RoommateCalendarMember calMem1 = RoommateCalendarMember.of(calendar, user1);
        RoommateCalendarMember calMem2 = RoommateCalendarMember.of(calendar, user2);
        roommateCalendarMemberRepository.saveAll(List.of(calMem1, calMem2));

        RepeatRoommateCalendar repeatCal = repeatRoommateCalendarRepository.save(RepeatRoommateCalendar.builder()
                .roommateCalendar(calendar)
                .repeatType(RepeatType.WEEKLY)
                .endDate(LocalDateTime.now().plusMonths(3))
                .build());

        excludeRoommateCalendarRepository.save(ExcludeRoommateCalendar.builder()
                .repeatRoommateCalendar(repeatCal)
                .excludeAt(LocalDateTime.now().plusDays(7))
                .build());

        // 19. 알림 설정, 푸시 알림, 연관 알림 및 공지사항 (AlarmSetting, Alarm, Notification, NotificationAlarm, ChattingRequiredAlarm, MatchingRequiredAlarm, CalendarAlarm)
        AlarmSetting setting1 = AlarmSetting.builder()
                .member(user1)
                .alarmSettingType(AlarmSettingType.NOTIFICATION)
                .isEnabled(true)
                .build();
        alarmSettingRepository.save(setting1);

        Alarm alarm1 = Alarm.builder()
                .member(user1)
                .title("채팅 알림")
                .contents("이인인 님으로부터 새로운 메시지가 도착했습니다.")
                .expiredAt(LocalDateTime.now().plusDays(30))
                .isRead(false)
                .build();
        alarmRepository.save(alarm1);

        Notification notice1 = Notification.builder()
                .member(adminMember)
                .title("서비스 정기 점검 안내")
                .contents("안정적인 서비스 환경 조성을 위한 시스템 정기 점검이 수행됩니다.")
                .isDeleted(false)
                .build();
        notificationRepository.save(notice1);

        notificationAlarmRepository.save(NotificationAlarm.builder().alarm(alarm1).notification(notice1).build());
        chattingRequiredAlarmRepository.save(ChattingRequiredAlarm.builder().member(user1).title("채팅 신청 알림").expiredAt(LocalDateTime.now().plusDays(30)).chattingRequired(chatReq).build());
        roommateMatchingRequiredAlarmRepository.save(RoommateMatchingRequiredAlarm.builder().member(user1).title("매칭 확정 알림").expiredAt(LocalDateTime.now().plusDays(30)).roommateMatchingRequired(matchingReq).build());
        roommateCalendarAlarmRepository.save(RoommateCalendarAlarm.builder().member(user1).title("일정 알림").expiredAt(LocalDateTime.now().plusDays(30)).roommateCalendar(calendar).build());

        // 20. 결제, 포인트 및 포인트 거래이력 (Payment, Point, PointLog)
        Payment payment = Payment.builder()
                .member(user1)
                .paymentKey("payment_key_sample_123")
                .amount(10000L)
                .type(PaymentType.CARD)
                .status(PaymentStatus.PENDING)
                .build();
        paymentRepository.save(payment);

        Point point = Point.builder()
                .member(user1)
                .points(1000L)
                .build();
        pointRepository.save(point);

        PointLog pointLog = PointLog.builder()
                .member(user1)
                .points(1000L)
                .reason("신규 가입 리워드 포인트 지급")
                .variance(VarianceType.INCREASE)
                .build();
        pointLogRepository.save(pointLog);
    }

    private List<LifePatternInformation> createLifePatternInformation(
            LifePattern lifePattern,
            String... descriptions
    ) {
        List<LifePatternInformation> informationList = new ArrayList<>();
        for (int i = 0; i < descriptions.length; i++) {
            informationList.add(LifePatternInformation.builder()
                    .lifePattern(lifePattern)
                    .dvalue(String.valueOf(i + 1))
                    .description(descriptions[i])
                    .build());
        }
        return informationList;
    }
}
