package com.greengrim.green.common.fcm;

import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visitor/fcm")
public class FcmController {

    private final FcmService fcmService;

    /**
     * [POST] 채팅 FCM 구독하기
     */
    @Operation(summary = "FCM 구독 - 채팅")
    @PostMapping("/subscribe")
    public void subscribeChatroom(
            @CurrentMember Member member,
            @RequestParam Long chatroomId) {
        fcmService.subscribeChatroom(member, chatroomId);
    }

    /**
     * [POST] 토픽 FCM 구독하기
     */
    @Operation(summary = "FCM 구독 - 토픽")
    @PostMapping("/subscribe/topics")
    public void subscribeTopic(
            @CurrentMember Member member,
            @RequestParam FcmTopicType topic) {
        fcmService.subscribeTopic(member, topic);
    }

    /**
     * [DELETE] 채팅 FCM 구독 취소하기
     */
    @Operation(summary = "FCM 구독 취소 - 채팅")
    @DeleteMapping("/unsubscribe")
    public void unsubscribeChatroom(
            @CurrentMember Member member,
            @RequestParam Long chatroomId) {
        fcmService.unsubscribeChatroom(member, chatroomId);
    }

    /**
     * [DELETE] 토픽 FCM 구독 취소하기
     */
    @Operation(summary = "FCM 구독 취소 - 토픽")
    @DeleteMapping("/unsubscribe/topics")
    public void unsubscribeTopic(
            @CurrentMember Member member,
            @RequestParam FcmTopicType topic) {
        fcmService.unsubscribeTopic(member, topic);
    }

}