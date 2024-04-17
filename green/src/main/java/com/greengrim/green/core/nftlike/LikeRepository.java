package com.greengrim.green.core.nftlike;

import com.greengrim.green.core.nft.Nft;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByMemberIdAndNft(Long memberId, Nft nft);
}
