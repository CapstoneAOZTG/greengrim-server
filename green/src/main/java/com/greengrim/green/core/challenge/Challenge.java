package com.greengrim.green.core.challenge;

import com.greengrim.green.common.entity.BaseTime;
import com.greengrim.green.core.chatroom.Chatroom;
import com.greengrim.green.core.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    private String title;
    @Size(max = 200)
    private String description;
    private String imgUrl;
    @NotNull
    private Category category;
    @NotNull
    private int goalCount;          // 목표 인증 횟수
    @NotNull
    private int ticketTotalCount;   // 총 티켓 개수
    @NotNull
    private int ticketCurrentCount; // 현재 남은 티켓 개수
    @NotNull
    @Min(2)
    @Max(6)
    private int weekMinCount;       // 주 최소 인증 횟수
    @NotNull
    @Max(100)
    private int capacity;           // 수용 가능 인원
    @NotNull
    @Max(100)
    private int headCount;          // 현재 인원
    @NotNull
    private String keyword;

    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    private Chatroom chatroom;

    public String getGoalCountTag() {
        return "인증 " + this.getGoalCount() + "회";
    }

    public String getTicketCountTag() {
        return "티켓 " + (this.ticketTotalCount - this.ticketCurrentCount)
                + "/" + this.ticketTotalCount;
    }

    public String getKeywordTag() {
        return "#키워드 " + this.keyword;
    }

    public String getWeekMinCountTag() {
        return "최소 인증 주 " + this.weekMinCount + "회";
    }

    public String getParticipantCountTag() {
        return "인원 " + this.headCount + "/" + this.capacity;
    }

    public void setHeadCount(int headCount) {
        this.headCount = headCount;
    }

    public void minusTicketCurrentCount() {
        if(this.ticketCurrentCount != 0) {
            this.ticketCurrentCount--;
        }
    }
}
