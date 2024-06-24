package com.greengrim.green.core.certification.controller;

import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.certification.dto.CertificationRequestDto.RegisterCertification;
import com.greengrim.green.core.certification.dto.CertificationResponseDto.registerCertificationResponse;
import com.greengrim.green.core.certification.service.RegisterCertificationService;
import com.greengrim.green.core.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
@RequiredArgsConstructor
@RequestMapping("/visitor/certifications")
@Tag(name = "인증")
public class RegisterCertificationController {

    private final RegisterCertificationService registerCertificationService;

    /**
     * [POST] 인증하기
     */
    @Operation(summary = "인증하기")
    @PostMapping
    public ResponseEntity<registerCertificationResponse> registerCertification(
            @CurrentMember Member member,
            @Valid @RequestBody RegisterCertification registerCertification) {
        return ResponseEntity.ok(registerCertificationService.register(member, registerCertification));
    }
}
