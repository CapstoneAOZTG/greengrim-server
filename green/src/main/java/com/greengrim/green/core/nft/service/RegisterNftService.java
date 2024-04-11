package com.greengrim.green.core.nft.service;

import com.greengrim.green.common.s3.S3Service;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.nft.Nft;
import com.greengrim.green.core.nft.dto.NftRequestDto.RegisterNft;
import com.greengrim.green.core.nft.repository.NftRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Transactional
@Log4j2
@Service
@RequiredArgsConstructor
public class RegisterNftService {

    private final S3Service s3Service;
    private final NftRepository nftRepository;

    public Nft register(Member member, RegisterNft registerNft, String tokenId,
        String txHash, String imgUrl) {
        Nft nft = Nft.builder()
                .tokenId(tokenId)
                .txHash(txHash)
                .imgUrl(imgUrl)
                .title(registerNft.getTitle())
                .description(registerNft.getDescription())
                .reportCount(0)
                .status(true)
                .member(member)
                .build();
        nftRepository.save(nft);
        return nft;
    }


}
