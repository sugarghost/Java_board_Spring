package com.board.spring.yoony.util;

import org.springframework.stereotype.Component;

@Component
public class ValidationChecker {

  public static boolean isEmpty(String targetString) {
    return targetString == null || "".equals(targetString) || targetString.isEmpty();
  }

  public static boolean isEmpty(Object targetObject) {
    return targetObject == null || "".equals(targetObject);
  }


  public static String SubStringWithSkipMark(String targetString, int StringLength) {
    if (targetString.length() > StringLength) {
      targetString = targetString.substring(0, StringLength) + "...";
    }
    return targetString;
  }
}
