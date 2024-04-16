package com.greengrim.green.core.hiding.challenge;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.ChallengeErrorCode;
import com.greengrim.green.common.exception.errorCode.HidingErrorCode;
import com.greengrim.green.core.challenge.Challenge;
import com.greengrim.green.core.challenge.repository.ChallengeRepository;
import com.greengrim.green.core.chatparticipant.ChatparticipantService;
import com.greengrim.green.core.chatroom.service.ChatroomService;
import com.greengrim.green.core.member.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeHidingService {

    private final ChallengeHidingRepository challengeHidingRepository;
    private final ChallengeRepository challengeRepository;
    private final ChatroomService chatroomService;
    private final ChatparticipantService chatparticipantService;

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
        Challenge challenge = checkChallengeValidation(challengeId);
        if (checkNonExistingHiding(member.getId(), challengeId)) {
            register(member.getId(), challengeId);
            // 챌린지 참여 중인데 차단했다면 채팅방 나가기 처리
            if(chatparticipantService.checkParticipantExists(member. getId(), challenge.getChatroom().getId())) {
                chatroomService.exitChatroom(member, challenge.getChatroom().getId());
            }
        } else {
            throw new BaseException(HidingErrorCode.ALREADY_HIDING);
        }
    }

    private Challenge checkChallengeValidation(Long challengeId) {
        return challengeRepository.findByIdAndStatusIsTrue(challengeId)
                .orElseThrow(() -> new BaseException(ChallengeErrorCode.EMPTY_CHALLENGE));
    }

    private boolean checkNonExistingHiding(Long memberId, Long challengeId) {
        return challengeHidingRepository.findByMemberIdAndChallengeId(memberId, challengeId).isEmpty();
    }
}
