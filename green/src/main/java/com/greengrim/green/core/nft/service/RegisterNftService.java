package com.greengrim.green.core.nft.service;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.NftErrorCode;
import com.greengrim.green.common.fcm.FcmService;
import com.greengrim.green.common.web3j.Abi;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.member.repository.MemberRepository;
import com.greengrim.green.core.nft.Nft;
import com.greengrim.green.core.nft.repository.NftRepository;
import com.greengrim.green.core.nft.usecase.RegisterNftUseCase;
import jakarta.transaction.Transactional;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

@Log4j2
@Service
@RequiredArgsConstructor
public class RegisterNftService implements RegisterNftUseCase {

    private final NftRepository nftRepository;
    private final MemberRepository memberRepository;
    private final FcmService fcmService;
    private final Abi contract;

    /**
     * NFT 교환하기
     */
    @Async
    @Transactional
    public void exchangeNft(Member member, Long id) {
        Nft nft = nftRepository.findByIdAndStatusTrue(id)
            .orElseThrow(() -> new BaseException(NftErrorCode.EMPTY_NFT));

        if(nft.getMember() != null) {
            throw new BaseException(NftErrorCode.ALREADY_EXCHANGED_NFT);}

        TransactionReceipt transactionReceipt = null;
        try {
            transactionReceipt = contract.safeMint("0x3a475978c5D12d07c938350a4D83CDed70Df73CF", BigInteger.valueOf(5)).sendAsync().get();
        }
        catch (InterruptedException | ExecutionException e) {
            throw new BaseException(NftErrorCode.EXCHANGE_FAIL);}

        if (transactionReceipt != null) {
            member.minusPoint(300);
            nft.setMember(member);
            memberRepository.save(member);
            fcmService.sendMintingSuccess(member, nft.getId());
        }
        else {
            fcmService.sendMintingFail(member);
        }
    }
}