package com.board.spring.yoony.article;

import com.board.spring.yoony.command.ActionCommand;
import com.board.spring.yoony.command.DependencyCommand;
import com.board.spring.yoony.error.CustomException;
import com.board.spring.yoony.error.ErrorCode;
import com.board.spring.yoony.file.FileDTO;
import com.board.spring.yoony.file.FileMapper;
import com.board.spring.yoony.util.RequestUtil;
import com.board.spring.yoony.util.Security;
import com.board.spring.yoony.util.ValidationChecker;
import java.io.File;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 게시글 등록 처리를 위한 커맨드
 *
 * @author yoony
 * @version 1.0
 * @see ActionCommand
 * @see DependencyCommand
 * @since 2023. 02. 26.
 */
@Service
@RequiredArgsConstructor
public class ArticleWriteActionCommand implements ActionCommand {

  private Logger logger = LogManager.getLogger(ArticleWriteActionCommand.class);

  private final DependencyCommand dependencyCommand;

  /**
   * 게시글 등록 처리를 수행한다.
   * <p>게시글 등록을 위해 title, content, writer, categoryId, password를 매개변수로 받아옴
   * <p>password를 sha256으로 암호화하고 게시글을 등록
   * <p>파일이 첨부되었다면 파일을 저장하고 파일 정보를 DB에 저장
   *
   * @param request  HttpServletRequest
   * @param paramMap Map<String, Object> 처리에 필요한 파라미터를 담은 맵
   * @param model    Map<String, Object> 처리 결과를 담을 맵
   * @throws Exception
   * @throws CustomException
   * @author yoony
   * @version 1.0
   * @see ActionCommand#execute(HttpServletRequest, Map, Map)
   * @see ArticleMapper#insertArticle(ArticleDTO) 게시글 등록
   * @see FileMapper#insertFile(FileDTO) 파일 등록
   * @since 2023. 02. 26.
   */
  @Transactional
  @Override
  public void execute(HttpServletRequest request, Map<String, Object> paramMap,
      Map<String, Object> model) throws Exception {

    // TODO: 나중에 서비스를 만들떄, Return이 유효하지 않다면 그냥 예외를 던져버리기
    // 예를 들어 Update경우 return에 2, 3 등의 1이 아닌 경우가 들어오는 경우가 생길 수 있음
    // 즉 return을 true 등으로 던지면 확장성이 떨어짐.
    // 유효하지 않은 경우는 내부적으로 예외를 던지고
    // 패스워드 체크 등의 별도 검증은 내부적으로 하거나 아니면 컨트롤러에서 미리 잡아주기

    logger.debug("execute()");
    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

    ArticleDTO articleDTO = new ArticleDTO();
    articleDTO.setTitle(multipartRequest.getParameter("title"));
    articleDTO.setContent(multipartRequest.getParameter("content"));
    articleDTO.setWriter(multipartRequest.getParameter("writer"));
    articleDTO.setCategoryId(
        RequestUtil.getIntParameter(multipartRequest.getParameter("categoryId")));
    articleDTO.setPassword(multipartRequest.getParameter("password"));

    // insert 유효성 검사
    if (!articleDTO.isInsertArticleValid()) {
      logger.debug("isInsertArticleValid() : invalid data");
      throw new CustomException(ErrorCode.ARTICLE_INSERT_NOT_VALID);
    }

    articleDTO.setPassword(Security.sha256Encrypt(multipartRequest.getParameter("password")));

    // MyBatis Mapper 가져옴
    SqlSessionTemplate sqlSessionTemplate = dependencyCommand.getSqlSessionTemplate();
    ArticleMapper articleMapper = sqlSessionTemplate.getMapper(ArticleMapper.class);

    // 게시글을 등록함
    int articleInsertResult = articleMapper.insertArticle(articleDTO);
    long articleId = articleDTO.getArticleId();
    logger.debug("articleId : " + articleId);
    // 게시글 등록 성공 여부를 확인함
    if (articleInsertResult < 1) {
      throw new CustomException(ErrorCode.ARTICLE_INSERT_FAIL);
    }

    String contentType = multipartRequest.getContentType();
    if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {

      // 파일을 저장하고 파일 정보를 DB에 저장함
      FileMapper fileMapper = sqlSessionTemplate.getMapper(FileMapper.class);
      java.util.Iterator<String> fileNames = multipartRequest.getFileNames();
      while (fileNames.hasNext()) {
        String targetFileName = fileNames.next();
        MultipartFile file = multipartRequest.getFile(targetFileName);

        String originalFileName = file.getOriginalFilename();
        if (ValidationChecker.isEmpty(originalFileName)) {
          continue;
        }
        String ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        String newFileName = UUID.randomUUID() + "." + ext;
        file.transferTo(new File(newFileName));

        FileDTO fileDTO = new FileDTO();
        fileDTO.setArticleId(articleId);
        fileDTO.setFileOriginName(originalFileName);
        fileDTO.setFileSaveName(newFileName);
        fileDTO.setFileType(ext);
        fileDTO.setFilePath(dependencyCommand.getEnvironment()
            .getProperty("spring.servlet.multipart.location"));

        int insertResult = fileMapper.insertFile(fileDTO);

        if (insertResult < 1) {
          throw new CustomException(ErrorCode.FILE_INSERT_FAIL);
        }
      }
    }
    sqlSessionTemplate.commit();
  }
}
