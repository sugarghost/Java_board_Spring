package com.board.spring.yoony.command;

import com.board.spring.yoony.article.ArticleDeleteActionCommand;
import com.board.spring.yoony.article.ArticleModifyActionCommand;
import com.board.spring.yoony.article.ArticleWriteActionCommand;
import com.board.spring.yoony.comment.CommentWriteActionCommand;
import com.board.spring.yoony.file.FileDownloadActionCommand;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * DownloadCommandHelper 클래스
 * <p>다운로드 처리를 위한 DownloadCommand를 생성하는 클래스
 *
 * @author yoony
 * @version 1.0
 * @see DownloadCommand
 * @see DependencyCommand
 * @since 2023. 02. 26.
 */
@Component
public class DownloadCommandHelper {

  /**
   * DownloadCommand에서 사용할 의존성을 관리하는 클래스
   */
  private final DependencyCommand dependencyCommand;
  /**
   * 다운로드 처리를 위한 DownloadCommand를 저장하는 맵
   */
  private final Map<String, DownloadCommand> commands = new HashMap<>();

  /**
   * DownloadCommandHelper 생성자
   * <p>의존성을 관리하는 클래스를 매개변수로 받아 의존성을 주입한다.
   * <p>다운로드 처리를 위한 DownloadCommand를 생성하여 맵에 저장한다.
   *
   * @param dependencyCommand 의존성을 관리하는 클래스
   * @version 1.0
   * @author yoony
   * @see DependencyCommand 의존성을 관리하는 클래스
   * @see FileDownloadActionCommand 파일 다운로드 요청 처리를 위한 DownloadCommand
   * @since 2023. 02. 26.
   */
  public DownloadCommandHelper(DependencyCommand dependencyCommand) {
    this.dependencyCommand = dependencyCommand;
    this.commands.put("file", new FileDownloadActionCommand(this.dependencyCommand));
  }

  /**
   * 다운로드 처리를 위한 DownloadCommand를 반환한다.
   * <p>만약 다운로드 처리를 위한 DownloadCommand가 없다면 UnknownDownloadCommand를 반환한다.
   *
   * @param command 다운로드 처리를 위한 DownloadCommand와 매핑되는 문자열
   * @return 다운로드 처리를 위한 DownloadCommand
   * @version 1.0
   * @author yoony
   * @see UnknownDownloadCommand 다운로드 처리를 위한 DownloadCommand가 없을 때 반환하는 클래스
   * @since 2023. 02. 26.
   */
  public DownloadCommand getCommand(String command) {
    DownloadCommand downloadCommand = commands.get(command);
    if (downloadCommand == null) {
      downloadCommand = new UnknownDownloadCommand();
    }
    return downloadCommand;
  }

}
