package xyz.needpainkiller.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import xyz.needpainkiller.demo.api.auth.service.AuthenticationService;
import xyz.needpainkiller.demo.lib.filter.ApiFilter;


/**
 * WebSecurity 설정
 * WebSecurityCustomizer 인터페이스를 구현하여 설정
 *
 * @author needpainkiller6612
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Value("${server.ssl.enabled}")
    private String sslEnabled;
    @Value("${spring.base-url}")
    private String baseUrl;
    @Value("${api.path-pattern}")
    private String API_PATTERN;

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ApiFilter apiFilter;


    /**
     * httpSessionEventPublisher
     * - HttpSessionEventPublisher 빈 생성
     * - HttpSessionEventPublisher 를 통해 HttpSessionEventPublisher 설정
     * - HttpSessionEventPublisher 설정을 통해 HttpSessionEventPublisher 빈을 등록
     */
    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }

    /**
     * authenticationManagerBean
     * - AuthenticationManager 빈 생성
     * - AuthenticationManagerBuilder 를 통해 UserDetailsService 와 PasswordEncoder 설정
     * - authenticationService : UserDetailsService 설정
     * - bCryptPasswordEncoder : PasswordEncoder 설정
     * - authenticationManagerBuilder : AuthenticationManagerBuilder 빌드
     * - authenticationManagerBuilder.build() : AuthenticationManager 반환
     */
    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(authenticationService).passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }

    /**
     * corsConfigurationSource
     * - CorsConfigurationSource 를 통해 CorsConfiguration 설정
     * - CorsConfiguration 설정을 통해 CORS 설정을 변경
     * - addAllowedOriginPattern : 허용 Origin 설정
     * - addAllowedHeader : 허용 Header 설정
     * - addAllowedMethod : 허용 Method 설정
     * - setMaxAge : MaxAge 설정
     * - setAllowCredentials : AllowCredentials 설정
     * - UrlBasedCorsConfigurationSource : UrlBasedCorsConfigurationSource 빌드
     * - source : UrlBasedCorsConfigurationSource 반환
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern(baseUrl);
        configuration.addAllowedOriginPattern("http://localhost:8080");

        configuration.addAllowedHeader("*");

        configuration.addAllowedMethod(HttpMethod.GET);
        configuration.addAllowedMethod(HttpMethod.PUT);
        configuration.addAllowedMethod(HttpMethod.POST);
        configuration.addAllowedMethod(HttpMethod.DELETE);
        configuration.addAllowedMethod(HttpMethod.OPTIONS);

        configuration.setMaxAge(60L);
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }


    /**
     * SecurityFilterChain
     * - SecurityFilterChain 를 통해 HttpSecurity 설정
     * - HttpSecurity 설정을 통해 보안 설정을 변경
     * - requiresChannel : SSL 설정
     * - sessionManagement : 세션 설정
     * - formLogin, httpBasic : 기본 제공 Login 폼 비활성화
     * - csrf : CSRF 설정
     * - headers : X-FRAME-ORIGIN 설정
     * - cors : CORS 설정
     * - exceptionHandling : 예외 처리 설정
     * - authorizeHttpRequests : API_PATH 를 통해 request 되는 요청은 CSRF 프로텍션을 적용함
     * - addFilterBefore : ApiFilter 를 UsernamePasswordAuthenticationFilter 전에 등록
     * - filterChain : SecurityFilterChain 빌드
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        if (sslEnabled.equalsIgnoreCase("true")) {
            http.requiresChannel(channelRequestMatcherRegistry -> channelRequestMatcherRegistry.anyRequest().requiresSecure());
        }
        HttpSecurity httpSecurity = http
                /*
                 * 세션기반 인증 비활성화
                 * */
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                /*
                 * 기본 제공 Login 폼 비활성화
                 * HTTP Basic Authentication 비활성화 */
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                /*
                 * anonymous 를 비활성화 할 경우 JwtAuthenticationFilter 에서 등록되지 않은 SecurityContext 로 인해
                 * AuthenticationCredentialsNotFoundException 를 발생시켜 authenticationEntryPoint 이 호출됨
                 */
