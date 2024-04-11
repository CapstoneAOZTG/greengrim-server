package com.greengrim.green.core.challenge.service;

import static com.greengrim.green.common.entity.Time.calculateTime;
import static com.greengrim.green.common.util.UtilService.getPageable;

import com.greengrim.green.common.entity.SortOption;
import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.ChallengeErrorCode;
import com.greengrim.green.core.certification.service.GetCertificationService;
import com.greengrim.green.core.challenge.Category;
import com.greengrim.green.core.challenge.Challenge;
import com.greengrim.green.core.challenge.HotChallengeOption;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.ChallengeDetailInfo;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.ChallengeInfo;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.ChallengeSimpleInfo;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.HomeChallenges;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.MyChatroom;
import com.greengrim.green.core.challenge.repository.ChallengeRepository;
import com.greengrim.green.core.chatparticipant.Chatparticipant;
import com.greengrim.green.core.chatparticipant.ChatparticipantService;
import com.greengrim.green.core.member.Member;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetChallengeService {

    private final GetCertificationService getCertificationService;
    private final ChallengeRepository challengeRepository;
    private final ChatparticipantService chatparticipantService;

    /**
     * 챌린지 상세 조회
     * TODO: @param member 를 이용해 차단 목록에 있다면 보여주지 않기
     */
    public ChallengeDetailInfo getChallengeDetail(Member member, Long id) {
        Challenge challenge = findByIdWithValidation(id);
        boolean isMine = checkIsMine(member.getId(), challenge.getMember().getId());
        return new ChallengeDetailInfo(challenge, chatparticipantService.checkParticipantExists(member.getId(),
            challenge.getChatroom().getId()), isMine);
    }

    /**
     * 카테고리 별 챌린지 조회
     * TODO: @param member 를 이용해 차단 목록에 있다면 보여주지 않기
     */
    public PageResponseDto<List<ChallengeSimpleInfo>> getChallengesByCategory(
            Member member, Category category, int page, int size, SortOption sort) {
        Page<Challenge> challenges = challengeRepository.findByCategoryAndStateIsTrue(
                category, getPageable(page, size, sort));

        return makeChallengesSimpleInfoList(challenges);
    }

    /**
     * 내가 만든 챌린지 조회
     */
    public PageResponseDto<List<ChallengeSimpleInfo>> getMyChallenges(
        Member member, int page, int size, SortOption sort) {
        Page<Challenge> challenges = challengeRepository.findByMemberAndStateIsTrue(
                member, getPageable(page, size, sort));
            return makeChallengesSimpleInfoList(challenges);
    }


    /**
     * 멤버 별 참여중인 챌린지 조회
     * TODO: @param member 를 이용해 차단 목록에 있다면 보여주지 않기
     */
    public PageResponseDto<List<ChallengeSimpleInfo>> getChallengesByMemberId(
            Long memberId, Member member, int page, int size, SortOption sort) {
        // 만약 조회할 memberId가 넘어오지 않았다면 자신의 것 조회!
        if(memberId == null) {
            memberId = member.getId();
        }
        Page<Challenge> challenges = challengeRepository.findByMemberIdAndStateIsTrue(
                memberId, getPageable(page, size, sort));
        return makeChallengesSimpleInfoList(challenges);
    }

    public Challenge findByIdWithValidation(Long id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new BaseException(ChallengeErrorCode.EMPTY_CHALLENGE));
    }

    private boolean checkIsMine(Long memberId, Long ownerId) {
        return Objects.equals(memberId, ownerId);
    }

    /**
     * 홈 화면 핫 챌린지 조회
     * TODO: @param member 를 이용해 차단 목록에 있다면 보여주지 않기
     */
    public HomeChallenges getHotChallenges(Member member) {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Challenge> challenges;
        List<ChallengeInfo> challengeInfoList = new ArrayList<>();
        // 1번 최근에 신설된
        challenges = challengeRepository.findAllAndStatusIsTrueDesc(pageable);
        challenges.forEach(challenge -> challengeInfoList.add(
                new ChallengeInfo(challenge,
                        calculateTime(challenge.getCreatedAt(), 3) + HotChallengeOption.MOST_RECENT.getSubTitle())));
        // 2번 참여 인원이 가장 많은
        challenges = challengeRepository.findHotChallengesByHeadCount(pageable);
        challenges.forEach(challenge -> challengeInfoList.add(
                new ChallengeInfo(challenge,
                        challenge.getHeadCount() + HotChallengeOption.MOST_HEADCOUNT.getSubTitle())));
        //3번 일주일 내 인증이 가장 많은
        challenges = challengeRepository.findMostCertifiedChallengesWithinAWeek(LocalDateTime.now().minusWeeks(1),
                pageable);
        challenges.forEach(challenge -> challengeInfoList.add(
                new ChallengeInfo(challenge, HotChallengeOption.MOST_CERTIFICATION.getSubTitle())));
        return new HomeChallenges(challengeInfoList);
    }

    /**
     * 핫 챌린지 더보기
     * TODO: @param member 를 이용해 차단 목록에 있다면 보여주지 않기
     */
    public PageResponseDto<List<ChallengeSimpleInfo>> getMoreHotChallenges(Member member, HotChallengeOption option,
                                                                           int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Challenge> challenges = null;
        switch (option) {
            case MOST_RECENT -> challenges = challengeRepository.findAllAndStatusIsTrueDesc(pageable);
            case MOST_CERTIFICATION -> challenges = challengeRepository.findMostCertifiedChallengesWithinAWeek(
                    LocalDateTime.now().minusWeeks(1), pageable);
            case MOST_HEADCOUNT -> challenges = challengeRepository.findHotChallengesByHeadCount(pageable);
        }
        return makeChallengesSimpleInfoList(challenges);
    }

    private PageResponseDto<List<ChallengeSimpleInfo>> makeChallengesSimpleInfoList(Page<Challenge> challenges) {
        List<ChallengeSimpleInfo> challengeSimpleInfoList = new ArrayList<>();
        challenges.forEach(challenge ->
                challengeSimpleInfoList.add(new ChallengeSimpleInfo(challenge)));

        return new PageResponseDto<>(challenges.getNumber(), challenges.hasNext(), challengeSimpleInfoList);
    }

    // makeChallengesSimpleInfoList 함수 Template 적용 버전
    // 사용 예시: makeChallengesList(challenges, HotChallengeInfo::new);
    private <T> PageResponseDto<List<T>> makeChallengesList(Page<Challenge> challenges, Function<Challenge, T> mapper) {
        List<T> challengeList = new ArrayList<>();
        challenges.forEach(challenge -> challengeList.add(mapper.apply(challenge)));

        return new PageResponseDto<>(challenges.getNumber(), challenges.hasNext(), challengeList);
    }

    /**
     * 내가 참가중인 챌린지(채팅방) 조회
     */
    public List<MyChatroom> getMyChatrooms(Member member) {
        List<MyChatroom> myChatrooms = new ArrayList<>();

        List<Chatparticipant> chatparticipants = chatparticipantService.findByMemberId(member.getId());
        chatparticipants.forEach(chatparticipant -> {
            Long chatroomId = chatparticipant.getChatroom().getId();
            Challenge challenge = challengeRepository.findByChatroomId(chatroomId);

            Duration duration = Duration.between(challenge.getCreatedAt(), LocalDateTime.now());
            long days = duration.toDays();

            String afterDay;
            if (days == 0) afterDay = "오늘";
            else afterDay = days + "일 전";
            myChatrooms.add(new MyChatroom(challenge, afterDay));
        });
        return myChatrooms;
    }

    /**
     * 키워드로 챌린지 검색하기
     * TODO: 차단 구현 후 member 이용해서 차단한 리소스 걸러내기
     */
    public PageResponseDto<List<ChallengeSimpleInfo>> searchChallenges(
            Member member, Category category, int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Challenge> challenges = challengeRepository.searchChallenges(keyword, category, pageable);
        return makeChallengesSimpleInfoList(challenges);
    }
}
