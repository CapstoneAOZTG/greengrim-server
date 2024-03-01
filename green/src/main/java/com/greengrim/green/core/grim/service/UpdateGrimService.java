package com.greengrim.green.core.grim.service;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.core.grim.Grim;
import com.greengrim.green.core.grim.dto.GrimRequestDto.UpdateTitle;
import com.greengrim.green.core.grim.repository.GrimRepository;
import com.greengrim.green.core.member.Member;
import jakarta.transaction.Transactional;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class UpdateGrimService {

    private final GrimRepository grimRepository;

    public void updateTitle(Member member, UpdateTitle updateTitle) {
        Grim grim = grimRepository.findByIdAndStatusIsTrue(updateTitle.getId())
                .orElseThrow(() -> new BaseException(GrimErrorCode.EMPTY_GRIM));

        validateAuthorization(member, grim);

        grim.setTitle(updateTitle.getTitle());
    }

    private void validateAuthorization(Member member, Grim grim) {
        if(!checkIsMine(member.getId(), grim.getMember().getId())) {
            throw new BaseException(GrimErrorCode.NO_AUTHORIZATION);
        }
    }
    private boolean checkIsMine(Long memberId, Long ownerId) {
        return Objects.equals(memberId, ownerId);
    }
}
