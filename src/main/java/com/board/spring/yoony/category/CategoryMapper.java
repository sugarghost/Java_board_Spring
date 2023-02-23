package com.board.spring.yoony.category;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

  public List<CategoryDTO> selectCategoryList();
}
