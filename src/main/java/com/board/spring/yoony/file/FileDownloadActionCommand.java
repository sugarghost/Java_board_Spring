package com.board.spring.yoony.file;


import com.board.spring.yoony.article.ArticleWriteActionCommand;
import com.board.spring.yoony.command.ActionCommand;
import com.board.spring.yoony.command.DependencyCommand;
import com.board.spring.yoony.command.DownloadCommand;
import com.board.spring.yoony.error.CustomException;
import com.board.spring.yoony.error.ErrorCode;
import com.board.spring.yoony.util.RequestUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * 파일 다운로드를 위한 커맨드
 *
 * @author yoony
 * @version 1.0
 * @see ActionCommand
 * @see DependencyCommand
 * @since 2023. 02. 26.
 */
@Service
@RequiredArgsConstructor
public class FileDownloadActionCommand implements DownloadCommand {


  private Logger logger = LogManager.getLogger(ArticleWriteActionCommand.class);

  private final DependencyCommand dependencyCommand;

  /**
   * 파일 다운로드를 수행한다.
   * <p>파라미터로 articleId, fileId를 받아와서 해당 파일을 다운로드
   *
   * @param request HttpServletRequest
   * @throws Exception
   * @throws CustomException
   * @author yoony
   * @version 1.0
   * @see ActionCommand#execute(HttpServletRequest, Map, Map)
   * @see FileMapper#selectFile(FileDTO) 파일 조회
   */
  @Override
  public ResponseEntity execute(HttpServletRequest request) throws Exception {
    logger.debug("execute()");

    long articleId = RequestUtil.getLongParameter(request.getParameter("articleId"));
    long fileId = RequestUtil.getLongParameter(request.getParameter("fileId"));

    SqlSessionTemplate sqlSessionTemplate = dependencyCommand.getSqlSessionTemplate();
    FileMapper fileMapper = sqlSessionTemplate.getMapper(FileMapper.class);

    FileDTO fileDTOParam = new FileDTO();
    fileDTOParam.setArticleId(articleId);
    fileDTOParam.setFileId(fileId);
    FileDTO fileDTO = fileMapper.selectFile(fileDTOParam);

    if (fileDTO == null) {
      throw new CustomException(ErrorCode.FILE_NOT_FOUND);
    }
    Resource resource = new FileSystemResource(fileDTO.getFilePath() + File.separator
        + fileDTO.getFileSaveName());
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=" + fileDTO.getFileOriginName());
    return ResponseEntity.ok()
        .headers(headers)
        .contentLength(resource.contentLength())
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(resource);
  }
}
