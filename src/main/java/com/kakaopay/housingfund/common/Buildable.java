package com.kakaopay.housingfund.common;

/**
 * Builder pattern을 적용시키기 위한 인터페이스
 * @param <T>
 */
public interface Buildable<T> {

    T build();
}
