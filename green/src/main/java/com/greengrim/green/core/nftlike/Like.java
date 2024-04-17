package com.greengrim.green.core.nftlike;

import com.greengrim.green.common.entity.BaseTime;
import com.greengrim.green.core.nft.Nft;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Like extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Nft nft;

    @NotNull
    private boolean status;

    public boolean changeStatus() {
        this.status = !this.status;
        return this.status;
    }
}
