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

@Service
@RequiredArgsConstructor
public class ArticleModifyCommand implements MainCommand {


  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final DependencyCommand dependencyCommand;

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
