package com.board.spring.yoony.command;

import com.board.spring.yoony.article.ArticleWriteActionCommand;
import java.util.HashMap;
import java.util.Map;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActionCommandHelper {

  @Autowired
  private final SqlSessionTemplate sqlSessionTemplate;
  private final Map<String, ActionCommand> commands = new HashMap<>();

  public ActionCommandHelper(SqlSessionTemplate sqlSessionTemplate) {
    this.sqlSessionTemplate = sqlSessionTemplate;
    this.commands.put("write", new ArticleWriteActionCommand(sqlSessionTemplate));
  }

  public ActionCommand getCommand(String command) {
    ActionCommand actionCommand = commands.get(command);
    if (actionCommand == null) {
      actionCommand = new UnknownActionCommand();
    }
    return actionCommand;
  }

}
