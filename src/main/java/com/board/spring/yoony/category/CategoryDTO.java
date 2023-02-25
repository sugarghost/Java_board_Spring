package com.board.spring.yoony.category;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;


@Data
@Alias("CategoryDTO")
public class CategoryDTO {

  // 카테고리 ID(auto increment)
  private long categoryId;
  // 카테고리 이름
  private String name;

}
