package ru.vogulev.sofia_wb_tg_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class SofiaWbTgBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(SofiaWbTgBotApplication.class, args);
    }

}
