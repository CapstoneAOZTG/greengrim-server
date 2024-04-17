package com.greengrim.green.core.nft.usecase;

import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.nft.dto.NftRequestDto.NftModifyInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftDetailInfo;

public interface UpdateNftUseCase {

    NftDetailInfo modify(Member member, NftModifyInfo modifyInfo);

    void delete(Member member, Long id);

    void checkIsMine(Long viewerId, Long ownerId);

}