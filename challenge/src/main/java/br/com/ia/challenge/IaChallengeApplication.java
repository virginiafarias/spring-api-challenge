package br.com.ia.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class IaChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(IaChallengeApplication.class, args);
	}

}
