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

/**
 * 게시글 작성 페이지를 처리하는 커맨드
 *
 * @author yoony
 * @version 1.0
 * @see MainCommand
 * @see DependencyCommand
 * @since 2023. 02. 26.
 */
@Service
@RequiredArgsConstructor
public class ArticleWriteCommand implements MainCommand {

  private Logger logger = LogManager.getLogger(this.getClass());


  private final DependencyCommand dependencyCommand;

  /**
   * 게시글 작성 페이지를 처리한다.
   * <p>카테고리 목록을 가져옴
   * <p>board/free/write 페이지로 이동
   *
   * @param request   HttpServletRequest
   * @param paramMap  Map<String, Object> 처리에 필요한 파라미터를 담은 맵
   * @param viewModel Map<String, Object> 처리 결과를 담을 맵
   * @return String 뷰 페이지
   * @throws Exception
   * @see MainCommand#execute(HttpServletRequest, Map, Map)
   * @see CategoryMapper#selectCategoryList() 카테고리 목록 조회
   * @since 2023. 02. 26.
   */
  @Override
  public String execute(HttpServletRequest request, Map<String, Object> paramMap,
      Map<String, Object> viewModel) {
    logger.debug("execute()");
    String viewPage = "board/free/write";
    // MyBatis mapper 가져옴
    SqlSessionTemplate sqlSessionTemplate = dependencyCommand.getSqlSessionTemplate();
    CategoryMapper categoryMapper = sqlSessionTemplate.getMapper(CategoryMapper.class);

    List<CategoryDTO> categoryList = categoryMapper.selectCategoryList();
    request.setAttribute("categoryList", categoryList);
    return viewPage;
  }
}
