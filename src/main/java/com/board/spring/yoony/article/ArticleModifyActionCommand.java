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
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@RequiredArgsConstructor
public class ArticleModifyActionCommand implements ActionCommand {

  private Logger logger = LogManager.getLogger(ArticleWriteActionCommand.class);

  private final DependencyCommand dependencyCommand;
  @Transactional
  @Override
  public void execute(HttpServletRequest request, Map<String, Object> paramMap,
      Map<String, Object> model) throws Exception {
    logger.debug("execute()");
    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

    ArticleDTO articleDTO = new ArticleDTO();

    articleDTO.setArticleId(
        RequestUtil.getIntParameter(multipartRequest.getParameter("articleId")));

    articleDTO.setTitle(multipartRequest.getParameter("title"));
    articleDTO.setContent(multipartRequest.getParameter("content"));
    articleDTO.setWriter(multipartRequest.getParameter("writer"));
    articleDTO.setPassword(multipartRequest.getParameter("password"));

    if (!articleDTO.isUpdateArticleValid()) {
      throw new CustomException(ErrorCode.ARTICLE_UPDATE_NOT_VALID);
    }
    articleDTO.setPassword(Security.sha256Encrypt(articleDTO.getPassword()));

    // MyBatis Mapper 가져옴
    ArticleMapper articleMapper = dependencyCommand.getSqlSessionTemplate()
        .getMapper(ArticleMapper.class);
    boolean isPasswordValid = articleMapper.selectPasswordCheck(articleDTO);

    // 패스워드가 일치하지 않으면 에러
    if (!isPasswordValid) {
      throw new CustomException(ErrorCode.ARTICLE_PASSWORD_NOT_VALID);
    }
    // 검사를 통과했으면 수정을 진행

    int articleUpdateResult = articleMapper.updateArticle(articleDTO);
    if (articleUpdateResult < 1) {
      throw new CustomException(ErrorCode.ARTICLE_UPDATE_FAIL);
    }
    // 파일 삭제 대상의 ID들을 받아옴
    String[] deleteFileIds = multipartRequest.getParameterValues("deleteFileId");

    FileMapper fileMapper = dependencyCommand.getSqlSessionTemplate()
        .getMapper(FileMapper.class);

    // 파일 삭제 대상이 있다면 삭제

    if (deleteFileIds != null) {
      for (String deleteFileId : deleteFileIds) {
        FileDTO deleteFileDTO = new FileDTO();
        deleteFileDTO.setFileId(Integer.parseInt(deleteFileId));
        deleteFileDTO.setArticleId(articleDTO.getArticleId());
        fileMapper.deleteFile(deleteFileDTO);
      }
    }

    // 파일 삭제와 별개로 넘어온 파일이 있는 경우 Upload 처리
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
      fileDTO.setArticleId(articleDTO.getArticleId());
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
    dependencyCommand.getSqlSessionTemplate().commit();
  }
}
