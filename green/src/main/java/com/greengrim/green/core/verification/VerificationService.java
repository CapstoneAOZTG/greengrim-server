package com.greengrim.green.core.verification;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.CertificationErrorCode;
import com.greengrim.green.common.exception.errorCode.VerificationErrorCode;
import com.greengrim.green.core.certification.Certification;
import com.greengrim.green.core.certification.repository.CertificationRepository;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.member.repository.MemberRepository;
import com.greengrim.green.core.verification.dto.VerificationRequest.RegisterVerification;
import jakarta.transaction.Transactional;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class VerificationService {

    private final CertificationRepository certificationRepository;
    private final VerificationRepository verificationRepository;
    private final MemberRepository memberRepository;

    public void register(Member member, RegisterVerification registerVerification) {
        Certification certification = certificationRepository.findById(registerVerification.getCertificationId())
                .orElseThrow(() -> new BaseException(CertificationErrorCode.EMPTY_CHALLENGE));

        // 예외 검사
        validateDuplicationAndSelfVerification(member, certification);

        // 생성 및 저장
        Verification verification = Verification.builder()
                .memberId(member.getId())
                .certificationId(registerVerification.getCertificationId())
                .response(registerVerification.isResponse())
                .build();
        verificationRepository.save(verification);

        // 상호 검증 처리하기
        verifyCertification(certification);
    }

    /**
     * 상호 검증이 과반수에 도달했을 때 처리하는 함수
     */
    public void verifyCertification(Certification certification) {
        // 이 인증에 거짓이라고 답한 사람이 5명 이상이라면, 해당 인증은 실패한 인증이다.
        // (상호 검증 중단, 인증 비활성화 처리, 포인트 뺏기, 탄소 감소량 뺏기)
        if (checkCertificationFailOrSuccess(certification.getId(), false)) {
            // 인증 entity 상호 검증 실패 처리
            certification.setValidationFail();
            Member lier = certification.getMember();
            // 포인트 뺏기
            lier.minusPoint(certification.getChallenge().getCategory().getPoint());
            // 탄소 감소량 뺏기
            lier.setCarbonReduction(-certification.getChallenge().getCategory().getCarbonReduction());
            memberRepository.save(lier);
        } // 진실이라고 답한 사람이 5명 이상이라면, 해당 인증은 성공한 인증이다. (상호 검증 중단)
        else if (checkCertificationFailOrSuccess(certification.getId(), true)) {
            certification.setValidationSuccess();
        }
        // 인증 entity 에서 남은 상호 검증 횟수 1 줄이기
        certification.minusVerificationCount();
    }

    /**
     * 상호 검증에 참여했는지 확인하는 함수
     */
    public boolean checkVerification(Long memberId, Long certificationId) {
        return verificationRepository.findByMemberIdAndCertificationId(
                memberId, certificationId).isPresent();
    }

    /**
     * 상호 검증이 과반수에 도달했는지 확인하는 함수
     */
    private boolean checkCertificationFailOrSuccess(Long certificationId, boolean response) {
        return verificationRepository.countByCertificationIdAndResponse(
                certificationId, response) >= 5;
    }

    /**
     * 자신의 인증에 대해서 상호 검증하려는지 확인하는 함수 만약 같다면 true return
     */
    private boolean checkSelfVerification(Long memberId, Long certificationOwnerId) {
        return Objects.equals(memberId, certificationOwnerId);
    }

    private void validateDuplicationAndSelfVerification(Member member, Certification certification) {
        if (checkVerification(member.getId(), certification.getId())) {
            throw new BaseException(VerificationErrorCode.EXIST_VERIFICATION);
        }

        if (checkSelfVerification(member.getId(), certification.getMember().getId())) {
            throw new BaseException(VerificationErrorCode.SELF_VERIFICATION);
        }
    }
}
