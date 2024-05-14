package xyz.needpainkiller.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * PasswordEncoder 설정
 * BCryptPasswordEncoder 설정
 */
@Slf4j
@Configuration
public class PasswordEncoderConfig {
    /**
     * BCryptPasswordEncoder 생성
     * strength : 10
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        int strength = 10;
        return new BCryptPasswordEncoder(strength);
    }
}
