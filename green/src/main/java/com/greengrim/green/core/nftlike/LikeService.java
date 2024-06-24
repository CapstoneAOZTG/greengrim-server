package com.greengrim.green.core.nftlike;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.NftErrorCode;
import com.greengrim.green.common.fcm.FcmService;
import com.greengrim.green.core.alarm.entity.AlarmType;
import com.greengrim.green.core.alarm.service.RegisterAlarmService;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.nft.Nft;
import com.greengrim.green.core.nft.repository.NftRepository;
import com.greengrim.green.core.nftlike.dto.LikeRequestDto.RegisterLike;
import com.greengrim.green.core.nftlike.dto.LikeResponseDto.PushLikeInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final NftRepository nftRepository;
    private final FcmService fcmService;
    private final RegisterAlarmService registerAlarmService;

    public void save(Like like) {
        likeRepository.save(like);
    }

    public void register(Long memberId, Nft nft) {
        save(Like.builder()
                .memberId(memberId)
                .nft(nft)
                .status(true)
                .build());
    }

    public PushLikeInfo pushLike(Member member, RegisterLike registerLike) {
        Nft nft = nftRepository.findByIdAndStatusTrue(registerLike.getNftId())
                .orElseThrow(() -> new BaseException(NftErrorCode.EMPTY_NFT));

        Like like = likeRepository.findByMemberIdAndNft(member.getId(), nft).orElse(null);
        if (like == null) { // 좋아요를 누른 적 없음
            register(member.getId(), nft);
            newLike(member, nft);
            return new PushLikeInfo(true);
        } else { // 좋아요를 누른 적 있음
            boolean likeStatus = like.changeStatus();   // 좋아요 누른 후 상태 반환
            if(likeStatus) { // // 좋아요 상태가 true
                newLike(member, nft);
            }
            else {
                nft.minusLikeCount();                  // 좋아요 상태가 false 면 -1
            }
            return new PushLikeInfo(likeStatus);
        }
    }

    /**
     * 좋아요를 누른 상태라면 true 를, 누르지 않은 상태라면 false 리턴
     * 검증된 memberId, NFT만 넣어주세용
     */
    public boolean checkIsLiked(Long memberId, Nft nft) {
        Like like = likeRepository.findByMemberIdAndNft(memberId, nft).orElse(null);
        if (like == null) {
            return false;
        } else {
            return like.isStatus();
        }
    }

    /**
     * 누군가 좋아요를 눌렀다면
     * 좋아요 수 +1, fcm 전송, 알림 보관
     */
    public void newLike(Member member, Nft nft) {
        nft.plusLikeCount();
        fcmService.sendNftLike(nft.getMember(), nft.getId(), member.getNickName());
        registerAlarmService.register(nft.getMember(), AlarmType.NFT_LIKE, nft.getId(), nft.getImgUrl(), member.getNickName(), member.getId());
    }
}
