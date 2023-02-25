package com.board.spring.yoony.article;

import com.board.spring.yoony.command.ActionCommand;
import com.board.spring.yoony.error.CustomException;
import com.board.spring.yoony.error.ErrorCode;
import com.board.spring.yoony.file.FileDTO;
import com.board.spring.yoony.file.FileMapper;
import com.board.spring.yoony.file.FileProperty;
import com.board.spring.yoony.util.RequestUtil;
import com.board.spring.yoony.util.Security;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
@Service
@RequiredArgsConstructor
public class ArticleWriteActionCommand implements ActionCommand {

  private Logger logger = LogManager.getLogger(ArticleWriteActionCommand.class);

  @Autowired
  private final SqlSessionTemplate sqlSessionTemplate;
  @Override
  public void execute(MultipartHttpServletRequest request, Map<String, Object> paramMap,
      Map<String, Object> model) {
    logger.debug("execute()");

    try{
    // MyBatis instance 가져옴
    ArticleDTO articleDTO = new ArticleDTO();
      logger.debug("title 1: " + ((MultipartHttpServletRequest) request).getParameter("title"));
      logger.debug("title 2: " + ((MultipartHttpServletRequest) request).getAttribute("title"));
      logger.debug("title 3: " + ((MultipartHttpServletRequest) request).getParameterValues("title"));
      articleDTO.setTitle(request.getParameter("title"));
      articleDTO.setContent(request.getParameter("content"));
      articleDTO.setWriter(request.getParameter("writer"));
      articleDTO.setCategoryId(RequestUtil.getIntParameter(request, "categoryId"));
      articleDTO.setPassword(request.getParameter("password"));

      if (!articleDTO.isInsertArticleValid()) {
        logger.debug("isInsertArticleValid() : invalid data");
      }

      articleDTO.setPassword(Security.sha256Encrypt(request.getParameter("password")));

      // MyBatis Mapper 가져옴
      ArticleMapper articleMapper = sqlSessionTemplate.getMapper(ArticleMapper.class);
      // 게시글을 등록함
      int articleInsertResult = articleMapper.insertArticle(articleDTO);
      long articleId = articleDTO.getArticleId();
      logger.debug("articleId : " + articleId);
      // 게시글 등록 성공 여부를 확인함
      if (articleInsertResult > 0) {

        String contentType = request.getContentType();
        if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
          List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("files");
          for (MultipartFile file : files) {

            String fileName = file.getOriginalFilename();
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
            String newFileName = UUID.randomUUID() + "." + ext;
            file.transferTo(new File(newFileName));
            FileProperty fileProperty = new FileProperty();


            FileDTO fileDTO = new FileDTO();
            fileDTO.setArticleId(articleId);
            fileDTO.setFileOriginName(fileName);
            fileDTO.setFileSaveName(newFileName);
            fileDTO.setFileType(ext);
            fileDTO.setFilePath(fileProperty.getUploadPath());


            FileMapper fileMapper = sqlSessionTemplate.getMapper(FileMapper.class);
            int fileInsertResult = fileMapper.insertFile(fileDTO);

            if (fileInsertResult == 0) {
              logger.error("파일 등록 실패 :" + fileName);
            }
          }
        }
      } else {
        logger.error("게시글 등록 실패");
      }
    } catch (Exception e) {
      logger.error("에러");
      throw new CustomException("게시글 등록 실패", ErrorCode.INTER_SERVER_ERROR);
    }
  }
}
