package com.board.spring.yoony.article;

import com.board.spring.yoony.category.CategoryDTO;
import com.board.spring.yoony.category.CategoryMapper;
import com.board.spring.yoony.command.DependencyCommand;
import com.board.spring.yoony.command.MainCommand;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleWriteCommand implements MainCommand {

  private Logger logger = LogManager.getLogger(this.getClass());


  private final DependencyCommand dependencyCommand;

  @Override
  public String execute(HttpServletRequest request, Map<String, Object> paramMap,
      Map<String, Object> viewModel) {
    logger.debug("execute()");
    String viewPage = "board/free/write";
    // MyBatis mapper 가져옴
    CategoryMapper categoryMapper = dependencyCommand.getSqlSessionTemplate().getMapper(CategoryMapper.class);

    List<CategoryDTO> categoryList = categoryMapper.selectCategoryList();
    request.setAttribute("categoryList", categoryList);
    return viewPage;
  }
}
