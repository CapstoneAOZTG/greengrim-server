package com.greengrim.green.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@RequiredArgsConstructor
@Configuration
public class TraitsConfig {

    @Bean
    public static String[][] traits() {
        String[][] traits = {
            {"black", "white", "red", "blue"}, // background
            {"short weed", "long weed", "flower", "tree"}, // hair
            {"smile", "sad", "angry", "surprise"}, // face
            {"peace sign", "thumbs up", "ok", "salute"}, // gesture
            {"black", "white", "red", "blue"}, // accessory
            {"black", "white", "red", "blue"}  // shoes
        };
        return traits;
    }

}
