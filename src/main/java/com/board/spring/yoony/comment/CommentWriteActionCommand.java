package com.board.spring.yoony.comment;

import com.board.spring.yoony.article.ArticleMapper;
import com.board.spring.yoony.command.ActionCommand;
import com.board.spring.yoony.command.DependencyCommand;
import com.board.spring.yoony.error.CustomException;
import com.board.spring.yoony.error.ErrorCode;
import com.board.spring.yoony.util.RequestUtil;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * 댓글 작성 처리를 처리하는 커맨드
 *
 * @author yoony
 * @version 1.0
 * @see ActionCommand
 * @see DependencyCommand
 * @since 2023. 02. 26.
 */
@Service
@RequiredArgsConstructor
public class CommentWriteActionCommand implements ActionCommand {

  private Logger logger = LogManager.getLogger(CommentWriteActionCommand.class);
  private final DependencyCommand dependencyCommand;

  /**
   * 댓글 작성 처리를 수행한다.
   * <p>게시물 존재 여부 확인 후 댓글 작성
   * <p>articleId, content 파라미터를 받아와 작성
   * <p>댓글 등록 후 댓글 리스트를 가져와 model에 담아 반환
   *
   * @param request  HttpServletRequest
   * @param paramMap Map<String, Object> 처리에 필요한 파라미터를 담은 맵
   * @param model    Map<String, Object> 처리 결과를 담을 맵
   * @throws Exception
   * @throws CustomException
   * @author yoony
   * @version 1.0
   * @see ActionCommand#execute(HttpServletRequest, Map, Map)
   * @see ArticleMapper#selectArticleCheck(long) 게시글 존재 여부 체크
   * @see CommentMapper#insertComment(CommentDTO) 댓글 작성
   * @see CommentMapper#selectCommentList(long) 댓글 리스트 가져오기
   * @since 2023. 02. 26.
   */
  @Override
  public void execute(HttpServletRequest request, Map<String, Object> paramMap,
      Map<String, Object> model) throws Exception {
    logger.debug("execute()");

    // MyBatis Mapper 가져옴
    ArticleMapper articleMapper = dependencyCommand.getSqlSessionTemplate()
        .getMapper(ArticleMapper.class);
    CommentMapper commentMapper = dependencyCommand.getSqlSessionTemplate()
        .getMapper(CommentMapper.class);

    long articleId = RequestUtil.getLongParameter(request.getParameter("articleId"));
    String content = request.getParameter("content");

    // articleId 유효성 검사
    if (articleMapper.selectArticleCheck(articleId)) {
      new CustomException(ErrorCode.ARTICLE_ID_NOT_VALID);
    }

    // 게시물이 존재하면 댓글 작성
    CommentDTO commentDTO = new CommentDTO();
    commentDTO.setArticleId(articleId);
    commentDTO.setContent(content);

    // content 유효성 검사
    if (!commentDTO.isContentValid()) {
      new CustomException(ErrorCode.COMMENT_CONTENT_NOT_VALID);
    }

    int commentInsertResult = commentMapper.insertComment(commentDTO);

    // 댓글 작성 성공 여부 확인
    if (commentInsertResult < 1) {
      new CustomException(ErrorCode.COMMENT_INSERT_FAIL);
    }

    // 댓글 작성 성공시 댓글 새로 가져옴
    List<CommentDTO> commentList = commentMapper.selectCommentList(articleId);
    model.put("commentList", commentList);
  }
}
