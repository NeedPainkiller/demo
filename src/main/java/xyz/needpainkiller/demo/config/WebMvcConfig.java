package xyz.needpainkiller.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.needpainkiller.demo.lib.HtmlCharacterEscapes;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * WebMvc 설정
 * WebMvcConfigurer 인터페이스를 구현하여 설정
 */
@Slf4j
@Configuration
@EnableWebMvc
@MultipartConfig
@ComponentScan
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * HttpMessageConverter 설정
     *
     * @param converters HttpMessageConverter 리스트
     *                   StringHttpMessageConverter : 문자열 변환
     *                   MappingJackson2HttpMessageConverter : JSON 변환
     *                   ObjectMapper : JSON 변환 설정
     *                   HtmlCharacterEscapes : HTML 문자 변환
     *                   converters에 추가
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        stringHttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);
        converters.add(stringHttpMessageConverter);

        ObjectMapper copy = objectMapper.copy();
        copy.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter(copy);
        converters.add(jackson2HttpMessageConverter);
    }


    /**
     * 비동기 작업 설정
     * TaskExecutor 설정
     * TaskExecutor : 비동기 작업을 위한 인터페이스
     * ThreadPoolTaskExecutor : TaskExecutor 구현체
     * corePoolSize : 기본 스레드 수
     * maxPoolSize : 최대 스레드 수
     * threadNamePrefix : 스레드 이름 접두사
     * initialize : 초기화
     * configurer.setTaskExecutor : TaskExecutor 설정
     * configurer.setDefaultTimeout : 기본 타임아웃 설정
     */
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(mvcExecutor());
        configurer.setDefaultTimeout(30000L);
    }

    private static ThreadPoolTaskExecutor mvcExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2);
        taskExecutor.setMaxPoolSize(1000);
        taskExecutor.setThreadNamePrefix("mvc-task-");
        taskExecutor.initialize();
        return taskExecutor;
    }
}
