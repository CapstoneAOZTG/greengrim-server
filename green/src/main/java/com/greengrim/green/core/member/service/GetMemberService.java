package com.greengrim.green.core.member.service;

import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.member.dto.MemberResponseDto.MemberSimpleInfo;
import com.greengrim.green.core.member.repository.MemberRepository;
import com.greengrim.green.core.member.usecase.GetMemberUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMemberService implements GetMemberUseCase {

    private final MemberRepository memberRepository;

    public MemberSimpleInfo getMemberInfo(Member member) {
        return new MemberSimpleInfo(member);
    }
}