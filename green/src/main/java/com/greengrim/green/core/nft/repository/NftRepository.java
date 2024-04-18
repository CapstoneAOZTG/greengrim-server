package com.greengrim.green.core.nft.repository;

import com.greengrim.green.core.nft.Nft;
import com.greengrim.green.core.nft.NftGrade;
import java.util.List;
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

    @Query("SELECT n.grade, COUNT(n) FROM Nft n WHERE n.member IS NULL "
        + "AND n.status = true "
        + "GROUP BY n.grade ")
    List<Object[]> countNftsByGrade();

    @Query(value = "SELECT n FROM Nft n WHERE n.grade = :grade "
        + "AND n.status = true "
        + "AND n.member IS NULL ")
    Page<Nft> findCollectionNfts(NftGrade grade, Pageable pageable);

    @Query(value = "SELECT n FROM Nft n WHERE n.grade = :grade "
        + "AND n.member IS NULL "
        + "AND n.status = true "
        + "ORDER BY RAND() LIMIT 1")
    Optional<Nft> findRandomByGrade(NftGrade grade);

    @Query(value = "SELECT n FROM Nft n WHERE n.grade = :grade "
        + "AND n.member IS NULL "
        + "AND n.status = true "
        + "AND n.id NOT IN :nftList "
        + "ORDER BY RAND() LIMIT 1")
    Optional<Nft> findRandomByGradeExceptList(NftGrade grade, List<Long> nftList);

    @Query(value = "SELECT n "
            + "FROM Nft n "
            + "LEFT JOIN MemberHiding mh ON n.member = mh.hiddenMember AND mh.memberId = :memberId "
            + "LEFT JOIN NftHiding nh ON n.id = nh.nftId AND nh.memberId = :memberId "
            + "WHERE n.status = true "
            + "AND n.member IS NOT NULL "
            + "AND mh.hiddenMember IS NULL "
            + "AND nh.nftId IS NULL "
            + "AND n.status = true")
    Page<Nft> findExchangedNfts(@Param("memberId") Long memberId, Pageable pageable);

    @Query(value = "SELECT n "
            + "FROM Nft n "
            + "LEFT JOIN MemberHiding mh ON n.member = mh.hiddenMember AND mh.memberId = :memberId "
            + "LEFT JOIN NftHiding nh ON n.id = nh.nftId AND nh.memberId = :memberId "
            + "WHERE n.status = true "
            + "AND n.member IS NOT NULL "
            + "AND mh.hiddenMember IS NULL "
            + "AND n.member.id=:targetId "
            + "AND n.status = true")
    Page<Nft> findMemberNfts(@Param("memberId") Long memberId,
                             @Param("targetId") Long targetId,
                             Pageable pageable);

}
