package com.board.spring.yoony.comment;

import java.sql.Date;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("CommentDTO")
public class CommentDTO {

  // 댓글 id(auto increment)
  private long commentId;
  // 게시글 id(foreign key: article table)
  private long articleId;
  // 내용
  private String content;
  // 작성일(current_timestamp())
  private Date createdDate;

  public boolean isContentValid() {
    return content != null && !content.isEmpty() && content.matches("^.{1,255}$");
  }
}
