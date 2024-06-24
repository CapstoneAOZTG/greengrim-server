package com.greengrim.green.core.nftlike.repository;

import com.greengrim.green.core.nft.entity.Nft;
import java.util.Optional;

import com.greengrim.green.core.nftlike.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByMemberIdAndNft(Long memberId, Nft nft);
}
