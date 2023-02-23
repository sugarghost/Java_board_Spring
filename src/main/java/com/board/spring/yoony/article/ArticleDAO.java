package com.board.spring.yoony.article;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface ArticleDAO {

  int selectArticleCount(Map<String, Object> params);

  List<ArticleDTO> selectArticleList(Map<String, Object> params);

}
