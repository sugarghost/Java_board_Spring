package com.board.spring.yoony.article;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper {

  public int insertArticle(ArticleDTO articleDTO);

  int selectArticleCount(Map<String, Object> params);

  List<ArticleDTO> selectArticleList(Map<String, Object> params);

}
