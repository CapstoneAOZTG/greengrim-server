package com.greengrim.green.core.nft;

import com.greengrim.green.common.entity.BaseTime;
import com.greengrim.green.core.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Nft extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private int tokenId;
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private String imgUrl;
    @NotNull
    private String traits;
    @NotNull
    @Enumerated(EnumType.STRING)
    private NftGrade grade;
    @NotNull
    private int reportCount;
    @NotNull
    private boolean status;


    private String txHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;

    public void delete() {
        this.status = false;
    }

    public void setMember(Member member) {
        this.member = member;
    }

}
