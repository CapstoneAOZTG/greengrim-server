package com.greengrim.green.core.wallet.service;

import com.greengrim.green.core.member.entity.Member;
import com.greengrim.green.core.member.entity.Role;
import com.greengrim.green.core.member.service.RegisterMemberService;
import com.greengrim.green.core.wallet.entity.Wallet;
import com.greengrim.green.core.wallet.dto.WalletRequestDto.WalletRequest;
import com.greengrim.green.core.wallet.dto.WalletResponseDto.WalletDetailInfo;
import com.greengrim.green.core.wallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
     * 지갑 수정하기
     */
    public void modifyWallet(Member member, WalletRequest walletRequest) {
        Wallet wallet = member.getWallet();
        wallet.changeWallet(walletRequest.getName(), walletRequest.getAddress());
        registerMemberService.save(member);
        walletRepository.save(wallet);
    }

    /**
     * 지갑 정보 조회하기
     */
    public WalletDetailInfo getWalletDetail(Member member) {
        Wallet wallet = member.getWallet();
        if(wallet != null) {
            return new WalletDetailInfo(true, wallet.getName(), wallet.getAddress());
        }
        return new WalletDetailInfo(false, "", "");
    }

}
