package com.greengrim.green.core.nft.service;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.NftErrorCode;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.nft.Nft;
import com.greengrim.green.core.nft.dto.NftRequestDto.NftModifyInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftDetailInfo;
import com.greengrim.green.core.nft.repository.NftRepository;
import com.greengrim.green.core.nft.usecase.UpdateNftUseCase;
import jakarta.transaction.Transactional;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class UpdateNftService implements UpdateNftUseCase {

    private final NftRepository nftRepository;
    private final String[][] traits;

    /**
     * NFT 삭제하기
     */
    @Transactional
    public void delete(Member member, Long id) {
        Nft nft = nftRepository.findByIdAndStatusTrue(id)
            .orElseThrow(() -> new BaseException(NftErrorCode.EMPTY_NFT));

        checkIsMine(member.getId(), nft.getMember().getId());

        nft.setStatusFalse();
        nftRepository.save(nft);
    }

    /**
     * NFT 수정하기
     */
    @Transactional
    public NftDetailInfo modify(Member member, NftModifyInfo modifyInfo) {
        Nft nft = nftRepository.findByIdAndStatusTrue(modifyInfo.getId())
            .orElseThrow(() -> new BaseException(NftErrorCode.EMPTY_NFT));

        checkIsMine(member.getId(), nft.getMember().getId());

        nft.modify(modifyInfo);
        nftRepository.save(nft);

        return new NftDetailInfo(nft, true, traits);
    }

    public void checkIsMine(Long viewerId, Long ownerId) {
        if(!Objects.equals(viewerId, ownerId)) {
            throw new BaseException(NftErrorCode.NO_AUTHORIZATION);
        }
    }

}