package com.greengrim.green.core.verification.controller;

import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.verification.service.VerificationService;
import com.greengrim.green.core.verification.dto.VerificationRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visitor/verifications")
@Tag(name = "상호 검증")
public class VerificationController {

    private final VerificationService verificationService;

    /**
     * [POST] 상호 검증하기
     */
    @Operation(summary = "상호 검증하기 - 출석체크")
    @PostMapping
    public ResponseEntity<Integer> registerVerification(
            @CurrentMember Member member,
            @Valid @RequestBody VerificationRequestDto.RegisterVerification registerVerification) {
        verificationService.register(member, registerVerification);
        return new ResponseEntity<>(200, HttpStatus.OK);
    }
}
