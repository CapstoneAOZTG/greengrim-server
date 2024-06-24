package com.greengrim.green.core.challenge.service;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.ChallengeErrorCode;
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

    private final ChallengeRepository challengeRepository;

    @Transactional
    public void modify(Member member, Long id, ChallengeRequestDto.ModifyChallenge modifyChallenge) {
        Challenge challenge = challengeRepository.findByIdAndStatusIsTrue(id)
                .orElseThrow(() -> new BaseException(ChallengeErrorCode.EMPTY_CHALLENGE));

        checkMyChallenge(challenge.getMember().getId(), member.getId());

        challenge.modify(modifyChallenge.getTitle(), modifyChallenge.getDescription(), modifyChallenge.getImgUrl());
    }

    private void checkMyChallenge(Long ownerId, Long memberId) {
        if(!Objects.equals(ownerId, memberId)) {
            throw new BaseException(ChallengeErrorCode.NO_AUTHORIZATION);
        }
    }
}
