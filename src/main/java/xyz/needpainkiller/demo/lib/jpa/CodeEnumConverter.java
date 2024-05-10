package xyz.needpainkiller.demo.lib.jpa;


import jakarta.persistence.AttributeConverter;

/**
 * JPA - Enum 간 값-객체 변환을 위한 인터페이스
 */
public interface CodeEnumConverter<E extends CodeEnum> extends AttributeConverter<E, Integer> {

}
