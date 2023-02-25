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
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileDownloadActionCommand implements DownloadCommand {


  private Logger logger = LogManager.getLogger(ArticleWriteActionCommand.class);

  private final DependencyCommand dependencyCommand;

  @Override
  public ResponseEntity execute(HttpServletRequest request) throws Exception {
    logger.debug("execute()");

    long articleId = RequestUtil.getLongParameter(request.getParameter("articleId"));
    long fileId = RequestUtil.getLongParameter(request.getParameter("fileId"));

    FileMapper fileMapper = dependencyCommand.getSqlSessionTemplate()
        .getMapper(FileMapper.class);

    FileDTO fileDTOParam = new FileDTO();
    fileDTOParam.setArticleId(articleId);
    fileDTOParam.setFileId(fileId);
    FileDTO fileDTO = fileMapper.selectFile(fileDTOParam);

    if (fileDTO == null) {
      throw new CustomException("파일 정보가 존재하지 않습니다.", ErrorCode.FILE_NOT_FOUND);
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
