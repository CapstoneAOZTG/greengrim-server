package com.greengrim.green.core.challenge.controller;

import com.greengrim.green.common.entity.SortOption;
import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.challenge.Category;
import com.greengrim.green.core.challenge.HotChallengeOption;
import com.greengrim.green.core.challenge.dto.ChallengeRequestDto.MyChallengesRequest;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.ChallengeDetailInfo;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.ChallengeSimpleInfo;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.ChatroomTopBarInfo;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.HomeChallenges;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.MyChallengeInfo;
import com.greengrim.green.core.challenge.service.GetChallengeService;
import com.greengrim.green.core.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visitor/challenges")
public class GetChallengeController {

    private final GetChallengeService getChallengeService;

    /**
     * [GET] 챌린지 상세 조회
     */
    @Operation(summary = "챌린지 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ChallengeDetailInfo> getChallengeInfo(
            @CurrentMember Member member,
            @PathVariable("id") Long id) {
        return new ResponseEntity<>(
                getChallengeService.getChallengeDetail(member, id),
                HttpStatus.OK);
    }

    /**
     * [GET] 카테고리 별 챌린지 목록 조회
     */
    @Operation(summary = "카테고리 별 챌린지 목록 조회")
    @GetMapping
    public ResponseEntity<PageResponseDto<List<ChallengeSimpleInfo>>> getChallengesByCategory(
            @CurrentMember Member member,
            @RequestParam(value = "category") Category category,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") SortOption sort) {
        return ResponseEntity.ok(getChallengeService.getChallengesByCategory(
                member, category, page, size, sort));
    }

    /**
     * [GET] 멤버 별 참여중인 챌린지 목록 조회
     */
    @Operation(summary = "멤버 별 참여중인 챌린지 목록 조회",
            description = "자신이 참여중인 챌린지를 조회하고 싶다면 memberId는 안 보내셔도 됩니다!")
    @GetMapping("/members")
    public ResponseEntity<PageResponseDto<List<ChallengeSimpleInfo>>> getMemberChallenges(
            @CurrentMember Member member,
            @RequestParam(value = "memberId", required = false) Long id,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") SortOption sort) {
        return ResponseEntity.ok(getChallengeService.getChallengesByMemberId(
                id, member, page, size, sort));
    }

    /**
     * [GET] 홈 화면 핫 챌린지 5개 조회
     */
    @Operation(summary = "홈 화면 핫 챌린지 조회")
    @GetMapping("/home")
    public ResponseEntity<HomeChallenges> getHotChallenges(
            @CurrentMember Member member) {
        return ResponseEntity.ok(getChallengeService.getHotChallenges(member));
    }

    /**
     * [GET] 핫 챌린지 더보기
     */
    @Operation(summary = "핫 챌린지 더 보기")
    @GetMapping("/hot-challenges")
    public ResponseEntity<PageResponseDto<List<ChallengeSimpleInfo>>> getMoreHotChallenges(
            @CurrentMember Member member,
            @RequestParam(value = "option") HotChallengeOption option,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size) {
        return ResponseEntity.ok(getChallengeService.getMoreHotChallenges(member, option, page, size));
    }

    /**
     * [GET] 내 채팅방(챌린지) 조회
     */
    @Operation(summary = "내 채팅방(챌린지) 조회")
    @PostMapping("/chatrooms")
    public ResponseEntity<List<MyChallengeInfo>> getMyChallenges(
        @CurrentMember Member member,
        @RequestBody List<MyChallengesRequest> myChallengesRequests) {
        return ResponseEntity.ok(getChallengeService.getMyChallenges(member, myChallengesRequests));
    }

    /**
     * [GET] 채팅방 상단 챌린지 정보 조회
     */
    @Operation(summary = "채팅방 상단 챌린지 정보 조회")
    @GetMapping("/chatroom-topbar/{id}")
    public ResponseEntity<ChatroomTopBarInfo> getChatroomTopBarInfo(
            @CurrentMember Member member,
            @PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(getChallengeService.getChatroomTopBarInfo(member, id));
    }

}
