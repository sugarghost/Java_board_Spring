package com.board.spring.yoony.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PropertyUtil {
  @Autowired
  private static Environment env;

  public static String getProperty(String key) {
    return env.getProperty(key);
  }
}
