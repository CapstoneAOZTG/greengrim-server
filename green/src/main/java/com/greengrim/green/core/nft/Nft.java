package com.greengrim.green.core.nft;

import com.greengrim.green.common.entity.BaseTime;
import com.greengrim.green.core.grim.Grim;
import com.greengrim.green.core.market.Market;
import com.greengrim.green.core.member.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
    private String nftId;
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private String contracts;
    @NotNull
    private String txHash;
    @NotNull
    private String imgUrl;
    @NotNull
    private int reportCount;
    @NotNull
    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    private Grim grim;

    @OneToOne(mappedBy = "nft", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Market market;

    public void delete() {
        this.status = false;
        this.grim = null;
    }

    public boolean isMarketed() {
        return this.market != null;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public void setMember(Member member) {
        this.member = member;
    }

}
