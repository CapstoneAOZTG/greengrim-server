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
            {"Purple", "Lime green", "Orange", "Blue", "Gray"}, // background
            {"Seedling", "Dandelion", "Flower", "Weed"}, // hair
            {"Basic", "Runny nose", "Blush"}, // face
            {"Basic", "Thumbs up", "Peace", "V"}, // gesture
            {"Basic", "Black", "Yellow", "White", "Blue", "Red"},  // shoes
            {"Basic", "Ear Phone", "Black Watch", "Gold Watch", "Red Glasses", "Purple Glasses"}, // accessory
            {"BASIC", "STANDARD", "PREMIUM"} // rarity
        };
        return traits;
    }

}
