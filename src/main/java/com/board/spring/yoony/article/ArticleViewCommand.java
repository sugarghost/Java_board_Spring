package com.board.spring.yoony.article;

import com.board.spring.yoony.command.DependencyCommand;
import com.board.spring.yoony.command.MainCommand;
import com.board.spring.yoony.comment.CommentDTO;
import com.board.spring.yoony.comment.CommentMapper;
import com.board.spring.yoony.error.CustomException;
import com.board.spring.yoony.error.CustomExceptionView;
import com.board.spring.yoony.error.ErrorCode;
import com.board.spring.yoony.file.FileDTO;
import com.board.spring.yoony.file.FileMapper;
import com.board.spring.yoony.util.RequestUtil;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleViewCommand implements MainCommand {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final DependencyCommand dependencyCommand;

  @Override
  public String execute(HttpServletRequest request, Map<String, Object> paramMap,
      Map<String, Object> viewModel) {
    logger.debug("execute()");
    String viewPage = "board/free/view";
    ArticleMapper articleMapper = dependencyCommand.getSqlSessionTemplate()
        .getMapper(ArticleMapper.class);
    FileMapper fileMapper = dependencyCommand.getSqlSessionTemplate().getMapper(FileMapper.class);
    CommentMapper commentMapper = dependencyCommand.getSqlSessionTemplate()
        .getMapper(CommentMapper.class);

    long articleId = RequestUtil.getLongParameter(request.getParameter("articleId"));

    if (articleId == 0) {
      throw new CustomExceptionView(ErrorCode.ARTICLE_ID_NOT_VALID);
    }

    ArticleDTO articleDTO = articleMapper.selectArticle(articleId);
    if (articleDTO == null) {
      throw new CustomExceptionView(ErrorCode.ARTICLE_NOT_FOUND);
    }

    // 조회수 증가
    articleMapper.updateArticleViewCount(articleId);
    // 별 의미는 없지만 보여줄때 자기가 들어가서 올라간 조회수 확인하라고 +1해줌
    articleDTO.setViewCount(articleDTO.getViewCount() + 1);

    viewModel.put("articleDTO", articleDTO);

    // 게시글에 파일이 존재하면 파일 목록을 가져옴
    if (articleDTO.getIsFileExist()) {
      List<FileDTO> fileList = fileMapper.selectFileList(articleId);
      viewModel.put("fileList", fileList);
    }

    // 댓글 목록을 가져옴
    List<CommentDTO> commentList = commentMapper.selectCommentList(articleId);
    if (commentList != null) {
      viewModel.put("commentList", commentList);
    }
    return viewPage;
  }
}
