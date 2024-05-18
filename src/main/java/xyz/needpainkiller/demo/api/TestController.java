package xyz.needpainkiller.demo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

/**
 * 테스트 컨트롤러
 *
 * @author needpainkiller6512
 */
@Tag(name = "TEST", description = "TEST")
@RequestMapping(value = "/api/v1/test", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController extends CommonController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    /**
     * 패스워드 키 값 생성 테스트 용 엔드포인트
     */
    @GetMapping(value = "/passwd")
    @Operation(description = "암호 키 생성")
    public ResponseEntity<Map<String, Object>> passwd(@RequestParam("payload") String payload, HttpServletRequest request) {
        Map<String, Object> model = new HashMap<>();
        String passwd = bCryptPasswordEncoder.encode(payload);
        model.put("passwd", passwd);
        return ok(model);
    }

}