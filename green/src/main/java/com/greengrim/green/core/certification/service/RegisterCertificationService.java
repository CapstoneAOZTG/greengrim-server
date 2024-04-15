package com.greengrim.green.core.certification.service;

import com.greengrim.green.common.fcm.FcmService;
import com.greengrim.green.core.certification.Certification;
import com.greengrim.green.core.certification.dto.CertificationRequestDto.RegisterCertification;
import com.greengrim.green.core.certification.dto.CertificationResponseDto.registerCertificationResponse;
import com.greengrim.green.core.certification.repository.CertificationRepository;
import com.greengrim.green.core.challenge.Challenge;
import com.greengrim.green.core.challenge.service.GetChallengeService;
import com.greengrim.green.core.history.HistoryOption;
import com.greengrim.green.core.history.HistoryService;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterCertificationService {

    private final GetChallengeService getChallengeService;
    private final HistoryService historyService;
    private final FcmService fcmService;
    private final MemberRepository memberRepository;
    private final CertificationRepository certificationRepository;

    private static final String FCM_MESSAGE_CERTIFICATION = "인증 활동";
    private static final String FCM_MESSAGE_SUCCESS_CHALLENGE = "챌린지 성공";

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
        successCertification(member, challenge);

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
    public void successCertification(Member member, Challenge challenge) {
        int point = challenge.getCategory().getPoint();
        // 포인트 추가
        member.plusPoint(point);
        // 탄소 저감량 추가
        member.setCarbonReduction(challenge.getCategory().getCarbonReduction());
        memberRepository.save(member);
        // history 저장
        historyService.save(member.getId(), challenge.getId(), challenge.getTitle(),
                challenge.getImgUrl(), HistoryOption.CERTIFICATION, point, member.getPoint());
        // FCM 전송
        sendFcm(member, challenge.getCategory().getName(), FCM_MESSAGE_CERTIFICATION, challenge.getCategory().getPoint());
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
        String title = challenge.getTitle() + " " + FCM_MESSAGE_SUCCESS_CHALLENGE;
        historyService.save(member.getId(), challenge.getId(), title,
                challenge.getImgUrl(), HistoryOption.CHALLENGE_SUCCESS, successPoint, member.getPoint());
        // FCM 전송
        sendFcm(member, challenge.getCategory().getName(), FCM_MESSAGE_SUCCESS_CHALLENGE, successPoint);
    }

    /**
     * 챌린지 성공 여부 반환
     */
    public boolean checkSuccessChallenge(int goalCount, int round) {
        return round == goalCount;
    }

    public void sendFcm(Member member, String name, String type, int point) {
        String fcmString = name + " " + type;
        fcmService.sendGetPoint(member, fcmString, point);
    }

    public int calculateSuccessPoint(int goalCount, int point) {
        return goalCount / 2 * point;
    }

}
