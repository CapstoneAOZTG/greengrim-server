package com.greengrim.green.common.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.ChattingErrorCode;
import com.greengrim.green.core.chat.ChatMessage;
import com.greengrim.green.core.chatparticipant.Chatparticipant;
import com.greengrim.green.core.chatparticipant.ChatparticipantService;
import com.greengrim.green.core.member.Member;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmService {

  private final FirebaseMessaging firebaseMessaging;
  private final ChatparticipantService chatparticipantService;

  public void subscribe(Member member) {
    List<Chatparticipant> chatparticipants = chatparticipantService.findByMemberId(member.getId());
    List<String> memberFcmToken = new ArrayList<>();
    memberFcmToken.add(member.getFcmToken());
    chatparticipants.forEach(chatparticipant -> {
      try {
        firebaseMessaging.getInstance()
            .subscribeToTopic(memberFcmToken, Long.toString(chatparticipant.getChatroom().getId()));
      } catch (FirebaseMessagingException e) {
        throw new BaseException(ChattingErrorCode.FAIL_FCM_SUBSCRIBE);
      }
    });
  }

  public void unsubscribe(Member member) {
    List<Chatparticipant> chatparticipants = chatparticipantService.findByMemberId(member.getId());
    List<String> memberFcmToken = new ArrayList<>();
    memberFcmToken.add(member.getFcmToken());
    chatparticipants.forEach(chatparticipant -> {
      try {
        firebaseMessaging.getInstance()
            .unsubscribeFromTopic(memberFcmToken, Long.toString(chatparticipant.getChatroom().getId()));
      } catch (FirebaseMessagingException e) {
        throw new BaseException(ChattingErrorCode.FAIL_FCM_SUBSCRIBE);
      }
    });
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

    ///send(message);
  }

  public void sendGetPoint(Member member, String title, int point) {
    Message message = Message.builder()
        .putData("type", "POINT")
        .putData("roomId", title + "(으)로 " + point + " 포인트를 획득했습니다.")
        .setToken(member.getFcmToken())
        .build();

    //send(message);
  }

  public void sendMintingSuccess(Member member, Long nftId) {
    Message message = Message.builder()
        .putData("type", "EXCHANGE_SUC")
        .putData("nftId", String.valueOf(nftId))
        .putData("message", "NFT 교환에 성공했어요.")
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

  public void send(Message message) {
    try {
      firebaseMessaging.getInstance().send(message);
    } catch (FirebaseMessagingException e) {
      throw new BaseException(ChattingErrorCode.FAIL_SEND_MESSAGE);
    }

    Gson gson = new Gson();
    String fcmMessageJson = gson.toJson(message);
    System.out.println("FCM 메시지: " + fcmMessageJson);
  }
}
