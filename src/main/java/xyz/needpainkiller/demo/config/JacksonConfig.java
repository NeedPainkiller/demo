package xyz.needpainkiller.demo.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Jackson 설정
 * ObjectMapper 설정
 */
@Configuration
public class JacksonConfig {

    /**
     * ObjectMapper 생성
     * JSON 변환 설정
     * - NULL 값 무시
     * - 알 수 없는 속성 무시
     * - 알 수 없는 Enum 값 NULL 처리
     * - 단일 값 배열로 처리
     * - 빈 문자열 NULL 처리
     * - 빈 배열 NULL 처리
     * - Java 8 날짜 변환 설정
     *
     * @return ObjectMapper
     */
    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    /**
     * ObjectMapper Bean 등록
     *
     * @return ObjectMapper
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return createObjectMapper();
    }

}
