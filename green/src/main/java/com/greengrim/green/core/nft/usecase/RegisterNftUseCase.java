package com.greengrim.green.core.nft.usecase;

import com.greengrim.green.core.member.entity.Member;
import com.greengrim.green.core.nft.entity.Nft;

public interface RegisterNftUseCase {

    void exchangeNft(Member member, Long id);

    void afterExchange(Member member, Nft nft, String txHash);

}
