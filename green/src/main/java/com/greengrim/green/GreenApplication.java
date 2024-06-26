package com.greengrim.green;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class GreenApplication {

	@PostConstruct
	public void started() {
		// timezone KST 셋팅
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}


	public static void main(String[] args) {
		SpringApplication.run(GreenApplication.class, args);
	}

}
