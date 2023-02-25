package com.board.spring.yoony.error;

import java.util.HashMap;
import java.util.Locale;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

/**
 * 전역 예외 처리를 위한 클래스
 *
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 26.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 에러 메시지를 가져오기 위해 사용하는 메시지 소스
   */
  @Autowired
  private MessageSource messageSource;

  /**
   * CustomExceptionView 예외를 처리하고 에러 페이지로 이동한다.
   * <p>에러 코드와 에러 메시지를 {@link ModelAndView}에 담아서 에러 페이지로 이동한다.
   * <p>에러 페이지는 "error/error"이다.
   * <p>에러 코드는 {@link ErrorCode}에 정의된 에러 코드를 사용한다.
   * <p>에러 메시지는 {@link MessageSource}를 사용하여 현재 로케일에 맞는 에러 메시지를 가져온다.
   *
   * @param ex CustomExceptionView 예외
   * @return ModelAndView
   * @author yoony
   * @version 1.0
   * @see ModelAndView
   * @see CustomExceptionView
   * @since 2023. 02. 26.
   */
  @ExceptionHandler(CustomExceptionView.class)
  public ModelAndView handleCustomExceptionView(CustomExceptionView ex) {
    log.error("handleCustomExceptionView", ex);

    ModelAndView modelAndView = new ModelAndView();

    Locale currentLocale = LocaleContextHolder.getLocale();
    String errorMessage = messageSource.getMessage(ex.getErrorCode().getErrorCode(), null,
        currentLocale);
    modelAndView.addObject("errorMessage", errorMessage);
    modelAndView.addObject("errorCode", ex.getErrorCode().getStatus());
    modelAndView.setViewName("error/error");
    return modelAndView;
  }

  /**
   * CustomException 예외를 처리하고 에러 응답을 반환한다.
   * <p>에러 코드와 에러 메시지를 {@link ResponseEntity}에 담아서 반환한다.
   * <p>에러 코드는 {@link ErrorCode}에 정의된 에러 코드를 사용한다.
   * <p>에러 메시지는 {@link MessageSource}를 사용하여 현재 로케일에 맞는 에러 메시지를 가져온다.
   *
   * @param ex CustomException 예외
   * @return ResponseEntity<ErrorResponse> 에러 응답을 담은 ResponseEntity 객체
   * @see ResponseEntity
   * @see ErrorResponse
   * @see CustomException
   * @since 2023. 02. 26.
   */
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
    log.error("handleCustomException", ex);
    Locale currentLocale = LocaleContextHolder.getLocale();
    String errorMessage = messageSource.getMessage(ex.getErrorCode().getErrorCode(), null,
        currentLocale);
    ErrorResponse response = new ErrorResponse(ex.getErrorCode(), errorMessage);
    return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
  }

  /**
   * Exception 예외를 처리하고 에러 페이지로 이동한다.
   * <p>에러 페이지는 "error/error"이다.
   *
   * @param ex Exception 예외
   * @return ModelAndView
   * @see ModelAndView
   * @see Exception
   * @since 2023. 02. 26.
   */
  @ExceptionHandler(Exception.class)
  public ModelAndView handleException(Exception ex) {
    log.error("handleException", ex);
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject("errorMessage", ex.getMessage());
    modelAndView.setViewName("error/error");
    return modelAndView;
  }
}