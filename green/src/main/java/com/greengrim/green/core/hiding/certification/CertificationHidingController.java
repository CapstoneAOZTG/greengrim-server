package com.greengrim.green.core.hiding.certification;

import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visitor/hiding")
@Tag(name = "숨기기")
public class CertificationHidingController {

    private final CertificationHidingService certificationHidingService;

    /**
     * [POST] 인증 숨기기
     */
    @Operation(summary = "인증 숨기기")
    @PostMapping("/certification")
    public ResponseEntity<Integer> hideCertification(
            @CurrentMember Member member,
            @RequestParam("id") Long id) {
        certificationHidingService.hideCertification(member, id);
        return new ResponseEntity<>(200, HttpStatus.OK);
    }
}
