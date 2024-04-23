package com.greengrim.green.core.challenge.service;

import static com.greengrim.green.common.entity.Time.calculateTime;
import static com.greengrim.green.common.util.UtilService.getPageable;

import com.greengrim.green.common.entity.SortOption;
import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.ChallengeErrorCode;
import com.greengrim.green.core.certification.repository.CertificationRepository;
import com.greengrim.green.core.challenge.Category;
import com.greengrim.green.core.challenge.Challenge;
import com.greengrim.green.core.challenge.HotChallengeOption;
import com.greengrim.green.core.challenge.dto.ChallengeRequestDto.MyChallengesRequest;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.ChallengeDetailInfo;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.ChallengeInfo;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.ChallengeSimpleInfo;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.ChatroomInfo;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.ChatroomTopBarInfo;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.HomeChallenges;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.MyChallengeInfo;
import com.greengrim.green.core.challenge.repository.ChallengeRepository;
import com.greengrim.green.core.chat.ChatMessage;
import com.greengrim.green.core.chat.repository.ChatRepository;
import com.greengrim.green.core.chatparticipant.ChatparticipantService;
import com.greengrim.green.core.member.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChatparticipantService chatparticipantService;
    private final CertificationRepository certificationRepository;
    private final ChatRepository chatRepository;

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
     */
    public PageResponseDto<List<ChallengeSimpleInfo>> getChallengesByCategory(
            Member member, Category category, int page, int size, SortOption sort) {
        Page<Challenge> challenges = challengeRepository.findByCategoryAndStateIsTrue(
                member.getId(), category, getPageable(page, size, sort));

        return makeChallengesSimpleInfoList(challenges);
    }

    /**
     * 멤버 별 참여중인 챌린지 조회
     * TODO: @param member 를 이용해 차단 목록에 있다면 보여주지 않기
     */
    public PageResponseDto<List<ChallengeSimpleInfo>> getChallengesByMemberId(
            Long targetId, Member member, int page, int size, SortOption sort) {
        // 만약 조회할 memberId가 넘어오지 않았다면 자신의 것 조회!
        if(targetId == null) {
            targetId = member.getId();
        }
        Page<Challenge> challenges = challengeRepository.findByMemberIdAndStateIsTrue(
                member.getId(), targetId, getPageable(page, size, sort));
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
     */
    public HomeChallenges getHotChallenges(Member member) {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Challenge> challenges;
        List<ChallengeInfo> challengeInfoList = new ArrayList<>();
        // 1번 최근에 신설된
        challenges = challengeRepository.findAllAndStatusIsTrueDesc(member.getId(), pageable);
        challenges.forEach(challenge -> challengeInfoList.add(
                new ChallengeInfo(challenge,
                        calculateTime(challenge.getCreatedAt(), 3) + HotChallengeOption.MOST_RECENT.getSubTitle())));
        // 2번 참여 인원이 가장 많은
        challenges = challengeRepository.findHotChallengesByHeadCount(member.getId(), pageable);
        challenges.forEach(challenge -> challengeInfoList.add(
                new ChallengeInfo(challenge,
                        challenge.getHeadCount() + HotChallengeOption.MOST_HEADCOUNT.getSubTitle())));
        //3번 일주일 내 인증이 가장 많은
        challenges = challengeRepository.findMostCertifiedChallengesWithinAWeek(
                member.getId(),
                LocalDateTime.now().minusWeeks(1),
                pageable);
        challenges.forEach(challenge -> challengeInfoList.add(
                new ChallengeInfo(challenge, HotChallengeOption.MOST_CERTIFICATION.getSubTitle())));
        return new HomeChallenges(challengeInfoList);
    }

    /**
     * 핫 챌린지 더보기
     */
    public PageResponseDto<List<ChallengeSimpleInfo>> getMoreHotChallenges(Member member, HotChallengeOption option,
                                                                           int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Challenge> challenges = null;
        switch (option) {
            case MOST_RECENT -> challenges = challengeRepository.findAllAndStatusIsTrueDesc(member.getId(), pageable);
            case MOST_CERTIFICATION -> challenges = challengeRepository.findMostCertifiedChallengesWithinAWeek(
                    member.getId(), LocalDateTime.now().minusWeeks(1), pageable);
            case MOST_HEADCOUNT -> challenges = challengeRepository.findHotChallengesByHeadCount(member.getId(), pageable);
        }
        return makeChallengesSimpleInfoList(challenges);
    }

    private PageResponseDto<List<ChallengeSimpleInfo>> makeChallengesSimpleInfoList(Page<Challenge> challenges) {
        List<ChallengeSimpleInfo> challengeSimpleInfoList = new ArrayList<>();
        challenges.forEach(challenge ->
                challengeSimpleInfoList.add(new ChallengeSimpleInfo(challenge)));

        return new PageResponseDto<>(challenges.getNumber(), challenges.hasNext(), challengeSimpleInfoList);
    }

    /**
     * 내가 참가중인 챌린지(채팅방) 조회
     */
    public List<MyChallengeInfo> getMyChallenges(Member member, List<MyChallengesRequest> myChallengesRequests) {
        List<MyChallengeInfo> myChallengeInfos = new ArrayList<>();

        // 채팅방 정보가 없다면
        if(myChallengesRequests.isEmpty()) return myChallengeInfos;

        HashMap<Long, String> visitMap = makeHashMapFromRequest(myChallengesRequests);
        List<Challenge> myChallenges = challengeRepository.findByMember(member);
        for (Challenge challenge : myChallenges) {

            Long chatroomId = challenge.getChatroom().getId();

            Optional<ChatMessage> chatMessage = chatRepository.
                findFirstByRoomIdOrderByCreatedAtDesc(chatroomId);

            Long newMessageCount = chatRepository.
                countByRoomIdAndCreatedAtGreaterThan(challenge.getChatroom().getId(),
                    visitMap.get(chatroomId));

            ChatroomInfo chatroomInfo = new ChatroomInfo(chatroomId, chatMessage, newMessageCount);
            myChallengeInfos.add(new MyChallengeInfo(challenge, chatroomInfo));
        }

        return myChallengeInfos;
    }

    /**
     * 키워드로 챌린지 검색하기
     */
    public PageResponseDto<List<ChallengeSimpleInfo>> searchChallenges(
            Member member, Category category, int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Challenge> challenges = challengeRepository.searchChallenges(member.getId(), keyword, category, pageable);
        return makeChallengesSimpleInfoList(challenges);
    }

    /**
     * 채팅방 상단 챌린지 정보 조회하기
     */
    public ChatroomTopBarInfo getChatroomTopBarInfo(Member member, Long id) {
        Challenge challenge = findByIdWithValidation(id);
        // 현재 내 인증 횟수
        int certificationCount = certificationRepository.countsByMemberAndChallenge(member, challenge);
        // 오늘 인증 여부
        String date = String.valueOf(LocalDate.now());
        boolean todayCertification = certificationRepository.findByDateAndMemberAndChallenge(date, member, challenge).isPresent();
        return new ChatroomTopBarInfo(challenge, certificationCount, todayCertification);
    }

    public HashMap makeHashMapFromRequest(List<MyChallengesRequest> myChallengesRequests) {
        HashMap<Long, String> hashMap = new HashMap<>();
        for (MyChallengesRequest obj : myChallengesRequests) {
            hashMap.put(obj.getChatroomId(), obj.getLastVisit());
        }
        return hashMap;
    }
}
