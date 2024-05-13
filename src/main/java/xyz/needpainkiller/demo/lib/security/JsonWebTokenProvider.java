package xyz.needpainkiller.demo.lib.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.needpainkiller.demo.api.user.model.Role;
import xyz.needpainkiller.demo.api.user.model.User;
import xyz.needpainkiller.demo.lib.security.error.TokenValidFailedException;

import javax.crypto.SecretKey;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static xyz.needpainkiller.demo.lib.error.CommonErrorCode.*;


/**
 * Json Web Token Provider
 * - JWT 토큰 생성, 검증, 파싱
 * - JWT 토큰에 담긴 정보를 추출
 * - JWT 토큰의 만료시간, 발급시간 추출
 *
 * @author needpainkiller6512
 */
@Slf4j
@Component
public class JsonWebTokenProvider {

    /**
     * Json Web Token 이 담길 Header Key
     */
    public static final String BEARER_TOKEN_HEADER = "X-Authorization";

    /**
     * 토큰의 claim key
     */
    protected static final String KEY_USER_PK = "user-pk";
    protected static final String KEY_USER_ID = "user-id";
    protected static final String KEY_USER_NAME = "user-name";
    protected static final String KEY_USER_EMAIL = "user-email";
    protected static final String KEY_ROLE_LIST = "role-list";

    /**
     * JsonWebTokenSecretKeyManager 에서 주입받을 SecretKey
     */
    protected final SecretKey secretKey;

    /**
     * 토큰 만료시간
     */
    protected final long expireTime;


    public JsonWebTokenProvider(
            @Autowired JsonWebTokenSecretKeyManager secretKey,
            @Value("${jwt.expire-time-ms}") long expireTime) {
        this.secretKey = secretKey.getSecretKey();
        this.expireTime = expireTime;
    }

    /**
     * Token 생성 및 Claim 설정
     *
     * @param user  User
     *              - 토큰에 담길 User 정보
     * @param roles List<Role>
     *              - 토큰에 담길 Role 정보
     */
    public String createToken(User user, List<Role> roles) {
        try {
            Long userPk = user.getId();
            String userId = user.getUserId();
            List<String> roleList = roles.stream().map(Role::getRoleName).toList();
            Claims claims = Jwts.claims().setSubject(userId);
            claims.put(KEY_USER_PK, userPk);
            claims.put(KEY_USER_ID, userId);
            claims.put(KEY_USER_NAME, user.getUserName());
            claims.put(KEY_USER_EMAIL, user.getUserEmail());
            claims.put(KEY_ROLE_LIST, roleList);
            String token = generateToken(claims);
            return token;
        } catch (Exception e) {
            throw new TokenValidFailedException(TOKEN_FAILED_CREATE, e.getMessage());
        }
    }

