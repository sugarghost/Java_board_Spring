package com.board.spring.yoony;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class CustomErrorController implements ErrorController {

  private static String IMAGE_PATH = "images/error/";

  @ExceptionHandler(Throwable.class)
  @RequestMapping("/error")
  public String handleError(HttpServletRequest request, HttpServletResponse response,  Model model) {
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

    int statusCode = Integer.parseInt(status.toString());
    response.setStatus(statusCode);

    model.addAttribute("code", status.toString());
    model.addAttribute("msg", HttpStatus.valueOf(Integer.valueOf(status.toString())));
    return "error/error";
  }
}