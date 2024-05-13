package xyz.needpainkiller.demo.lib.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.AbstractFileResolvingResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import xyz.needpainkiller.demo.lib.error.ApiErrorResponse;
import xyz.needpainkiller.demo.lib.error.BusinessException;
import xyz.needpainkiller.demo.lib.error.CommonErrorCode;
import xyz.needpainkiller.demo.lib.error.ErrorCode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * /api/v1 으로 시작하는 모든 요청에 대한 필터
 *
 * @author needpainkiller6512
 */
@Slf4j
@Component
@WebFilter
public class ApiFilter implements Filter {

    /**
     * Swagger, Actuator, Web Socket 에 대한 필터링
     */
    protected static final String[] AUTH_SWAGGER_ALLOW_LIST_PATH = {
            "/swagger", "/webjars", "/v2", "/v3"
    };
    protected static final String[] AUTH_ACTUATOR_ALLOW_LIST_PATH = {
            "/actuator"
    };
    protected static final String[] AUTH_WS_ALLOW_LIST_PATH = {
            "/ws"
    };

    /**
     * 정적 파일 캐시 설정
     */
    protected static final String[] FILE_EXTENSIONS = {"html", "js", "json", "csv", "css", "png", "svg", "eot", "ttf", "woff", "woff2", "appcache", "jpg", "jpeg", "gif", "ico"};
    protected static final String CACHE_CONTROL_HEAD = "Cache-Control";
    protected static final String CACHE_CONTROL_NO_CACHE = "no-cache, private";
    protected static final String CACHE_CONTROL_AGE_DAILY = "private, max-age=86400";
    protected static final String PRAGMA_HEAD = "Pragma";
    protected static final String PRAGMA_NO_CACHE = "no-cache";

    /**
     * 기본 base url, api prefix, resource path 설정
     */
    protected final String baseUrl;
    protected final String API_PREFIX;
    protected final String resourcePathScope;
    protected final String resourcePathRoot;

    protected ApiFilter(
            @Value("${spring.base-url}") String baseUrl,
            @Value("${api.path-prefix}") String API_PREFIX,
            @Value("${resource.scope}") String resourcePathScope,
            @Value("${resource.path}") String resourcePathRoot) {
        this.baseUrl = baseUrl;
        this.API_PREFIX = API_PREFIX;
        this.resourcePathScope = resourcePathScope;
        this.resourcePathRoot = resourcePathRoot;

        log.info("ApiFilter instance : {}", this.getClass().getName());
    }

