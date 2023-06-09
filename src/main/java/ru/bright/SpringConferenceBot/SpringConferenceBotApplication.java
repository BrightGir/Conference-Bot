package ru.bright.SpringConferenceBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan("ru.bright")
@EnableScheduling
public class SpringConferenceBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringConferenceBotApplication.class, args);
	}

}
