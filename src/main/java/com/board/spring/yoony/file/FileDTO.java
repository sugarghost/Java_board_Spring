package com.board.spring.yoony.file;

import java.sql.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FileDTO {

  // 파일 ID(auto increment)
  private long fileId;
  // 게시글 ID(foreign key: article table)
  private long articleId;
  // 원본 파일명(사용자가 등록한 당시 파일명)
  private String fileOriginName;
  // 저장 파일명(서버에 저장된 파일명으로 UUID를 사용)
  private String fileSaveName;
  // 파일 타입
  private String fileType;
  // 파일 경로
  private String filePath;
  // 파일 등록일 (current_timestamp())
  private Date createdDate;
}
