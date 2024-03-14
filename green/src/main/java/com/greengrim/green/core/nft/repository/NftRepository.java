package com.greengrim.green.core.nft.repository;

import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.nft.Nft;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NftRepository extends JpaRepository<Nft, Long> {

    Optional<Nft> findByIdAndStatusTrue(Long id);

    @Query(value = "SELECT n FROM Nft n WHERE n.id =:id AND n.member =:member AND n.status = true")
    Optional<Nft> findByIdAndMemberAndStatusTrue(@Param("member") Member member, Long id);

    @Query(value = "SELECT n FROM Nft n WHERE n.status=true")
    Page<Nft> findHomeNfts(Pageable pageable);

    @Query(value = "SELECT n FROM Nft n WHERE n.status=true AND n.member=:member")
    Page<Nft> findMyNfts(@Param("member") Member member, Pageable pageable);
}
