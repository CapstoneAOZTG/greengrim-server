package com.greengrim.green.core.alarm.entity;

import com.greengrim.green.common.entity.BaseTime;
import com.greengrim.green.core.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class Alarm extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private AlarmType type;
    @NotNull
    private Long resourceId;              // 상세보기 이동을 위한 Id
    @NotNull
    private String imgUrl;                // 알림 이미지
    @Builder.Default
    private boolean isChecked = false;       // 확인했는지
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;                // 알림 주인

    private Long interactedMemberId;                // 상호작용한 memberId
    private String variableContent;       // 바뀔 수 있는 부분

    public void setChecked() {
        if(!isChecked) {
            this.isChecked = true;
        }
    }
}
