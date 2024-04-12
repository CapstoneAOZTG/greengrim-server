package com.greengrim.green.core.hiding.challenge;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.ChallengeErrorCode;
import com.greengrim.green.common.exception.errorCode.HidingErrorCode;
import com.greengrim.green.core.challenge.repository.ChallengeRepository;
import com.greengrim.green.core.member.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeHidingService {

    private final ChallengeHidingRepository challengeHidingRepository;
    private final ChallengeRepository challengeRepository;

    @Transactional
    public void register(Long memberId, Long challengeId) {
        challengeHidingRepository.save(
                ChallengeHiding.builder()
                        .memberId(memberId)
                        .challengeId(challengeId)
                        .build());
    }

    @Transactional
    public void hideChallenge(Member member, Long challengeId) {
        checkChallengeValidation(challengeId);
        if (checkNonExistingHiding(member.getId(), challengeId)) {
            register(member.getId(), challengeId);
        } else {
            throw new BaseException(HidingErrorCode.ALREADY_HIDING);
        }
    }

    private void checkChallengeValidation(Long challengeId) {
        challengeRepository.findByIdAndStatusIsTrue(challengeId)
                .orElseThrow(() -> new BaseException(ChallengeErrorCode.EMPTY_CHALLENGE));
    }

    private boolean checkNonExistingHiding(Long memberId, Long challengeId) {
        return challengeHidingRepository.findByMemberIdAndChallengeId(memberId, challengeId).isEmpty();
    }
}
