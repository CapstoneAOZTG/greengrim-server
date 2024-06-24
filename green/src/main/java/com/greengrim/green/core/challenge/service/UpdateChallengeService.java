package com.greengrim.green.core.challenge.service;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.ChallengeErrorCode;
import com.greengrim.green.core.certification.service.UpdateCertificationService;
import com.greengrim.green.core.challenge.Challenge;
import com.greengrim.green.core.challenge.dto.ChallengeRequestDto;
import com.greengrim.green.core.challenge.repository.ChallengeRepository;
import com.greengrim.green.core.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UpdateChallengeService {

    private static final int MAX_HEADCOUNT_FOR_DELETE = 1;  // 삭제를 위한 최대 참여 인원

    private final ChallengeRepository challengeRepository;
    private final UpdateCertificationService updateCertificationService;

    @Transactional
    public void modify(Member member, Long id, ChallengeRequestDto.ModifyChallenge modifyChallenge) {
        Challenge challenge = challengeRepository.findByIdAndStatusIsTrue(id)
                .orElseThrow(() -> new BaseException(ChallengeErrorCode.EMPTY_CHALLENGE));

        checkIsMine(challenge.getMember().getId(), member.getId());

        challenge.modify(modifyChallenge.getTitle(), modifyChallenge.getDescription(), modifyChallenge.getImgUrl());
    }

    @Transactional
    public void delete(Member member, Long id) {
        Challenge challenge = challengeRepository.findByIdAndStatusIsTrue(id)
                .orElseThrow(() -> new BaseException(ChallengeErrorCode.EMPTY_CHALLENGE));

        checkIsMine(challenge.getMember().getId(), member.getId());

        // 인원이 2명 이상이면 삭제 불가
        checkDeleteCondition(challenge);
        // 챌린지 soft delete
        challenge.setStatusFalse();
        // 그 챌린지의 인증들 soft delete
        deleteCertifications(challenge);
    }

    private void checkIsMine(Long ownerId, Long memberId) {
        if(!Objects.equals(ownerId, memberId)) {
            throw new BaseException(ChallengeErrorCode.NO_AUTHORIZATION);
        }
    }

    // 삭제가능한지 조건 확인
    private void checkDeleteCondition(Challenge challenge) {
        if(challenge.getHeadCount() > MAX_HEADCOUNT_FOR_DELETE) {
            throw new BaseException(ChallengeErrorCode.NOT_DELETE);
        }
    }

    // 인증들 삭제
    private void deleteCertifications(Challenge challenge) {
        updateCertificationService.setCertificationStatusFalseByChallenge(challenge);
    }
}
