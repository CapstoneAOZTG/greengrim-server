package com.greengrim.green.core.nft.service;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.NftErrorCode;
import com.greengrim.green.common.fcm.FcmService;
import com.greengrim.green.common.web3j.Abi;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.member.repository.MemberRepository;
import com.greengrim.green.core.nft.Nft;
import com.greengrim.green.core.nft.NftGrade;
import com.greengrim.green.core.nft.repository.NftRepository;
import com.greengrim.green.core.nft.usecase.RegisterNftUseCase;
import jakarta.transaction.Transactional;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

    private static final int EXCHANGE_NFT_BASIC_POINT = 500;
    private static final int EXCHANGE_NFT_STANDARD_POINT = 700;
    private static final int EXCHANGE_NFT_PREMIUM_POINT = 1000;

    /**
     * NFT 교환하기
     */
    @Transactional
    public void exchangeNft(Member member, Long id) {

        Nft nft = nftRepository.findByIdAndStatusTrue(id)
            .orElseThrow(() -> new BaseException(NftErrorCode.EMPTY_NFT));

        beforeExchange(member.getPoint(), nft.getGrade());
        if(nft.getMember() != null) {
            throw new BaseException(NftErrorCode.ALREADY_EXCHANGED_NFT);}
        TransactionReceipt transactionReceipt = null;

        try {
            transactionReceipt =
                contract.safeMint(member.getWallet().getAddress(), BigInteger.valueOf(nft.getTokenId())).sendAsync().get();
        }
        catch (InterruptedException | ExecutionException e) {
            throw new BaseException(NftErrorCode.EXCHANGE_FAIL);
        }

        if (transactionReceipt != null) {
            afterExchange(member, nft, transactionReceipt.getTransactionHash());
            fcmService.sendMintingSuccess(member, nft.getId(), String.valueOf(nft.getGrade()));
        }
        else {
            fcmService.sendMintingFail(member);
        }
    }

    public void beforeExchange(int memberPoint, NftGrade grade) {

        int point = switch (grade) {
            case BASIC -> EXCHANGE_NFT_BASIC_POINT;
            case STANDARD -> EXCHANGE_NFT_STANDARD_POINT;
            case PREMIUM -> EXCHANGE_NFT_PREMIUM_POINT;
        };

        if(memberPoint < point) {
            throw new BaseException(NftErrorCode.NOT_ENOUGH_POINT);
        }
    }

    @Transactional
    public void afterExchange(Member member, Nft nft, String txHash) {

        int point = switch (nft.getGrade()) {
            case BASIC -> EXCHANGE_NFT_BASIC_POINT;
            case STANDARD -> EXCHANGE_NFT_STANDARD_POINT;
            case PREMIUM -> EXCHANGE_NFT_PREMIUM_POINT;
        };

        nft.setMember(member);
        nft.setTxHash(txHash);
        member.minusPoint(point);
        memberRepository.save(member);
    }
}