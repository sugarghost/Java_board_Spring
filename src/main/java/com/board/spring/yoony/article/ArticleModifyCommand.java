package com.board.spring.yoony.article;

import com.board.spring.yoony.command.DependencyCommand;
import com.board.spring.yoony.command.MainCommand;
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
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 게시글 수정 페이지를 처리하는 커맨드
 *
 * @author yoony
 * @version 1.0
 * @see MainCommand
 * @see DependencyCommand
 * @since 2023. 02. 26.
 */
@Service
@RequiredArgsConstructor
public class ArticleModifyCommand implements MainCommand {


  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final DependencyCommand dependencyCommand;

  /**
   * 게시글 수정 페이지를 처리한다.
   * <p>articleId를 매개변수로 받아 게시글 정보를 가져온다.
   * <p>게시글에 등록된 파일 정보가 있다면 파일 리스트를 가져온다.
   * <p>board/free/modify 페이지로 이동한다.
   *
   * @param request   HttpServletRequest
   * @param paramMap  Map<String, Object> 처리에 필요한 파라미터를 담은 맵
   * @param viewModel Map<String, Object> 처리 결과를 담을 맵
   * @return String 뷰 페이지
   * @throws Exception
   * @throws CustomException
   * @see MainCommand#execute(HttpServletRequest, Map, Map)
   * @see ArticleMapper#selectArticle(long) 게시글 조회
   * @see FileMapper#selectFileList(long) 첨부파일 목록 조회
   * @since 2023. 02. 26.
   */
  @Override
  public String execute(HttpServletRequest request, Map<String, Object> paramMap,
      Map<String, Object> viewModel) {
    logger.debug("execute()");
    String viewPage = "board/free/modify";

    ArticleMapper articleMapper = dependencyCommand.getSqlSessionTemplate()
        .getMapper(ArticleMapper.class);

    long articleId = RequestUtil.getLongParameter(request.getParameter("articleId"));

    ArticleDTO articleDTO = articleMapper.selectArticle(articleId);
    if (articleDTO == null) {
      throw new CustomExceptionView(ErrorCode.ARTICLE_NOT_FOUND);
    }
    request.setAttribute("articleDTO", articleDTO);
    if (articleDTO.isFileExist()) {
      FileMapper fileMapper = dependencyCommand.getSqlSessionTemplate().getMapper(FileMapper.class);
      List<FileDTO> fileList = fileMapper.selectFileList(articleId);
      request.setAttribute("fileList", fileList);
    }
    return viewPage;
  }
}
