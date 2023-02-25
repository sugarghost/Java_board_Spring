package com.board.spring.yoony.file;

import java.util.List;
import java.util.Map;

public interface FileMapper {
  public int insertFile(FileDTO fileDTO);

  public List<FileDTO> selectFileList(long articleId);

  public FileDTO selectFile(FileDTO fileDTO);

  public int selectFileCount(long articleId);

  public boolean selectFileExist(long articleId);

  public int deleteFile(FileDTO fileDTO);

  public int deleteAllFile(long articleId);
}
