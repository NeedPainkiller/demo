package xyz.needpainkiller.demo.lib.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 특수문자 사용을 금지하는 어노테이션 유효성 검사 클래스
 *
 * @author needpainkiller6512
 */
@Slf4j
public class SpecialCharValidator implements ConstraintValidator<NonSpecialCharacter, String> {
    private Pattern pattern;

    @Override
    public void initialize(NonSpecialCharacter constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Matcher m = pattern.matcher(value);
        boolean isValid = m.find();
        return !isValid;
    }

}