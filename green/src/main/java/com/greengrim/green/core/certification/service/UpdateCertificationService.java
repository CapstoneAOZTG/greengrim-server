package com.greengrim.green.core.certification.service;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.CertificationErrorCode;
import com.greengrim.green.common.s3.S3Service;
import com.greengrim.green.core.certification.Certification;
import com.greengrim.green.core.certification.repository.CertificationRepository;
import com.greengrim.green.core.member.Member;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCertificationService {

    private final S3Service s3Service;
    private final CertificationRepository certificationRepository;

    public void delete(Member member, Long id) {
        Certification certification = certificationRepository.findById(id)
                .orElseThrow(() -> new BaseException(CertificationErrorCode.EMPTY_CERTIFICATION));
        // 내꺼인지 확인
        checkIsMine(member.getId(), certification.getMember().getId());
        // s3에서 인증 사진 삭제
        s3Service.deleteFile(certification.getImgUrl());
        // db에서 삭제
        certificationRepository.delete(certification);
    }

    private void checkIsMine(Long viewerId, Long ownerId) {
        if(!Objects.equals(viewerId, ownerId)) {
            throw new BaseException(CertificationErrorCode.NO_AUTHORIZATION);
        }
    }

}
