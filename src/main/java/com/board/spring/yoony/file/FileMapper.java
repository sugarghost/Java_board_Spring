package com.board.spring.yoony.file;

import java.util.List;
import java.util.Map;

public interface FileMapper {
  public int insertFile(FileDTO fileDTO);

  public List<FileDTO> selectFileList(int articleId);

  public FileDTO selectFile(Map<String, Integer> params);

  public int selectFileCount(int articleId);

  public boolean selectFileExist(int articleId);

  public int deleteFile(FileDTO fileDTO);

  public int deleteAllFile(int articleId);
}
