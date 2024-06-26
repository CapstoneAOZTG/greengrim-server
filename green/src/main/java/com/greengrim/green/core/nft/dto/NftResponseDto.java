package com.greengrim.green.core.nft.dto;

import static com.greengrim.green.common.entity.Time.calculateTime;
import static com.greengrim.green.common.util.UtilService.formatIntToString;

import com.greengrim.green.core.member.dto.MemberResponseDto.MemberSimpleInfo;
import com.greengrim.green.core.nft.entity.Nft;
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
        private boolean isLiked;
        private TraitsInfo traitsInfo;

        public NftDetailInfo(Nft nft, boolean isMine, boolean isLiked, String [][] traits) {
            this.memberSimpleInfo = new MemberSimpleInfo(nft.getMember());
            this.nftInfo = new NftInfo(nft);
            this.tokenId = nft.getTokenId();
            this.isMine = isMine;
            this.isLiked = isLiked;
            this.traitsInfo = new TraitsInfo(traits, nft.getTraits());
        }
    }

    @Getter
    @AllArgsConstructor
    public static class NftStockInfo {
        private Long nftId;
        private String imgUrl;
        private String title;
        private int tokenId;
        private TraitsInfo traitsInfo;

        public NftStockInfo(Nft nft, String [][] traits) {
            this.nftId = nft.getId();
            this.imgUrl = nft.getImgUrl();
            this.title = nft.getTitle();
            this.tokenId = nft.getTokenId();
            this.traitsInfo = new TraitsInfo(traits, nft.getTraits());
        }
    }

    @Getter
    @AllArgsConstructor
    public static class NftStockAmountInfo {
        private int basic;
        private int standard;
        private int premium;
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
    public static class NftCollectionInfo {
        private Long id;
        private String imgUrl;
        private String title;
        private String tokenId;

        public NftCollectionInfo(Nft nft) {
            this.id = nft.getId();
            this.imgUrl = nft.getImgUrl();
            this.title = nft.getTitle();
            this.tokenId = "#" + String.valueOf(nft.getTokenId());
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class NftAndMemberInfo {
        private NftSimpleInfo nftSimpleInfo;
        private MemberSimpleInfo memberSimpleInfo;
        private boolean isLike;

        public NftAndMemberInfo(Nft nft, boolean isLike) {
            this.nftSimpleInfo = new NftSimpleInfo(nft);
            this.memberSimpleInfo = new MemberSimpleInfo(nft.getMember());
            this.isLike = isLike;
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class HotNftInfo {
        private NftSimpleInfo nftSimpleInfo;
        private MemberSimpleInfo memberSimpleInfo;
        private String likeCount;

        public HotNftInfo(Nft nft) {
            this.nftSimpleInfo = new NftSimpleInfo(nft);
            this.memberSimpleInfo = new MemberSimpleInfo(nft.getMember());
            this.likeCount = formatIntToString(nft.getLikeCount());
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class TraitsInfo {
        public String background;
        public String hair;
        public String face;
        public String gesture;
        public String shoes;
        public String accessory;
        public String rarity;

        TraitsInfo(String [][] traits, String traitsString) {
            this.background = traits[0][traitsString.charAt(0) - '0' - 1];
            this.hair = traits[1][traitsString.charAt(1) - '0' - 1];
            this.face = traits[2][traitsString.charAt(2) - '0'- 1];
            this.gesture = traits[3][traitsString.charAt(3) - '0' - 1];
            this.shoes = traits[4][traitsString.charAt(4) - '0' - 1];
            this.accessory = traits[5][traitsString.charAt(5) - '0' - 1];
            this.rarity = traits[6][traitsString.charAt(6) - '0' - 1];
        }
    }
}
