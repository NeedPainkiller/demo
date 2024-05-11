package xyz.needpainkiller.demo.lib.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;


/**
 * 특수문자 사용을 금지하는 어노테이션
 *
 * @author needpainkiller6512
 */
@Constraint(validatedBy = {SpecialCharValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NonSpecialCharacter {
    Pattern pattern = Pattern.compile("[^ㄱ-ㅎ가-힣A-Za-z0-9 \\-!?,./{}\\[\\]()_]");

    String message() default "Special Character not Allowed at this parameter";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
