package com.greengrim.green.core.grim.service;

import static com.greengrim.green.common.util.UtilService.checkIsMine;
import static com.greengrim.green.common.util.UtilService.getPageable;

import com.greengrim.green.common.entity.SortOption;
import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.core.grim.Grim;
import com.greengrim.green.core.grim.dto.GrimResponseDto.GrimDetailInfo;
import com.greengrim.green.core.grim.dto.GrimResponseDto.GrimInfo;
import com.greengrim.green.core.grim.dto.GrimResponseDto.GrimSimpleInfo;
import com.greengrim.green.core.grim.repository.GrimRepository;
import com.greengrim.green.core.member.Member;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetGrimService {

    private final GrimRepository grimRepository;

    public PageResponseDto<List<GrimInfo>> getGrims(Member member, int page, int size, SortOption sortOption) {
        // TODO: 차단 목록 제외하기
        Page<Grim> grims = grimRepository.findAllStatusIsTrue(getPageable(page, size, sortOption));
        return pagingGrim(grims);
    }

    public PageResponseDto<List<GrimInfo>> getMyGrims(Member member, int page, int size, SortOption sortOption) {
        Page<Grim> grims = grimRepository.findByMemberStatusIsTrue(member, getPageable(page, size, sortOption));
        return pagingGrim(grims);
    }

    public PageResponseDto<List<GrimSimpleInfo>> getMyGrimsForNft(Member member, int page, int size, SortOption sortOption) {
        Page<Grim> grims = grimRepository.findByMemberStatusIsTrue(member, getPageable(page, size, sortOption));
        List<GrimSimpleInfo> grimSimpleInfos = new ArrayList<>();
        grims.forEach(grim -> grimSimpleInfos.add(new GrimSimpleInfo(grim)));
        return new PageResponseDto<>(grims.getNumber(), grims.hasNext(), grimSimpleInfos);
    }

    public GrimDetailInfo getGrimDetailInfo(Member member, Long id) {
        Grim grim = grimRepository.findByIdAndStatusIsTrue(id)
                .orElseThrow(() -> new BaseException(GrimErrorCode.EMPTY_GRIM));
        boolean isMine = false;
        if(member!= null) { // 로그인 했다면
            isMine = checkIsMine(member.getId(), grim.getMember().getId());
        }
        return new GrimDetailInfo(grim, isMine);
    }

    private PageResponseDto<List<GrimInfo>> pagingGrim(Page<Grim> grims) {
        List<GrimInfo> grimInfos = new ArrayList<>();
        grims.forEach(grim -> grimInfos.add(new GrimInfo(grim)));
        return new PageResponseDto<>(grims.getNumber(), grims.hasNext(), grimInfos);
    }

}
