package com.board.spring.yoony.command;

import com.board.spring.yoony.article.ArticleListCommand;
import com.board.spring.yoony.article.ArticleModifyCommand;
import com.board.spring.yoony.article.ArticleViewCommand;
import com.board.spring.yoony.article.ArticleWriteCommand;
import java.util.HashMap;
import java.util.Map;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

/**
 * MainCommandHelper 클래스
 * <p>페이지 요청 처리를 위한 MainCommand를 생성하는 클래스
 *
 * @author yoony
 * @version 1.0
 * @see MainCommand
 * @see DependencyCommand
 * @since 2023. 02. 26.
 */
@Component
public class MainCommandHelper {

  /**
   * MainCommand에서 사용할 의존성을 관리하는 클래스
   */
  private final DependencyCommand dependencyCommand;
  /**
   * 페이지 요청 처리를 위한 MainCommand를 저장하는 맵
   */
  private final Map<String, MainCommand> commands = new HashMap<>();

  /**
   * MainCommandHelper 생성자
   * <p>의존성을 관리하는 클래스를 매개변수로 받아 의존성을 주입한다.
   * <p>메인 페이지 요청 처리를 위한 MainCommand를 생성하여 맵에 저장한다.
   *
   * @param dependencyCommand 의존성을 관리하는 클래스
   * @version 1.0
   * @author yoony
   * @see DependencyCommand 의존성을 관리하는 클래스
   * @see ArticleListCommand 게시글 목록 페이지 처리를 위한 MainCommand
   * @see ArticleWriteCommand 게시글 작성 페이지 처리를 위한 MainCommand
   * @see ArticleViewCommand 게시글 조회 페이지 처리를 위한 MainCommand
   * @see ArticleModifyCommand 게시글 수정 페이지 처리를 위한 MainCommand
   * @since 2023. 02. 26.
   */
  public MainCommandHelper(DependencyCommand dependencyCommand) {
    this.dependencyCommand = dependencyCommand;
    this.commands.put("list", new ArticleListCommand(dependencyCommand));
    this.commands.put("write", new ArticleWriteCommand(dependencyCommand));
    this.commands.put("view", new ArticleViewCommand(dependencyCommand));
    this.commands.put("modify", new ArticleModifyCommand(dependencyCommand));
  }

  /**
   * 페이지 요청 처리를 위한 MainCommand를 반환한다.
   * <p>만약 페이지 요청 처리를 위한 MainCommand가 없다면 UnknownMainCommand를 반환한다.
   *
   * @param command 메인 페이지 요청 처리를 위한 MainCommand와 매핑되는 문자열
   * @return 메인 페이지 요청 처리를 위한 MainCommand
   * @version 1.0
   * @see MainCommand 페이지 요청 처리를 위한 MainCommand
   * @see UnknownMainCommand 알 수 없는 요청 처리를 위한 MainCommand
   * @since 2023. 02. 26.
   */
  public MainCommand getCommand(String command) {
    MainCommand mainCommand = commands.get(command);
    if (mainCommand == null) {
      mainCommand = new UnknownMainCommand();
    }
    return mainCommand;
  }

}
