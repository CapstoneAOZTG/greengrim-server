package com.greengrim.green.common.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.ChattingErrorCode;
import com.greengrim.green.core.alarm.AlarmType;
import com.greengrim.green.core.chat.ChatMessage;
import com.greengrim.green.core.member.Member;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final FirebaseMessaging firebaseMessaging;

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

    public void sendChatMessage(ChatMessage chatMessage) {
        Message message = Message.builder()
                .putData("type", String.valueOf(chatMessage.getType()))
                .putData("roomId", String.valueOf(chatMessage.getRoomId()))
                .putData("senderId", String.valueOf(chatMessage.getSenderId()))
                .putData("certId", String.valueOf(chatMessage.getCertId()))
                .putData("message", chatMessage.getMessage())
                .putData("nickName", chatMessage.getNickName())
                .putData("profileImg", chatMessage.getProfileImg())
                .putData("certImg", chatMessage.getCertImg())
                .putData("sentDate", chatMessage.getSentDate())
                .putData("sentTime", chatMessage.getSentTime())
                .setTopic(String.valueOf(chatMessage.getRoomId()))
                .build();

        send(message);
    }

    public void sendGetCertificationPoint(Member member, Long resourceId,
                             String variableContent) {
        Message message = Message.builder()
                .putData("type", "POINT_CERTIFICATION")
                .putData("resourceId", String.valueOf(resourceId))
                .putData("message", variableContent + " " + AlarmType.POINT_CERTIFICATION.getContent())
                .setToken(member.getFcmToken())
                .build();

        send(message);
    }

    public void sendGetVerificationPoint(Member member) {
        Message message = Message.builder()
                .putData("type", "POINT_VERIFICATION")
                .putData("message", AlarmType.POINT_VERIFICATION.getContent())
                .setToken(member.getFcmToken())
                .build();

        send(message);
    }

    public void sendSuccessChallenge(Member member, Long resourceId, String variableContent) {
        Message message = Message.builder()
                .putData("type", "CHALLENGE_SUCCESS")
                .putData("resourceId", String.valueOf(resourceId))
                .putData("message", variableContent + " " + AlarmType.CHALLENGE_SUCCESS.getContent())
                .setToken(member.getFcmToken())
                .build();

        send(message);
    }

    public void sendMintingSuccess(Member member, Long resourceId, String variableContent) {
        Message message = Message.builder()
                .putData("type", "EXCHANGE_SUC")
                .putData("resourceId", String.valueOf(resourceId))
                .putData("message", variableContent + " " + AlarmType.NFT_EXCHANGE.getContent())
                .putData("memberId", "")
                .setToken(member.getFcmToken())
                .build();

        send(message);
    }

    public void sendNftLike(Member member, Long resourceId, String variableContent) {
        Message message = Message.builder()
                .putData("type", "NFT_LIKE")
                .putData("resourceId", String.valueOf(resourceId))
                .putData("message", variableContent + " " + AlarmType.NFT_LIKE.getContent())
                .setToken(member.getFcmToken())
                .build();

        send(message);
    }

    public void sendMintingFail(Member member) {
        Message message = Message.builder()
                .putData("type", "EXCHANGE_FAIL")
                .putData("message", "NFT 교환에 실패했어요.")
                .setToken(member.getFcmToken())
                .build();

        send(message);
    }

    public void sendNewIssue(Long resourceId, String variableContent) {
        Message message = Message.builder()
                .putData("type", "NEW_ISSUE")
                .putData("resourceId", String.valueOf(resourceId))
                .putData("message", variableContent + " " + AlarmType.NEW_ISSUE.getContent())
                .setTopic(FcmTopicType.TOPIC_ISSUE.getTitle())
                .build();

        send(message);
    }

    public void sendNewNotice(Long resourceId, String variableContent) {
        Message message = Message.builder()
                .putData("type", "NEW_NOTICE")
                .putData("resourceId", String.valueOf(resourceId))
                .putData("message", variableContent)
                .setTopic(FcmTopicType.TOPIC_NOTICE.getTitle())
                .build();

        send(message);
    }

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
}
