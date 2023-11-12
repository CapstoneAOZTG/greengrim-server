package com.greengrim.green.core.certification.controller;

import com.greengrim.green.common.auth.CurrentMember;
import com.greengrim.green.core.certification.service.UpdateCertificationService;
import com.greengrim.green.core.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
@RequiredArgsConstructor
public class UpdateCertificationController {

    private final UpdateCertificationService updateCertificationService;

    /**
     * [DELETE] 인증 삭제하기
     */
    @Operation(summary = "인증 삭제하기")
    @DeleteMapping("/visitor/certifications/{id}")
    public ResponseEntity<Integer> deleteCertification(
            @CurrentMember Member member,
            @PathVariable("id") Long id) {
        updateCertificationService.delete(member, id);
        return new ResponseEntity<>(200, HttpStatus.OK);
    }
}
