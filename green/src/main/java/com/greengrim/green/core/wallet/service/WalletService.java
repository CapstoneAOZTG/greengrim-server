package com.greengrim.green.core.wallet.service;

import static com.greengrim.green.common.kas.KasConstants.PURCHASE_FEE;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.WalletErrorCode;
import com.greengrim.green.common.kas.KasService;
import com.greengrim.green.common.password.BcryptService;
import com.greengrim.green.core.market.Market;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.member.Role;
import com.greengrim.green.core.member.service.RegisterMemberService;
import com.greengrim.green.core.nft.Nft;
import com.greengrim.green.core.transaction.dto.TransactionRequestDto.PurchaseNftOnMarketTransactionDto;
import com.greengrim.green.core.transaction.dto.TransactionRequestDto.TransactionSetDto;
import com.greengrim.green.core.transaction.service.RegisterTransactionService;
import com.greengrim.green.core.wallet.Wallet;
import com.greengrim.green.core.wallet.dto.WalletRequestDto.CheckPassword;
import com.greengrim.green.core.wallet.dto.WalletRequestDto.WalletRequest;
import com.greengrim.green.core.wallet.dto.WalletResponseDto.CheckPasswordInfo;
import com.greengrim.green.core.wallet.dto.WalletResponseDto.ExistsWalletInfo;
import com.greengrim.green.core.wallet.dto.WalletResponseDto.WalletDetailInfo;
import com.greengrim.green.core.wallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Duration;
import java.time.LocalDateTime;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    private final RegisterMemberService registerMemberService;

    public Wallet register(String name, String address) {
        Wallet wallet = Wallet.builder()
                .name(name)
                .address(address)
                .build();
        walletRepository.save(wallet);
        return wallet;
    }

    private void changeToMember(Member member, Wallet wallet) {
        member.changeRole(Role.ROLE_MEMBER);
        member.changeWallet(wallet);
        registerMemberService.save(member);
    }

    /**
     * 지갑 추가하기
     */
    public void registerWallet(Member member, WalletRequest walletRequest) {
        Wallet wallet = register(walletRequest.getName(), walletRequest.getAddress());
        changeToMember(member, wallet);
    }

    /**
     * 지갑 존재 유무 조회하기
     */
    public ExistsWalletInfo existsWallet(Member member) {
        return new ExistsWalletInfo(member.getWallet() != null);
    }

    /**
     * 지갑 정보 조회하기
     */
    public WalletDetailInfo getWalletDetail(Member member) {
        Wallet wallet = member.getWallet();
        return new WalletDetailInfo(wallet.getName(), wallet.getAddress());
    }

}
