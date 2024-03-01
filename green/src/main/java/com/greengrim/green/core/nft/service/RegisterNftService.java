package com.greengrim.green.core.nft.service;

import static com.greengrim.green.common.kas.KasConstants.MINTING_FEE;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.s3.S3Service;
import com.greengrim.green.core.grim.Grim;
import com.greengrim.green.core.grim.repository.GrimRepository;
import com.greengrim.green.common.kas.KasProperties;
import com.greengrim.green.common.kas.KasService;
import com.greengrim.green.common.kas.NftManager.NftManagerService;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.nft.Nft;
import com.greengrim.green.core.nft.dto.NftRequestDto.RegisterNft;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftId;
import com.greengrim.green.core.nft.repository.NftRepository;
import com.greengrim.green.core.transaction.dto.TransactionRequestDto.MintingTransactionDto;
import com.greengrim.green.core.transaction.dto.TransactionRequestDto.TransactionSetDto;
import com.greengrim.green.core.transaction.service.RegisterTransactionService;
import com.greengrim.green.core.wallet.Wallet;
import com.greengrim.green.core.wallet.service.WalletService;
import jakarta.transaction.Transactional;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Transactional
@Log4j2
@Service
@RequiredArgsConstructor
public class RegisterNftService {

    private final S3Service s3Service;
    private final KasService kasService;
    private final KasProperties kasProperties;
    private final WalletService walletService;
    private final NftManagerService nftManagerService;
    private final NftRepository nftRepository;
    private final GrimRepository grimRepository;
    private final RegisterTransactionService registerTransactionService;

    public Nft register(Member member, RegisterNft registerNft, String nftId,
                        String contracts, String txHash, String imgUrl, Grim grim) {
        Nft nft = Nft.builder()
                .nftId(nftId)
                .contracts(contracts)
                .txHash(txHash)
                .imgUrl(imgUrl)
                .title(registerNft.getTitle())
                .description(registerNft.getDescription())
                .reportCount(0)
                .status(true)
                .member(member)
                .grim(grim)
                .market(null)
                .build();
        nftRepository.save(nft);
        return nft;
    }

    public NftId registerNft(Member member, RegisterNft registerNft)
            throws IOException, ParseException, java.text.ParseException, InterruptedException {
        Grim grim = grimRepository.findByIdAndStatusIsTrue(registerNft.getGrimId())
                .orElseThrow(() -> new BaseException(GrimErrorCode.EMPTY_GRIM));

        Wallet wallet = member.getWallet();
        // 비밀번호 맞는지 확인 및 지갑 사용 처리
        walletService.checkPayPassword(wallet, registerNft.getPassword());
        //walletService.useWallet(wallet);

        // TODO: 수수료 납부 설정하기
        String sendFee = "";
        if (MINTING_FEE != 0D) {
            kasService.sendKlayToFeeAccount(wallet, MINTING_FEE);
        }

        log.info("=============================================");
        // Asset 업로드하고 imgUrl 받아오기
        String asset = kasService.uploadAsset(s3Service.parseFileName(grim.getImgUrl()));
        log.info("asset: {} \n", asset);
        // MetaData 업로드하고 받아오기
        String imgUrl = kasService.uploadMetadata(registerNft, asset);
        log.info("imgUrl: {} \n", imgUrl);
        // GreenGrim 토큰으로 발행된 NFT는 모두 순서를 정해서 번호를 해야함! 우리는 그냥 10진수로 하자
        String nftId = "0x" + Long.toHexString(nftManagerService.getNftIdAndPlusOne());
        log.info("nftId: {} \n", nftId);
        // Minting 하고 txHash 받아오기
        String txHash = kasService.createNft(wallet.getAddress(), nftId, imgUrl);
        log.info("txHash: {} \n", txHash);
        // NFT 객체 생성하기
        Nft nft = register(member,
                registerNft,
                nftId,
                kasProperties.getNftContractAddress(),
                txHash,
                asset,
                grim
        );
        log.info("nft 객체 생성 완료!");
        grim.setNft(nft);
        log.info("grimId {}에 nftId {} 할당 완료", grim.getId(), nft.getId());
        registerTransactionService.saveMintingTransaction(
                new MintingTransactionDto(member.getId(), nft,
                        new TransactionSetDto("", txHash, sendFee)));
        log.info("=============================================");
        return new NftId(nft.getId());
    }
}
