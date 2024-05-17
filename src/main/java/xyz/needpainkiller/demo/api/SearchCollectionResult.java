package xyz.needpainkiller.demo.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * 검색 결과 컬렉션
 *
 * @param <E> 컬렉션 요소 타입
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchCollectionResult<E extends Serializable> implements Serializable {

    @Serial
    private static final long serialVersionUID = -7372386524381520007L;
    private Collection<E> collection;
    private Long foundRows;

    /**
     * 컬렉션 요소 반환
     *
     * @return 컬렉션 요소
     */
    public Collection<E> getCollection() {
        if (collection == null || collection.isEmpty()) {
            return Collections.emptyList();
        }
        return collection;
    }

    /**
     * 컬렉션 길이 반환
     *
     * @return 컬렉션 길이
     */
    public Long getFoundRows() {
        return Math.max(foundRows, 0);
    }


    /**
     * 빌더
     *
     * @param <E> 컬렉션 요소 타입
     * @return 빌더
     */
    public static <E extends Serializable> SearchCollectionResultBuilder<E> builder() {
        return new SearchCollectionResultBuilder<>();
    }

    /**
     * 검색 결과 컬렉션 빌더
     *
     * @param <E> 컬렉션 요소 타입
     */
    public static class SearchCollectionResultBuilder<E extends Serializable> {
        private Collection<E> collection;
        private Long foundRows;

        SearchCollectionResultBuilder() {
        }

        /**
         * 컬렉션 요소 설정
         */
        public SearchCollectionResultBuilder<E> collection(Collection<E> collection) {
            this.collection = collection;
            return this;
        }

        /**
         * 컬렉션 길이 설정
         */
        public SearchCollectionResultBuilder<E> foundRows(Long foundRows) {
            this.foundRows = Objects.requireNonNullElse(foundRows, 0L);
            return this;
        }

        /**
         * 컬렉션 길이 설정 (Integer)
         */
        public SearchCollectionResultBuilder<E> foundRows(Integer foundRows) {
            if (foundRows == null) {
                this.foundRows = 0L;
            } else {
                this.foundRows = foundRows.longValue();
            }
            return this;
        }

        /**
         * 검색 결과 컬렉션 생성
         */
        public SearchCollectionResult<E> build() {
            return new SearchCollectionResult<>(this.collection, this.foundRows);
        }

        public String toString() {
            return "SearchCollectionResult.SearchCollectionResultBuilder(collection=" + this.collection + ", foundRows=" + this.foundRows + ")";
        }
    }
}
