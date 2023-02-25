package com.board.spring.yoony.command;

import com.board.spring.yoony.article.ArticleDeleteActionCommand;
import com.board.spring.yoony.article.ArticleModifyActionCommand;
import com.board.spring.yoony.article.ArticleWriteActionCommand;
import com.board.spring.yoony.comment.CommentWriteActionCommand;
import com.board.spring.yoony.file.FileDownloadActionCommand;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class DownloadCommandHelper {

  private final DependencyCommand dependencyCommand;
  private final Map<String, DownloadCommand> commands = new HashMap<>();

  public DownloadCommandHelper(DependencyCommand dependencyCommand) {
    this.dependencyCommand = dependencyCommand;
    this.commands.put("file", new FileDownloadActionCommand(this.dependencyCommand));
  }

  public DownloadCommand getCommand(String command) {
    DownloadCommand downloadCommand = commands.get(command);
    if (downloadCommand == null) {
      downloadCommand = new UnknownDownloadCommand();
    }
    return downloadCommand;
  }

}
