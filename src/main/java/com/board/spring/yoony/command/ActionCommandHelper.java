package com.board.spring.yoony.command;

import com.board.spring.yoony.article.ArticleDeleteActionCommand;
import com.board.spring.yoony.article.ArticleModifyActionCommand;
import com.board.spring.yoony.article.ArticleWriteActionCommand;
import com.board.spring.yoony.comment.CommentWriteActionCommand;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * ActionCommandHelper 클래스
 * <p>요청 처리를 위한 ActionCommand를 생성하는 클래스
 *
 * @author yoony
 * @version 1.0
 * @see ActionCommand
 * @see DependencyCommand
 * @since 2023. 02. 26.
 */
@Component
public class ActionCommandHelper {

  /**
   * ActionCommand에서 사용할 의존성을 관리하는 클래스
   */
  private final DependencyCommand dependencyCommand;
  /**
   * 요청 처리를 위한 ActionCommand를 저장하는 맵
   */
  private final Map<String, ActionCommand> commands = new HashMap<>();

  /**
   * ActionCommandHelper 생성자
   * <p>의존성을 관리하는 클래스를 매개변수로 받아 의존성을 주입한다.
   * <p>요청 처리를 위한 ActionCommand를 생성하여 맵에 저장한다.
   *
   * @param dependencyCommand 의존성을 관리하는 클래스
   * @version 1.0
   * @author yoony
   * @see DependencyCommand 의존성을 관리하는 클래스
   * @see ArticleWriteActionCommand 게시글 작성 요청 처리를 위한 ActionCommand
   * @see CommentWriteActionCommand 댓글 작성 요청 처리를 위한 ActionCommand
   * @see ArticleModifyActionCommand 게시글 수정 요청 처리를 위한 ActionCommand
   * @see ArticleDeleteActionCommand 게시글 삭제 요청 처리를 위한 ActionCommand
   * @since 2023. 02. 26.
   */
  // TODO: commans라고 하니 LIST 느낌이라고 생각함 이름을 그냥 Map으로 변경하는 걸 추천
  // TODO: command를 사용할 필요 없음 Mapping으로 분리
  //  1주차에 JSP 방식을 쓰고 2주차에 Model2를 씀 그떄 모델과 뷰를 찢어내자라는 철학으로 커맨드 패턴을 사용힘
  // 이제 스프링으로 넘어오면서 그게 다 스프링 컨트롤러에 녹아있기에 Command 패턴을 가져올 필요는 없음
  // Spring에서 분배다 됨 Mapping으로
  public ActionCommandHelper(DependencyCommand dependencyCommand) {
    this.dependencyCommand = dependencyCommand;
    this.commands.put("write", new ArticleWriteActionCommand(this.dependencyCommand));
    this.commands.put("comment_write", new CommentWriteActionCommand(this.dependencyCommand));
    this.commands.put("modify", new ArticleModifyActionCommand(this.dependencyCommand));
    this.commands.put("delete", new ArticleDeleteActionCommand(this.dependencyCommand));
  }

  /**
   * 요청 처리를 위한 ActionCommand를 반환한다.
   * <p>만약 요청 처리를 위한 ActionCommand가 없다면 UnknownActionCommand를 반환한다.
   *
   * @param command 요청 처리를 위한 ActionCommand와 매핑되는 문자열
   * @return ActionCommand 요청 처리를 위한 ActionCommand
   * @version 1.0
   * @author yoony
   * @see ActionCommand 요청 처리를 위한 ActionCommand
   * @see UnknownActionCommand 요청 처리를 위한 ActionCommand가 없을 때 반환하는 ActionCommand
   * @since 2023. 02. 26.
   */
  public ActionCommand getCommand(String command) {
    ActionCommand actionCommand = commands.get(command);
    if (actionCommand == null) {
      actionCommand = new UnknownActionCommand();
    }
    return actionCommand;
  }

}