//                        .anonymous(AbstractHttpConfigurer::disable)
                /*
                 * API_PATH 를 통해 request 되는 요청은 CSRF 프로텍션을 적용함
                 * 하지만 JWT 인증을 사용하고 있으므로  CSRF 토큰을 사용하기 어려움
                 * 대신 ApiFilter.java 에서 Request Header 의 Referer 를 통해 요청정보를 검증하도록 함
                 */
                .csrf(AbstractHttpConfigurer::disable)
                /*
                 * X-FRAME-ORIGIN 외 응답 Header 처리는 ApiFilter.java 같이 참조
                 */
                .headers(httpHeadersConfigurer -> httpHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .cors(httpCorsConfigurer -> httpCorsConfigurer.configurationSource(corsConfigurationSource()))
                /*
                 *  JwtAuthenticationFilter.java 에서 SecurityContext 에 등록이 되지 않은 경우 발생
                 *  Anonymous 를 비활성화 하였을때 사용함
                 */
//                        .exceptionHandling(httpExceptionHandlingConfigurer -> httpExceptionHandlingConfigurer.authenticationEntryPoint(authenticationErrorHandler).accessDeniedHandler(jwtAccessDeniedHandler))
                .authorizeHttpRequests(authorize -> {
                    /*
                     * SWAGGER 및 Resource 접근 허용
                     */
                    authorize.requestMatchers(
                                    // actuator
                                    new AntPathRequestMatcher("/actuator/**"),
                                    // swagger
                                    new AntPathRequestMatcher("/swagger-ui/**"),
                                    new AntPathRequestMatcher("/swagger.json"),
                                    new AntPathRequestMatcher("/swagger-resources/**"),
                                    new AntPathRequestMatcher("/swagger"),
                                    new AntPathRequestMatcher("/swagger**/**"),
                                    new AntPathRequestMatcher("/swagger-ui.html"),
                                    new AntPathRequestMatcher("/webjars/**"),
                                    new AntPathRequestMatcher("/v2/**"),
                                    new AntPathRequestMatcher("/v3/**"),
                                    // static
                                    new AntPathRequestMatcher("/WEB-INF/**"),
                                    new AntPathRequestMatcher("/web/**"),
                                    new AntPathRequestMatcher("/css/**"),
                                    new AntPathRequestMatcher("/js/**"),
                                    new AntPathRequestMatcher("/img/**"),
                                    new AntPathRequestMatcher("/view/**"),
                                    new AntPathRequestMatcher("/media/**"),
                                    new AntPathRequestMatcher("/static/**"),
                                    new AntPathRequestMatcher("/resources/**"),
                                    new AntPathRequestMatcher("/favicon.ico"),
                                    // robots.txt
                                    new AntPathRequestMatcher("/robots.txt")
                            ).permitAll()
                            /*
                             * 계정 / 권한 별 접근 권한을 추가 할 경우 authorizationChecker 를 구현할 것
                             * authorizationChecker.java 의 check 메서드에서 API 접근 가능 여부 확인
                             */
                            .requestMatchers(new AntPathRequestMatcher(API_PATTERN)).permitAll();
//                            .requestMatchers(new AntPathRequestMatcher(API_PATTERN)).access(authorizationChecker);

                });
        /*
         * ApiFilter 를 UsernamePasswordAuthenticationFilter 전에 등록
         * 인증 처리를 하기전에 필터를 거치도록 설정
         */
        httpSecurity.addFilterBefore(apiFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();

    }


    /**
     * WebSecurityCustomizer
     * - WebSecurityCustomizer 를 통해 HttpFirewall 설정
     * - HttpFirewall 설정을 통해 특정 요청에 대한 보안 설정을 변경
     * - ignore 요청을 통해 아래 링크 접근 허용
     */
    @Bean
    public WebSecurityCustomizer WebSecurityCustomizer() {
        return web -> web.httpFirewall(defaultHttpFirewall())
                .ignoring().requestMatchers(
                        // actuator
                        new AntPathRequestMatcher("/actuator/**"),
                        // swagger
                        new AntPathRequestMatcher("/swagger-ui/**"),
                        new AntPathRequestMatcher("/swagger.json"),
                        new AntPathRequestMatcher("/swagger-resources/**"),
                        new AntPathRequestMatcher("/swagger"),
                        new AntPathRequestMatcher("/swagger**/**"),
                        new AntPathRequestMatcher("/swagger-ui.html"),
                        new AntPathRequestMatcher("/webjars/**"),
                        new AntPathRequestMatcher("/v2/**"),
                        new AntPathRequestMatcher("/v3/**"),
                        new AntPathRequestMatcher("/WEB-INF/**"),
                        // static
                        new AntPathRequestMatcher("/web/**"),
                        new AntPathRequestMatcher("/css/**"),
                        new AntPathRequestMatcher("/js/**"),
                        new AntPathRequestMatcher("/img/**"),
                        new AntPathRequestMatcher("/view/**"),
                        new AntPathRequestMatcher("/media/**"),
                        new AntPathRequestMatcher("/static/**"),
                        new AntPathRequestMatcher("/resources/**"),
                        new AntPathRequestMatcher("/favicon.ico"),
                        // robots.txt
                        new AntPathRequestMatcher("/robots.txt")
                );
    }
}
