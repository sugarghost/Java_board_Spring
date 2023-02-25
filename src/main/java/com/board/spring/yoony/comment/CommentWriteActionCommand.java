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

@Service
@RequiredArgsConstructor
public class CommentWriteActionCommand implements ActionCommand {

  private Logger logger = LogManager.getLogger(CommentWriteActionCommand.class);
  private final DependencyCommand dependencyCommand;
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
      if(articleMapper.selectArticleCheck(articleId)){
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
