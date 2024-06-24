package com.greengrim.green.core.certification.service;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.CertificationErrorCode;
import com.greengrim.green.core.certification.Certification;
import com.greengrim.green.core.certification.repository.CertificationRepository;
import com.greengrim.green.core.challenge.Challenge;
import com.greengrim.green.core.member.Member;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateCertificationService {

    private final CertificationRepository certificationRepository;

    public void delete(Member member, Long id) {
        Certification certification = certificationRepository.findByIdAndStatusIsTrue(id)
                .orElseThrow(() -> new BaseException(CertificationErrorCode.EMPTY_CERTIFICATION));
        // 내꺼인지 확인
        checkIsMine(member.getId(), certification.getMember().getId());
        // 임시 삭제 처리
        certification.setStatusFalse();
    }

    /**
     * Member를 넘겨 받아 그 Member의 모든 인증을 soft delete
     */
    public void setCertificationStatusFalseByMember(Member member) {
        List<Certification> certifications = certificationRepository.findByMember(member);
        for (Certification c : certifications) {
            c.setStatusFalse();
        }
    }

    /**
     * Challenge를 넘겨 받아 그 Challenge의 모든 인증을 soft delete
     */
    public void setCertificationStatusFalseByChallenge(Challenge challenge) {
        List<Certification> certifications = certificationRepository.findByChallenge(challenge);
        for (Certification c : certifications) {
            c.setStatusFalse();
        }
    }

    private void checkIsMine(Long viewerId, Long ownerId) {
        if(!Objects.equals(viewerId, ownerId)) {
            throw new BaseException(CertificationErrorCode.NO_AUTHORIZATION);
        }
    }

}
