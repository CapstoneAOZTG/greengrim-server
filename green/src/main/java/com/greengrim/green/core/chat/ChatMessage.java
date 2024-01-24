package com.greengrim.green.core.chat;

import jakarta.persistence.Column;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class ChatMessage  {

  public enum MessageType {
    ENTER, TALK, EXIT, CERT
  }
  private MessageType type;
  private Long roomId;
  private Long senderId;
  private Long certId;
  private String message;
  private String nickName;
  private String profileImg;
  private String certImg;
  private String sentDate;
  private String sentTime;

  @Column(name = "created_at")
  private Long createdAt;

  public
  ChatMessage() {
  }

  @Builder
  public ChatMessage(MessageType type, Long roomId, Long senderId,
      Long certId, String message, String nickName, String profileImg, String certImg) {
    this.type = type;
    this.roomId = roomId;
    this.senderId = senderId;
    this.certId = certId;
    this.message = message;
    this.nickName = nickName;
    this.profileImg = profileImg;
    this.certImg = certImg;
  }

  public void setTime() {
    LocalDateTime now = LocalDateTime.now();
    System.out.println(now);

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 E요일", Locale.KOREAN);
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("a h시 m분", Locale.KOREAN);
    DateTimeFormatter createAtFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    this.createdAt = Long.valueOf(now.format(createAtFormatter));
    this.sentDate = now.format(dateFormatter);
    this.sentTime = now.format(timeFormatter);
  }
}
