package com.board.spring.yoony.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 의존성 주입을 위한 커맨드
 *
 * @author yoony
 * @version 1.0
 * @see SqlSessionTemplate
 * @see Environment
 * @see MessageSource
 * @since 2023. 02. 26.
 */
@Component
@RequiredArgsConstructor
@Getter
public class DependencyCommand {

  /**
   * MyBatis를 사용하기 위한 SqlSessionTemplate
   */
  private final SqlSessionTemplate sqlSessionTemplate;
  /**
   * 환경 변수를 관리하는 Environment
   */
  private final Environment environment;
  /**
   * 메시지를 관리하는 MessageSource
   */
  private final MessageSource messageSource;
}
