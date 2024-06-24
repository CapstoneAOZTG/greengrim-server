package com.greengrim.green.core.certification.service;

import com.greengrim.green.common.fcm.FcmService;
import com.greengrim.green.core.alarm.entity.AlarmType;
import com.greengrim.green.core.alarm.service.RegisterAlarmService;
import com.greengrim.green.core.certification.entity.Certification;
import com.greengrim.green.core.certification.dto.CertificationRequestDto.RegisterCertification;
import com.greengrim.green.core.certification.dto.CertificationResponseDto.registerCertificationResponse;
import com.greengrim.green.core.certification.repository.CertificationRepository;
import com.greengrim.green.core.challenge.entity.Challenge;
import com.greengrim.green.core.challenge.service.GetChallengeService;
import com.greengrim.green.core.history.entity.HistoryOption;
import com.greengrim.green.core.history.service.RegisterHistoryService;
import com.greengrim.green.core.member.entity.Member;
import com.greengrim.green.core.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterCertificationService {

    private final GetChallengeService getChallengeService;
    private final RegisterHistoryService registerHistoryService;
    private final FcmService fcmService;
    private final RegisterAlarmService registerAlarmService;
    private final MemberRepository memberRepository;
    private final CertificationRepository certificationRepository;

    public registerCertificationResponse register(Member member, RegisterCertification registerCertification) {
        Challenge challenge = getChallengeService.findByIdWithValidation(
                registerCertification.getChallengeId());

        int round = certificationRepository.countsByMemberAndChallenge(member, challenge) + 1;
        Certification certification = Certification.builder()
                .imgUrl(registerCertification.getImgUrl())
                .description(registerCertification.getDescription())
                .round(round)
                .validation(0)          // 상호 인증 성공 여부, 0=진행중
                .verificationCount(10)  // 남은 상호 인증 횟수
                .member(member)
                .challenge(challenge)
                .status(true)
                .reportCount(0)
                .build();

        certificationRepository.save(certification);

        // 인증 성공
        successCertification(member, challenge, certification.getId(), round);

        // 챌린지 성공
        boolean successChallenge = checkSuccessChallenge(challenge.getGoalCount(), round);
        if(successChallenge) {
            successChallenge(member, challenge);
        }
        return new registerCertificationResponse(certification, successChallenge);
    }

    /**
     * 인증 성공, 포인트와 탄소 저감량 제공 및 FCM 전송
     */
    public void successCertification(Member member, Challenge challenge, Long certificationId, int round) {
        int point = challenge.getCategory().getPoint();
        // 포인트 추가
        member.plusPoint(point);
        // 탄소 저감량 추가
        member.setCarbonReduction(challenge.getCategory().getCarbonReduction());
        memberRepository.save(member);
        // history 저장
        registerHistoryService.register(member.getId(), challenge.getId(), challenge.getTitle(),
                challenge.getImgUrl(), HistoryOption.CERTIFICATION, point, member.getPoint());

        String certificationTitle = round + "회차 인증";
        // FCM 전송
        fcmService.sendGetCertificationPoint(member, certificationId, certificationTitle);
        // 알람 저장
        registerAlarmService.register(member, AlarmType.POINT_CERTIFICATION, certificationId, challenge.getImgUrl(), certificationTitle, null);
    }

    /**
     * 챌린지 성공, 포인트 제공 및 FCM 전송
     */
    public void successChallenge(Member member, Challenge challenge) {
        // 챌린지 성공 포인트 계산
        int successPoint = calculateSuccessPoint(challenge.getGoalCount(), challenge.getCategory().getPoint());
        // 포인트 추가
        member.plusPoint(successPoint);
        memberRepository.save(member);
        // history 저장
        String title = challenge.getTitle() + " 챌린지 성공";
        registerHistoryService.register(member.getId(), challenge.getId(), title,
                challenge.getImgUrl(), HistoryOption.CHALLENGE_SUCCESS, successPoint, member.getPoint());
        // FCM 전송
        fcmService.sendSuccessChallenge(member, challenge.getId(), challenge.getTitle());
        // 알람 저장
        registerAlarmService.register(member, AlarmType.CHALLENGE_SUCCESS, challenge.getId(), challenge.getImgUrl(), challenge.getTitle(), null);
    }

    /**
     * 챌린지 성공 여부 반환
     */
    public boolean checkSuccessChallenge(int goalCount, int round) {
        return round == goalCount;
    }

    public int calculateSuccessPoint(int goalCount, int point) {
        return goalCount / 2 * point;
    }

}
