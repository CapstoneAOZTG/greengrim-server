package com.greengrim.green.core.nft.dto;

import static com.greengrim.green.common.entity.Time.calculateTime;

import com.greengrim.green.core.member.dto.MemberResponseDto.MemberSimpleInfo;
import com.greengrim.green.core.nft.Nft;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class NftResponseDto {

    @Getter
    @AllArgsConstructor
    public static class NftId {
        private Long id;
    }

    @Getter
    @AllArgsConstructor
    public static class NftInfo {
        private Long id;
        private String imgUrl;
        private String title;
        private String description;
        private String createdAt;

        public NftInfo(Nft nft) {
            this.id = nft.getId();
            this.imgUrl = "temp Url";
            this.title = nft.getTitle();
            this.description = nft.getDescription();
            this.createdAt = calculateTime(nft.getCreatedAt(), 1);
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class NftDetailInfo {
        private MemberSimpleInfo memberSimpleInfo;
        private NftInfo nftInfo;
        private String tokenId;
        private boolean isMine;

        public NftDetailInfo(Nft nft, boolean isMine) {
            this.memberSimpleInfo = new MemberSimpleInfo(nft.getMember());
            this.nftInfo = new NftInfo(nft);
            this.tokenId = nft.getTokenId();
            this.isMine = isMine;
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class NftSimpleInfo {
        private Long id;
        private String imgUrl;
        private String title;

        public NftSimpleInfo(Nft nft) {
            this.id = nft.getId();
            this.imgUrl = "temp Url";
            this.title = nft.getTitle();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class HomeNfts {
        private List<HomeNftInfo> homeNftInfos;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class NftAndMemberInfo {
        private NftSimpleInfo nftSimpleInfo;
        private MemberSimpleInfo memberSimpleInfo;

        public NftAndMemberInfo(Nft nft) {
            this.nftSimpleInfo = new NftSimpleInfo(nft);
            this.memberSimpleInfo = new MemberSimpleInfo(nft.getMember());
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class HomeNftInfo {
        private NftAndMemberInfo nftAndMemberInfo;

        public HomeNftInfo(Nft nft) {
            this.nftAndMemberInfo = new NftAndMemberInfo(nft);
        }
    }

}
