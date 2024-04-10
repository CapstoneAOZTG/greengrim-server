package com.greengrim.green.core.nft.dto;

import static com.greengrim.green.common.entity.Time.calculateTime;

import com.greengrim.green.core.member.dto.MemberResponseDto.MemberSimpleInfo;
import com.greengrim.green.core.nft.Nft;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class NftResponseDto {

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
            this.imgUrl = nft.getImgUrl();
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
        private int tokenId;
        private boolean isMine;
        private TraitsInfo traitsInfo;

        public NftDetailInfo(Nft nft, boolean isMine, String [][] traits) {
            this.memberSimpleInfo = new MemberSimpleInfo(nft.getMember());
            this.nftInfo = new NftInfo(nft);
            this.tokenId = nft.getTokenId();
            this.isMine = isMine;
            this.traitsInfo = new TraitsInfo(traits, nft.getTraits());
        }
    }

    @Getter
    @AllArgsConstructor
    public static class NftStockInfo {
        private int tokenId;
        private TraitsInfo traitsInfo;

        public NftStockInfo(Nft nft, String [][] traits) {
            this.tokenId = nft.getTokenId();
            this.traitsInfo = new TraitsInfo(traits, nft.getTraits());
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
            this.imgUrl = nft.getImgUrl();
            this.title = nft.getTitle();
        }
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
    @RequiredArgsConstructor
    public static class TraitsInfo {
        public String background;
        public String hair;
        public String face;
        public String gesture;
        public String accessory;
        public String shoes;

        TraitsInfo(String [][] traits, String traitsString) {
            this.background = traits[0][traitsString.charAt(0) - '0'];
            this.hair = traits[1][traitsString.charAt(1) - '0'];
            this.face = traits[2][traitsString.charAt(2) - '0'];
            this.gesture = traits[3][traitsString.charAt(3) - '0'];
            this.accessory = traits[4][traitsString.charAt(4) - '0'];
            this.shoes = traits[5][traitsString.charAt(5) - '0'];
        }
    }

}
