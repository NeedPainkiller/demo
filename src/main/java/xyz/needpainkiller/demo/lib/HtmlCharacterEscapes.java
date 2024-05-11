package xyz.needpainkiller.demo.lib;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.LookupTranslator;

/**
 * HTML 특수문자를 이스케이프 처리하는 클래스
 * - XSS 방지를 위해 사용
 *
 * @author needpainkiller6512
 */
public class HtmlCharacterEscapes extends CharacterEscapes {
    private final int[] asciiEscapes;
    private final CharSequenceTranslator translator;

    /**
     * HtmlCharacterEscapes 생성자
     * - XSS 방지 처리할 특수 문자 지정
     */
    public HtmlCharacterEscapes() {
        // 1. XSS 방지 처리할 특수 문자 지정
        asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
        asciiEscapes['<'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['>'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\"'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['('] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes[')'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['#'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\''] = CharacterEscapes.ESCAPE_CUSTOM;

        translator = new AggregateTranslator(
                new LookupTranslator(EntityArrays.BASIC_ESCAPE()), // <, >, &, " 는 여기에 포함됨
                new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE()),
                new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE()),
                // 여기에서 커스터마이징 가능
                new LookupTranslator(
                        new String[][]{
                                {"(", "&#40;"},
                                {")", "&#41;"},
                                {"#", "&#35;"},
                                {"'", "&#39;"}
                        }
                )
        );
    }

    @Override
    public int[] getEscapeCodesForAscii() {
        return asciiEscapes.clone();
    }

    @Override
    public SerializableString getEscapeSequence(int ch) throws ArithmeticException {
        return new SerializedString(translator.translate(Character.toString((char) ch)));
    }
}
