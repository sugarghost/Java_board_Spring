package com.board.spring.yoony.command;

import com.board.spring.yoony.article.ArticleListCommand;
import java.util.HashMap;
import java.util.Map;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainCommandHelper {

  @Autowired
  private final SqlSessionTemplate sqlSessionTemplate;
  private final Map<String, MainCommand> commands = new HashMap<>();

  public MainCommandHelper(SqlSessionTemplate sqlSessionTemplate) {
    this.sqlSessionTemplate = sqlSessionTemplate;
    this.commands.put("list", new ArticleListCommand(sqlSessionTemplate));
  }

  public MainCommand getCommand(String command) {
    MainCommand mainCommand = commands.get(command);
    if (mainCommand == null) {
      mainCommand = new UnknownMainCommand();
    }
    return mainCommand;
  }

}
