package com.greengrim.green.core.hiding.nft;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.HidingErrorCode;
import com.greengrim.green.common.exception.errorCode.NftErrorCode;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.nft.repository.NftRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NftHidingService {

    private final NftHidingRepository nftHidingRepository;
    private final NftRepository nftRepository;

    @Transactional
    public void register(Long memberId, Long nftId) {
        nftHidingRepository.save(
                NftHiding.builder()
                        .memberId(memberId)
                        .nftId(nftId)
                        .build());
    }

    @Transactional
    public void hideNft(Member member, Long nftId) {
        checkNftValidation(nftId);
        if (checkNonExistingHiding(member.getId(), nftId)) {
            register(member.getId(), nftId);
        } else {
            throw new BaseException(HidingErrorCode.ALREADY_HIDING);
        }
    }

    private void checkNftValidation(Long nftId) {
        nftRepository.findByIdAndStatusTrue(nftId)
                .orElseThrow(() -> new BaseException(NftErrorCode.INVALID_NFT));
    }

    private boolean checkNonExistingHiding(Long memberId, Long nftId) {
        return nftHidingRepository.findByMemberIdAndNftId(memberId, nftId).isEmpty();
    }
}
