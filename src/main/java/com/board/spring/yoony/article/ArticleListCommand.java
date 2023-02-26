package com.board.spring.yoony.article;

import com.board.spring.yoony.command.DependencyCommand;
import com.board.spring.yoony.command.MainCommand;
import com.board.spring.yoony.article.page.PageDTO;
import com.board.spring.yoony.category.CategoryMapper;
import com.board.spring.yoony.category.CategoryDTO;
import com.board.spring.yoony.util.RequestUtil;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 게시글 목록을 보여주는 커맨드
 *
 * @author yoony
 * @version 1.0
 * @see MainCommand
 * @see DependencyCommand
 * @since 2023. 02. 26.
 */
@Service
@RequiredArgsConstructor
public class ArticleListCommand implements MainCommand {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final DependencyCommand dependencyCommand;

  /**
   * 게시글 목록을 가져옴 categoryDAO.selectCategoryList()를 통해 카테고리 목록을 가져온 후 articleDAO.selectArticleList()를
   * 통해 게시글 목록을 가져온다.
   * <p>게시글 목록을 가져올 때는 검색을 위한 param을 넘겨준다.
   * <p>검색 조건은 searchWord, category, startDate, endDate, pageNum(현재 페이지) 이다.
   * <p>board/free/list 페이지로 이동
   *
   * @param request   HttpServletRequest
   * @param paramMap  Map<String, Object> 처리에 필요한 파라미터를 담은 맵
   * @param viewModel Map<String, Object> 처리 결과를 담을 맵
   * @return String 뷰 페이지
   * @version 1.0
   * @see MainCommand#execute(HttpServletRequest, Map, Map)
   * @see ArticleMapper#selectArticleCount(Map) 게시글 수 조회
   * @see ArticleMapper#selectArticleList(Map) 게시글 목록 조회
   * @see CategoryMapper#selectCategoryList() 카테고리 목록 조회
   * @since 2023. 02. 26.
   */
  @Override
  public String execute(HttpServletRequest request, Map<String, Object> paramMap,
      Map<String, Object> viewModel) {
    logger.debug("execute()");
    String viewPage = "board/free/list";
    SqlSessionTemplate sqlSessionTemplate = dependencyCommand.getSqlSessionTemplate();

    ArticleMapper articleMapper = sqlSessionTemplate.getMapper(ArticleMapper.class);

    // 페이지네이션 구현
    int totalCount = articleMapper.selectArticleCount(paramMap);
    int pageSize = 5;
    int blockSize = 10;

    int pageNum = RequestUtil.getIntParameter(request.getParameter("pageNum"));
    if (pageNum == 0) {
      pageNum = 1;
    }
    PageDTO pageDTO = new PageDTO(pageNum, pageSize, blockSize, totalCount);
    viewModel.put("pageDTO", pageDTO);
    paramMap.put("rowStart", pageDTO.getStartRowNum());
    paramMap.put("pageSize", pageSize);

    // 검색조건에 따른 게시글 가져옴
    List<ArticleDTO> articleList = articleMapper.selectArticleList(paramMap);
    viewModel.put("articleList", articleList);

    // 카테고리 목록 가져옴
    CategoryMapper categoryMapper = sqlSessionTemplate.getMapper(CategoryMapper.class);
    List<CategoryDTO> categoryList = categoryMapper.selectCategoryList();
    viewModel.put("categoryList", categoryList);
    return viewPage;
  }
}
