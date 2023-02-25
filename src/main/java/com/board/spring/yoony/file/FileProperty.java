package com.board.spring.yoony.file;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 설명
 *
 * @author YK
 * @version 1.0
 * @fileName FileProperty
 * @since 2023-02-24
 */
@Getter
@Service
public class FileProperty {

  @Value("${spring.servlet.multipart.location}")
  private String uploadPath;

  @Value("${spring.servlet.multipart.max-file-size}")
  private long maxSize;

}
