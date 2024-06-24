package com.greengrim.green.core.hiding.certification;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.CertificationErrorCode;
import com.greengrim.green.common.exception.errorCode.HidingErrorCode;
import com.greengrim.green.core.certification.repository.CertificationRepository;
import com.greengrim.green.core.member.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificationHidingService {

    private final CertificationHidingRepository certificationHidingRepository;
    private final CertificationRepository certificationRepository;

    @Transactional
    public void register(Long memberId, Long certificationId) {
        certificationHidingRepository.save(
                CertificationHiding.builder()
                        .memberId(memberId)
                        .certificationId(certificationId)
                        .build());
    }

    @Transactional
    public void hideCertification(Member member, Long certificationId) {
        checkCertificationValidation(certificationId);
        if (checkNonExistingHiding(member.getId(), certificationId)) {
            register(member.getId(), certificationId);
        } else {
            throw new BaseException(HidingErrorCode.ALREADY_HIDING);
        }
    }

    private void checkCertificationValidation(Long certificationIdId) {
        certificationRepository.findByIdAndStatusIsTrue(certificationIdId)
                .orElseThrow(() -> new BaseException(CertificationErrorCode.EMPTY_CERTIFICATION));
    }

    private boolean checkNonExistingHiding(Long memberId, Long certificationIdId) {
        return certificationHidingRepository.findByMemberIdAndCertificationId(memberId, certificationIdId).isEmpty();
    }
}
