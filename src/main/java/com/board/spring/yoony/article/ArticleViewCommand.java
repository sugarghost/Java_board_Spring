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

/**
 * 게시글 상세보기 페이지를 처리하는 커맨드
 *
 * @author yoony
 * @version 1.0
 * @see MainCommand
 * @see DependencyCommand
 * @since 2023. 02. 26.
 */
@Service
@RequiredArgsConstructor
public class ArticleViewCommand implements MainCommand {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final DependencyCommand dependencyCommand;

  /**
   * 게시글 상세보기 페이지를 처리한다.
   * <p>articleId를 매개변수로 받아 게시글 정보를 가져온다.
   * <p>게시글에 등록된 파일 정보가 있다면 파일 리스트를 가져온다.
   * <p>게시글에 등록된 댓글 정보가 있다면 댓글 리스트를 가져온다.
   * <p>board/free/view 페이지로 이동한다.
   *
   * @param request   HttpServletRequest
   * @param paramMap  Map<String, Object> 처리에 필요한 파라미터를 담은 맵
   * @param viewModel Map<String, Object> 처리 결과를 담을 맵
   * @return String 뷰 페이지
   * @throws Exception
   * @throws CustomException
   * @see MainCommand#execute(HttpServletRequest, Map, Map)
   * @see ArticleMapper#selectArticle(long) 게시글 조회
   * @see ArticleMapper#updateArticleViewCount(long) 조회수 증가
   * @see FileMapper#selectFileList(long) 첨부파일 목록 조회
   * @see CommentMapper#selectCommentList(long) 댓글 목록 조회
   * @since 2023. 02. 26.
   */
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
