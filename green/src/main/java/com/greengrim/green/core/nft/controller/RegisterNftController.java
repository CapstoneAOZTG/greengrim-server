package com.greengrim.green.core.nft.controller;

import com.greengrim.green.core.nft.service.RegisterNftService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterNftController {

    private final RegisterNftService registerNftService;

}
