package com.greengrim.green.core.hiding.nft;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NftHidingRepository extends JpaRepository<NftHiding, Long> {

    Optional<NftHiding> findByMemberIdAndNftId(Long memberId, Long nftId);
}