    /**
     * Token 생성
     *
     * @param claims 토큰에 담길 정보
     * @return 생성된 토큰
     */
    protected String generateToken(Claims claims) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validity = LocalDateTime.now().plus(expireTime, ChronoUnit.MILLIS);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Timestamp.valueOf(now))
                .setExpiration(Timestamp.valueOf(validity))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Request Header 에서 Token 추출
     *
     * @param request HttpServletRequest
     * @return 추출된 Token
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(BEARER_TOKEN_HEADER);
        if (Strings.isBlank(bearerToken)) {
            throw new TokenValidFailedException(TOKEN_HEADER_MUST_REQUIRED);
        }
        return bearerToken;
    }

    /**
     * Request Header 에서 Token 추출 후 검증
     *
     * @param request HttpServletRequest
     */
    public void validateToken(HttpServletRequest request) throws TokenValidFailedException {
        String token = resolveToken(request);
        validateToken(token);
    }

    /**
     * Token 검증
     *
     * @param token 토큰 문자열
     * @throws TokenValidFailedException 토큰 검증 실패시 예외 발생
     */
    public void validateToken(String token) throws TokenValidFailedException {
        if (Strings.isBlank(token)) {
            throw new TokenValidFailedException(TOKEN_MUST_REQUIRED);
        }
        if (getExpirationDate(token).before(new Date())) {  // 요청 Token 의 만료기한이 지난경우
            throw new TokenValidFailedException(TOKEN_EXPIRED);
        }
    }

    /**
     * 토큰의 UserPk 추출
     *
     * @param token 토큰 문자열
     * @return UserPk
     */
    public Long getUserPk(String token) {
        Object claim = getClaimFromToken(token, claims -> claims.get(KEY_USER_PK));
        if (claim == null) {
            throw new TokenValidFailedException(TOKEN_CLAIM_USER_NOT_EXIST);
        }
        return ((Integer) claim).longValue();
    }

    /**
     * 토큰의 UserId 추출
     *
     * @param token 토큰 문자열
     * @return UserId
     */
    public String getUserId(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 토큰의 UserName 추출
     *
     * @param token 토큰 문자열
     * @return UserName
     * @throws TokenValidFailedException 토큰 내 UserName 정보가 없을 경우 예외 발생
     */
    public String getUserName(String token) {
        Object claim = getClaimFromToken(token, claims -> claims.get(KEY_USER_NAME));
        if (claim == null) {
            throw new TokenValidFailedException(TOKEN_CLAIM_PARSE_FAILED);
        }
        return claim.toString();
    }

    /**
     * 토큰의 UserEmail 추출
     *
     * @param token 토큰 문자열
     * @return UserEmail
     * @throws TokenValidFailedException 토큰 내 UserEmail 정보가 없을 경우 예외 발생
     */
    public String getUserEmail(String token) {
        Object claim = getClaimFromToken(token, claims -> claims.get(KEY_USER_EMAIL));
        if (claim == null) {
            throw new TokenValidFailedException(TOKEN_CLAIM_PARSE_FAILED);
        }
        return claim.toString();
    }

    /**
     * 토큰의 권한 리스트 추출
     *
     * @param request HttpServletRequest
     * @return 권한 리스트
     * @throws TokenValidFailedException 토큰 내 권한 정보가 없을 경우 예외 발생
     */
    public List<String> getRole(HttpServletRequest request) {
        return getRole(resolveToken(request));
    }

    /**
     * 토큰의 권한 리스트 추출
     *
     * @param token 토큰 문자열
     * @return 권한 리스트
     * @throws TokenValidFailedException 토큰 내 권한 정보가 없을 경우 예외 발생
     */
    public List<String> getRole(String token) {
        List<String> authorityList = (List<String>) getClaimFromToken(token, claims -> claims.get(KEY_ROLE_LIST));
        if (authorityList == null || authorityList.isEmpty()) {
            throw new TokenValidFailedException(TOKEN_CLAIM_AUTHORITY_NOT_EXIST);
        }
        return authorityList;
    }

    public Date getIssuedAtDate(HttpServletRequest request) {
        String token = resolveToken(request);
        return getIssuedAtDate(token);
    }


    public Date getExpirationDate(HttpServletRequest request) {
        String token = resolveToken(request);
        return getExpirationDate(token);
    }


    /**
     * 토큰의 발급시간 추출
     *
     * @param token 토큰 문자열
     * @return 발급시간
     */
    public Date getIssuedAtDate(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    /**
     * 토큰의 만료시간 추출
     *
     * @param token 토큰 문자열
     * @return 만료시간
     */
    public Date getExpirationDate(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 토큰의 Claim 추출
     *
     * @param token          토큰 문자열
     * @param claimsResolver Claim 추출 함수
     * @param <T>            Claim Type
     * @return Claim
     * @throws TokenValidFailedException 토큰 검증 실패시 예외 발생
     */
    protected <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build()
                    .parseClaimsJws(token).getBody();
            return claimsResolver.apply(claims);
        } catch (ExpiredJwtException e) {
            throw new TokenValidFailedException(TOKEN_EXPIRED, e.getMessage());
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new TokenValidFailedException(TOKEN_CLAIM_PARSE_FAILED, e.getMessage());
        }
    }
}