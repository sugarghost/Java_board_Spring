package com.board.spring.yoony.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 설명
 *
 * @author YK
 * @version 1.0
 * @fileName RequestEndpoint
 * @since 2023-02-21
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestEndpoint {
  String value();
}