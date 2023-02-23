package com.board.spring.yoony.util;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class RequestUtil {

  public static String getUrlParameter(String url) {
    String[] urlSplit = url.split("\\?");
    if (urlSplit.length == 1) {
      return "";
    }
    return urlSplit[1];
  }


  public static int getIntParameter(HttpServletRequest request, String key) {
    return ValidationChecker.isEmpty(request.getParameter(key)) ? 0
        : Integer.parseInt(request.getParameter(key));
  }

}
