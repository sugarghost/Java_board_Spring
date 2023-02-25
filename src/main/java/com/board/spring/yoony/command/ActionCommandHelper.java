package com.board.spring.yoony.command;

import com.board.spring.yoony.article.ArticleDeleteActionCommand;
import com.board.spring.yoony.article.ArticleModifyActionCommand;
import com.board.spring.yoony.article.ArticleWriteActionCommand;
import com.board.spring.yoony.comment.CommentWriteActionCommand;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ActionCommandHelper {

  private final DependencyCommand dependencyCommand;
  private final Map<String, ActionCommand> commands = new HashMap<>();

  public ActionCommandHelper(DependencyCommand dependencyCommand) {
    this.dependencyCommand = dependencyCommand;
    this.commands.put("write", new ArticleWriteActionCommand(this.dependencyCommand));
    this.commands.put("comment_write", new CommentWriteActionCommand(this.dependencyCommand));
    this.commands.put("modify", new ArticleModifyActionCommand(this.dependencyCommand));
    this.commands.put("delete", new ArticleDeleteActionCommand(this.dependencyCommand));
  }

  public ActionCommand getCommand(String command) {
    ActionCommand actionCommand = commands.get(command);
    if (actionCommand == null) {
      actionCommand = new UnknownActionCommand();
    }
    return actionCommand;
  }

}