    /**
     * 정적 타입 파일 응답시 헤더 설정
     *
     * @param resourcePath 정적 파일 경로
     * @param response     응답 객체
     */
    protected void setStaticContentHeader(String resourcePath, HttpServletResponse response) {
        /*
         * 캐시 설정
         */
        response.setHeader(CACHE_CONTROL_HEAD, CACHE_CONTROL_NO_CACHE);

        if (endsWith(resourcePath, ".html")) {
            response.setContentType(MediaType.TEXT_HTML_VALUE);
            response.setHeader(PRAGMA_HEAD, PRAGMA_NO_CACHE);
        } else if (endsWith(resourcePath, ".css")) {
            response.setContentType("text/css");
            response.setHeader(PRAGMA_HEAD, PRAGMA_NO_CACHE);
        } else if (endsWith(resourcePath, ".js")) {
            response.setContentType("text/javascript");
            response.setHeader(PRAGMA_HEAD, PRAGMA_NO_CACHE);
        } else if (endsWith(resourcePath, ".json")) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setHeader(PRAGMA_HEAD, PRAGMA_NO_CACHE);
        } else if (endsWith(resourcePath, ".jpeg", ".jpg")) {
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            response.setHeader(CACHE_CONTROL_HEAD, CACHE_CONTROL_AGE_DAILY);
        } else if (endsWith(resourcePath, ".png")) {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            response.setHeader(CACHE_CONTROL_HEAD, CACHE_CONTROL_AGE_DAILY);
        } else if (endsWith(resourcePath, ".svg")) {
            response.setContentType("image/svg+xml");
            response.setHeader(CACHE_CONTROL_HEAD, CACHE_CONTROL_AGE_DAILY);
        } else if (endsWith(resourcePath, ".webp")) {
            response.setContentType("image/webp");
            response.setHeader(CACHE_CONTROL_HEAD, CACHE_CONTROL_AGE_DAILY);
        } else if (endsWith(resourcePath, ".woff")) {
            response.setContentType("font/woff");
            response.setHeader(CACHE_CONTROL_HEAD, CACHE_CONTROL_AGE_DAILY);
        } else if (endsWith(resourcePath, ".woff2")) {
            response.setContentType("font/woff2");
            response.setHeader(CACHE_CONTROL_HEAD, CACHE_CONTROL_AGE_DAILY);
        } else if (endsWith(resourcePath, ".ttf")) {
            response.setContentType("application/x-font-ttf");
            response.setHeader(CACHE_CONTROL_HEAD, CACHE_CONTROL_AGE_DAILY);
        }
    }

    /**
     * 파일 경로가 주어진 확장자로 끝나는지 확인
     */
    protected static boolean endsWith(String requestURI, String... with) {
        return Arrays.stream(with).anyMatch(requestURI::endsWith);
    }

    /**
     * 파일 경로가 주어진 확장자로 시작하는지 확인
     */
    protected static boolean startsWith(String requestURI, String... with) {
        return Arrays.stream(with).anyMatch(requestURI::startsWith);
    }

    /**
     * Filter Chain
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        /**
         * /api/v1 으로 시작하는 모든 요청에 대해 chain.doFilter 호출
         * 에러 발생시 예외 처리
         * */
        try {
            doFilter(httpServletRequest, httpServletResponse, chain);
        } catch (BusinessException e) {
            writeResponse(httpServletResponse, e.getErrorCode(), e);
        } catch (Exception e) {
            writeResponse(httpServletResponse, CommonErrorCode.INTERNAL_SERVER_ERROR, e);
        }
        log.debug("###### ApiDispatcherServlet > doDispatch end");
    }

    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug("#### API FILTER > START");

        /*CORS*/
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Pattern", "");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Expose-Headers", "*");
        response.setHeader("Access-Control-Max-Age", "3600");

        /*XSS 방지*/
        response.setHeader("x-xss-protection", "1; mode=block");

        /*MIME 공격 방지*/
        response.setHeader("x-content-type-options", "nosniff");

        String requestURI = request.getRequestURI();
        log.debug("#### ApiFilter > getRequestURI :  {}", requestURI);
        if (requestURI.startsWith(API_PREFIX)) { // API
            log.debug("#### API FILTER > API_PREFIX");

            /*CACHE*/
            response.setHeader(CACHE_CONTROL_HEAD, CACHE_CONTROL_NO_CACHE);
            response.setHeader(PRAGMA_HEAD, PRAGMA_NO_CACHE);

            ContentCachingRequestWrapper httpServletRequest = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper httpServletResponse = new ContentCachingResponseWrapper(response);
            chain.doFilter(httpServletRequest, httpServletResponse);
            httpServletResponse.copyBodyToResponse();
        } else if (startsWith(requestURI, AUTH_SWAGGER_ALLOW_LIST_PATH)) {
            log.debug("#### API FILTER > SWAGGER");
            chain.doFilter(request, response);
        } else if (startsWith(requestURI, AUTH_ACTUATOR_ALLOW_LIST_PATH)) {
            log.debug("#### API FILTER > ACTUATOR");
            chain.doFilter(request, response);
        } else if (startsWith(requestURI, AUTH_WS_ALLOW_LIST_PATH)) {
            log.debug("#### API FILTER > WEB SOCKET");
            chain.doFilter(request, response);
        } else {
            if (request.getMethod().equalsIgnoreCase("OPTIONS")
                    || request.getMethod().equalsIgnoreCase("PUT")
                    || request.getMethod().equalsIgnoreCase("DELETE")
                    || request.getMethod().equalsIgnoreCase("TRACE")
                    || request.getMethod().equalsIgnoreCase("PATCH")
                    || request.getMethod().equalsIgnoreCase("OPTION")) {
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                response.setContentType("text/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
                log.debug("#### API FILTER > METHOD_NOT_ALLOWED / END");
                return;
            }

            if (endsWith(requestURI, FILE_EXTENSIONS)) {
                log.debug("#### API FILTER > FILE");
                resourceToResponse(requestURI, response);
            } else {
                log.debug("#### API FILTER > INDEX");
                resourceToResponse("/index.html", response);
            }
        }
        log.debug("#### API FILTER > END");
    }

    /**
     * 응답 객체에 에러 코드와 메시지를 쓰는 메소드
     *
     * @param httpServletResponse 응답 객체
     * @param errorCode           에러 코드
     * @param e                   예외 객체
     */
    protected void writeResponse(HttpServletResponse httpServletResponse, ErrorCode errorCode, Exception e) throws IOException {
        log.error(e.getMessage());
        log.error("Exception : {} ->", e.getClass().getName(), e);
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.of(errorCode, e);
        httpServletResponse.setStatus(errorCode.getStatus().value());
        httpServletResponse.setContentType("text/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(httpServletResponse);
        PrintWriter writer = contentCachingResponseWrapper.getWriter();
        writer.write(apiErrorResponse.toString());
        writer.flush();
    }

    /**
     * 정적 파일 응답
     *
     * @param resourcePath 정적 파일 경로 (resourcePathRoot 기준)
     * @param response     응답 객체
     */
    protected void resourceToResponse(String resourcePath, HttpServletResponse response) throws IOException {
        log.debug("#### resourceToResponse");
        AbstractFileResolvingResource fileResolvingResource;

        if (resourcePathScope.equals("internal")) {
            fileResolvingResource = new ClassPathResource(resourcePathRoot + resourcePath);
        } else {
            fileResolvingResource = new FileUrlResource(resourcePathRoot + resourcePath);
        }

        try (InputStream classPathResourceStream = fileResolvingResource.getInputStream()) {
            setStaticContentHeader(resourcePath, response);
            classPathResourceStream.transferTo(response.getOutputStream());
        } catch (FileNotFoundException e) {
            log.info("#### resourceToResponse > NOT_FOUND : {}", e.getMessage());
            response.sendError(NOT_FOUND.value(), NOT_FOUND.getReasonPhrase());
        }
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("#### API FILTER > INIT");
    }

    @Override
    public void destroy() {
        log.debug("#### API FILTER > DESTROY");
    }
}