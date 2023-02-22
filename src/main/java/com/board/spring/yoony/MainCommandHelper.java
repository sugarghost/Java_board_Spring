package com.board.spring.yoony;

import com.board.spring.yoony.article.ArticleListCommand;
import java.util.HashMap;
import java.util.Map;


public class MainCommandHelper {

  private static final Map<String, MainCommand> commands = new HashMap<>();

  // command String과 Command를 매핑하기 위한 commands 초기화 블록
  static {
    commands.put("list", new ArticleListCommand());
  }

  public static MainCommand getCommand(String command) {
    MainCommand mainCommand = commands.get(command);
    if (mainCommand == null) {
      mainCommand = new UnknownMainCommand();
    }
    return mainCommand;
  }

}
