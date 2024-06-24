package com.greengrim.green.common.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.ChattingErrorCode;
import com.greengrim.green.core.chat.entity.ChatMessage;
import com.greengrim.green.core.member.entity.Member;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final FirebaseMessaging firebaseMessaging;
    private final FcmMessageBuilder fcmMessageBuilder;

    /**
     * 채팅방 구독하기
     */
    public void subscribeChatroom(Member member, Long chatroomId) {
        List<String> memberFcmToken = new ArrayList<>();
        memberFcmToken.add(member.getFcmToken());
        try {
            firebaseMessaging.getInstance()
                    .subscribeToTopic(memberFcmToken, Long.toString(chatroomId));
        } catch (FirebaseMessagingException e) {
            throw new BaseException(ChattingErrorCode.FAIL_FCM_SUBSCRIBE);
        }
    }

    /**
     * 알람 구독 설정하기
     */
    public void subscribeTopic(Member member, FcmTopicType fcmTopicType) {
        List<String> memberFcmToken = new ArrayList<>();
        memberFcmToken.add(member.getFcmToken());
        try {
            firebaseMessaging.getInstance()
                    .subscribeToTopic(memberFcmToken, fcmTopicType.getTitle());
        } catch (FirebaseMessagingException e) {
            throw new BaseException(ChattingErrorCode.FAIL_FCM_SUBSCRIBE);
        }
    }

    /**
     * 채팅방 구독 해제하기
     */
    public void unsubscribeChatroom(Member member, Long chatroomId) {
        List<String> memberFcmToken = new ArrayList<>();
        memberFcmToken.add(member.getFcmToken());
        try {
            firebaseMessaging.getInstance()
                    .unsubscribeFromTopic(memberFcmToken, Long.toString(chatroomId));
        } catch (FirebaseMessagingException e) {
            throw new BaseException(ChattingErrorCode.FAIL_FCM_SUBSCRIBE);
        }
    }

    /**
     * 알람 구독 해제하기
     */
    public void unsubscribeTopic(Member member, FcmTopicType fcmTopicType) {
        List<String> memberFcmToken = new ArrayList<>();
        memberFcmToken.add(member.getFcmToken());
        try {
            firebaseMessaging.getInstance()
                    .unsubscribeFromTopic(memberFcmToken, fcmTopicType.getTitle());
        } catch (FirebaseMessagingException e) {
            throw new BaseException(ChattingErrorCode.FAIL_FCM_SUBSCRIBE);
        }
    }

    /**
     * FCM 메세지 전송
     */
    public void send(Message message) {
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            throw new BaseException(ChattingErrorCode.FAIL_SEND_MESSAGE);
        }

        Gson gson = new Gson();
        String fcmMessageJson = gson.toJson(message);
        System.out.println("FCM 메시지: " + fcmMessageJson);
    }


    // [메세지 전송 시나리오 목록]

    /** 채팅방 메세지 */
    public void sendChatMessage(ChatMessage chatMessage) {
        send(fcmMessageBuilder.chatMessage(chatMessage));
    }

    /** 내 인증 완료 시 포인트 지급 */
    public void sendGetCertificationPoint(Member member, Long resourceId, String variableContent) {
        send(fcmMessageBuilder.getCertificationPoint(member, resourceId, variableContent));
    }

    /** 타 인증 검증 완료 시 포인트 지급 */
    public void sendGetVerificationPoint(Member member) {
        send(fcmMessageBuilder.getVerificationPoint(member));
    }

    /** 챌린지 성공 시 포인트 지급 */
    public void sendSuccessChallenge(Member member, Long resourceId, String variableContent) {
        send(fcmMessageBuilder.successChallenge(member, resourceId, variableContent));
    }

    /** NFT 좋아요 */
    public void sendNftLike(Member member, Long resourceId, String variableContent) {
        send(fcmMessageBuilder.mintingSuccess(member, resourceId, variableContent));
    }

    /** NFT 교환하기 성공 */
    public void sendMintingSuccess(Member member, Long resourceId, String variableContent) {
        send(fcmMessageBuilder.mintingSuccess(member, resourceId, variableContent));
    }

    /** NFT 교환하기 실패 */
    public void sendMintingFail(Member member) {
        send(fcmMessageBuilder.mintingFail(member));
    }

    /** 새로운 이슈 */
    public void sendNewIssue(Long resourceId, String variableContent) {
        send(fcmMessageBuilder.newIssue(resourceId, variableContent));
    }

    /** 새로운 공지사항 */
    public void sendNewNotice(Long resourceId, String variableContent) {
        send(fcmMessageBuilder.newNotice(resourceId, variableContent));
    }
}
