package com.board.spring.yoony.command;

import com.board.spring.yoony.article.ArticleListCommand;
import com.board.spring.yoony.article.ArticleModifyCommand;
import com.board.spring.yoony.article.ArticleViewCommand;
import com.board.spring.yoony.article.ArticleWriteCommand;
import java.util.HashMap;
import java.util.Map;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

@Component
public class MainCommandHelper {

  private final DependencyCommand dependencyCommand;
  private final Map<String, MainCommand> commands = new HashMap<>();

  public MainCommandHelper(SqlSessionTemplate sqlSessionTemplate,
      DependencyCommand dependencyCommand) {
    this.dependencyCommand = dependencyCommand;
    this.commands.put("list", new ArticleListCommand(dependencyCommand));
    this.commands.put("write", new ArticleWriteCommand(dependencyCommand));
    this.commands.put("view", new ArticleViewCommand(dependencyCommand));
    this.commands.put("modify", new ArticleModifyCommand(dependencyCommand));
  }

  public MainCommand getCommand(String command) {
    MainCommand mainCommand = commands.get(command);
    if (mainCommand == null) {
      mainCommand = new UnknownMainCommand();
    }
    return mainCommand;
  }

}
