package com.greengrim.green.core.grim.service;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.fcm.FcmService;
import com.greengrim.green.core.grim.Grim;
import com.greengrim.green.core.grim.dto.GrimRequestDto.RegisterGrimInfo;
import com.greengrim.green.core.grim.dto.GrimResponseDto.GrimInfo;
import com.greengrim.green.core.grim.repository.GrimRepository;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.member.repository.MemberRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RegisterGrimService {

    private final WebClient webClient;
    private final GrimRepository grimRepository;
    private final FcmService fcmService;
  private final MemberRepository memberRepository;

  public void save(Grim grim) {
        grimRepository.save(grim);
    }

    public GrimInfo register(Member member, String imgUrl) {
        Grim grim = Grim.builder()
                .title("untitled")
                .imgUrl(imgUrl)
                .status(true)
                .member(member)
                .nft(null)
                .build();
        save(grim);
        return new GrimInfo(grim);
    }

    public void generateGrim(Member member, RegisterGrimInfo registerGrimInfo) {

        if (!checkGreenPoint(member)) {
            throw new BaseException(GrimErrorCode.NOT_ENOUGH_POINT);
        }

        int randomValue = (int) (Math.random() * 41) + 10;
        String prompt = registerGrimInfo.getStyle() + registerGrimInfo.getNoun() + registerGrimInfo.getVerb();
        Mono<String> resultMono = webClient.post()
                .uri("/txt2img")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "prompt", prompt,
                        "guidance_scale", 7.5,
                        "height", 512,
                        "width", 512,
                        "num_inference_steps", 50,
                        "safety_check", true,
                        "seed", randomValue
                ))
                .retrieve()
                .bodyToMono(String.class);

        resultMono.subscribe(
                result -> {
                    System.out.println("Success: " + result);
                    minusPoint(member);
                    String imgUrl = result.replace("\"", "");
                    fcmService.sendGrimGenerationSuccess(register(member, imgUrl), member.getFcmToken());
                },
                error -> {
                    System.err.println("Error: " + error.getMessage());
                    fcmService.sendGrimGenerationFail(member.getFcmToken());
                }
        );
    }

    public boolean checkGreenPoint(Member member) {
        if (member.getPoint() < 500) {
            return false;
        }
        return true;
    }

    public void minusPoint(Member member) {
        member.minusPoint(500);
        memberRepository.save(member);
    }
}
