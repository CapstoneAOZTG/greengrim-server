package com.greengrim.green.core.hiding.member;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.HidingErrorCode;
import com.greengrim.green.common.exception.errorCode.MemberErrorCode;
import com.greengrim.green.core.member.entity.Member;
import com.greengrim.green.core.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberHidingService {

    private final MemberHidingRepository memberHidingRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void register(Long memberId, Member hiddenMember) {
        memberHidingRepository.save(
                MemberHiding.builder()
                        .memberId(memberId)
                        .hiddenMember(hiddenMember)
                        .build());
    }

    @Transactional
    public void hideMember(Member member, Long hiddenMemberId) {
        Member hiddenMember = checkMemberValidation(hiddenMemberId);
        // DB 에 존재하지 않으면 객체 생성
        if (checkNonExistingHiding(member.getId(), hiddenMember)) {
            register(member.getId(), hiddenMember);
        } else { // 존재하면 에러
            throw new BaseException(HidingErrorCode.ALREADY_HIDING);
        }
    }

    private Member checkMemberValidation(Long hiddenMemberId) {
        return memberRepository.findByIdAndStatusTrue(hiddenMemberId)
                .orElseThrow(() -> new BaseException(MemberErrorCode.EMPTY_MEMBER));
    }

    private boolean checkNonExistingHiding(Long memberId, Member hiddenMember) {
        return memberHidingRepository.findByMemberIdAndHiddenMember(memberId, hiddenMember).isEmpty();
    }
}
