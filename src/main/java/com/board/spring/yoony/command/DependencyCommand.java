package com.board.spring.yoony.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
public class DependencyCommand {
  private final SqlSessionTemplate sqlSessionTemplate;
  private final Environment environment;
  private final MessageSource messageSource;
}
