package xyz.needpainkiller.demo.lib.security;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Json Web Token Secret Key Manager
 * <p>
 * - JWT Secret Key를 관리하는 클래스
 */
@Getter
@Slf4j
@Component
public class JsonWebTokenSecretKeyManager {

    private final SecretKey secretKey;

    public JsonWebTokenSecretKeyManager(@Value("${jwt.secret-key}") String secretKeyStr) {
        if (secretKeyStr == null || secretKeyStr.isEmpty()) {
            throw new IllegalArgumentException("jwt.secret-key is empty");
        }
        String base64EncodedSecretKey = Base64.getEncoder().encodeToString(secretKeyStr.getBytes(StandardCharsets.UTF_8));
        this.secretKey = Keys.hmacShaKeyFor(base64EncodedSecretKey.getBytes(StandardCharsets.UTF_8));
    }
}