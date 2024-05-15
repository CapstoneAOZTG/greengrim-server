package com.greengrim.green.core.chat;

import com.greengrim.green.common.entity.Time;
import jakarta.persistence.Column;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class ChatMessage  {

  public enum MessageType {
    ENTER, TALK, EXIT, CERT, DATE
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
  private String createdAt;

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

    this.sentDate = now.format(Time.CHAT_DATE_FORMATTER);
    this.sentTime = now.format(Time.CHAT_TIME_FORMATTER);
    this.createdAt = now.format(Time.CHAT_CREATED_AT_FORMATTER);
  }

  public void setDateMessage(Long roomId) {
    LocalDateTime now = LocalDateTime.now();
    this.type = MessageType.DATE;
    this.roomId = roomId;
    this.senderId = null;
    this.certId = null;
    this.message = now.format(Time.CHAT_DATE_FORMATTER);
    this.nickName = "";
    this.profileImg = "";
    this.certImg = "";
    this.sentDate = "";
    this.sentTime = "";
  }
}
