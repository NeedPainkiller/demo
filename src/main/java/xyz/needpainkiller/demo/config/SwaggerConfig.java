package xyz.needpainkiller.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

/**
 * Swagger 설정
 */
@Slf4j
@Configuration
public class SwaggerConfig {

    /**
     *  Swagger 설정
     *  - LocalDateTime, LocalDate, LocalTime, ZonedDateTime -> String 변환
     *  - version : application.yml에서 설정
     */
    static {
        SpringDocUtils.getConfig()
                .replaceWithClass(LocalDateTime.class, String.class)
                .replaceWithClass(LocalDate.class, String.class)
                .replaceWithClass(LocalTime.class, String.class)
                .replaceWithClass(ZonedDateTime.class, String.class);
    }

    @Value("${version}")
    private String version;

    /**
     * API 정보
     * - title : Backend API Server
     * - description : Backend API Server
     * - version : application.yml에서 설정
     * - contact : Painkiller
     *
     * @return Info
     */
    private Info apiInfo() {
        Contact contact = new Contact().name("Painkiller").email("painkiller@gmail.com").url("https://home.painkiller.xyz/");
        return new Info()
                .title("Backend API Server")
                .description("Backend API Server")
                .version(version)
                .contact(contact);
    }

    /**
     * Root API 설정
     * - X-Authorization : JWT
     *
     * @return OpenAPI
     */
    @Bean
    public OpenAPI rootApi() {
        final String securitySchemeName = "X-Authorization";

        SecurityScheme jwtSecurityScheme = new SecurityScheme()
                .name(securitySchemeName)
                .in(SecurityScheme.In.HEADER)
                .type(SecurityScheme.Type.APIKEY);
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securitySchemeName);

        return new OpenAPI()
                .info(apiInfo())
                .components(new Components().addSecuritySchemes(securitySchemeName, jwtSecurityScheme))
                .addSecurityItem(securityRequirement)
                ;
    }

    /**
     * v1 API 설정
     * - /api/v1/** 경로 설정
     *
     * @return GroupedOpenApi
     */
    @Bean
    public GroupedOpenApi v1() {
        return GroupedOpenApi.builder()
                .group("v1").displayName("v1")
                .pathsToMatch("/api/v1/**")
                .build();
    }
}
