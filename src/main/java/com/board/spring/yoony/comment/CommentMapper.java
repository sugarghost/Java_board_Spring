package com.board.spring.yoony.comment;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {
  int insertComment(CommentDTO commentDTO);

  List<CommentDTO> selectCommentList(long articleId);
}
