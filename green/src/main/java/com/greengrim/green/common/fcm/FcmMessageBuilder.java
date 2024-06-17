package com.greengrim.green.common.fcm;

import com.google.firebase.messaging.Message;
import com.greengrim.green.core.alarm.AlarmType;
import com.greengrim.green.core.chat.ChatMessage;
import com.greengrim.green.core.member.Member;
import org.springframework.stereotype.Component;

@Component
public class FcmMessageBuilder {

    /**
     * 토픽 기반 메세지
     */
    public Message chatMessage(ChatMessage chatMessage){
        return Message.builder()
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
    }

    public Message newIssue(Long resourceId, String variableContent) {
        return Message.builder()
            .putData("type", "NEW_ISSUE")
            .putData("resourceId", String.valueOf(resourceId))
            .putData("message", variableContent + " " + AlarmType.NEW_ISSUE.getContent())
            .setTopic(FcmTopicType.TOPIC_ISSUE.getTitle())
            .build();
    }

    public Message newNotice(Long resourceId, String variableContent) {
        return Message.builder()
            .putData("type", "NEW_NOTICE")
            .putData("resourceId", String.valueOf(resourceId))
            .putData("message", variableContent)
            .setTopic(FcmTopicType.TOPIC_NOTICE.getTitle())
            .build();
    }

    /**
     * 토큰 기반 메세지
     */
    public Message getCertificationPoint(Member member, Long resourceId,
        String variableContent) {
        return Message.builder()
            .putData("type", "POINT_CERTIFICATION")
            .putData("resourceId", String.valueOf(resourceId))
            .putData("message", variableContent + " " + AlarmType.POINT_CERTIFICATION.getContent())
            .setToken(member.getFcmToken())
            .build();
    }

    public Message getVerificationPoint(Member member) {
        return Message.builder()
            .putData("type", "POINT_VERIFICATION")
            .putData("message", AlarmType.POINT_VERIFICATION.getContent())
            .setToken(member.getFcmToken())
            .build();
    }

    public Message successChallenge(Member member, Long resourceId, String variableContent) {
        return Message.builder()
            .putData("type", "CHALLENGE")
            .putData("resourceId", String.valueOf(resourceId))
            .putData("message", variableContent + " " + AlarmType.CHALLENGE_SUCCESS.getContent())
            .setToken(member.getFcmToken())
            .build();
    }

    public Message mintingSuccess(Member member, Long resourceId, String variableContent) {
        return Message.builder()
            .putData("type", "EXCHANGE_SUC")
            .putData("resourceId", String.valueOf(resourceId))
            .putData("message", variableContent + " " + AlarmType.NFT_EXCHANGE.getContent())
            .putData("memberId", "")
            .setToken(member.getFcmToken())
            .build();
    }

    public Message mintingFail(Member member) {
        return Message.builder()
            .putData("type", "EXCHANGE_FAIL")
            .putData("message", "NFT 교환에 실패했어요.")
            .setToken(member.getFcmToken())
            .build();
    }
}
