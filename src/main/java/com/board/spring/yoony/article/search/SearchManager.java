package com.board.spring.yoony.article.search;

import com.board.spring.yoony.util.ValidationChecker;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 검색 조건을 관리하기 위한 클래스 기존에 JSP include를 통해 검색 조건을 관리하던 방식에서 변경함
 *
 * @author yoony
 * @version 1.0
 * @since 2023. 02. 26.
 */
@Getter
@Setter
@ToString
public class SearchManager {

  /**
   * 현재 페이지 번호
   */
  private String pageNum;
  /**
   * 검색어(제목, 내용, 작성자)
   */
  private String searchWord;
  /**
   * 카테고리 아이디
   */
  private String categoryId;
  /**
   * 검색 시작 날짜
   */
  private String startDate;
  /**
   * 검색 종료 날짜
   */
  private String endDate;

  /**
   * 검색 조건을 관리하기 위한 생성자
   * <p>검색 조건 String을 가져와 필드에 저장함
   *
   * @param pageNum 현재 페이지 번호
   * @param searchWord 검색어(제목, 내용, 작성자)
   * @param categoryId 카테고리 아이디
   * @param startDate 검색 시작 날짜
   * @param endDate 검색 종료 날짜
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  public SearchManager(String pageNum, String searchWord, String categoryId, String startDate,
      String endDate) {
    this.pageNum = !ValidationChecker.isEmpty(pageNum) ? pageNum : "";
    this.searchWord = !ValidationChecker.isEmpty(searchWord) ? searchWord : "";
    this.categoryId = !ValidationChecker.isEmpty(categoryId) ? categoryId : "";
    this.startDate = !ValidationChecker.isEmpty(startDate) ? startDate : "";
    this.endDate = !ValidationChecker.isEmpty(endDate) ? endDate : "";
  }


  /**
   * 저장된 검색 조건을 Map으로 반환함
   *
   * @return 검색 조건을 저장한 Map 객체
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  public Map<String, Object> getSearchParamsMap() {
    Map<String, Object> searchParams = new HashMap<>();
    if (!ValidationChecker.isEmpty(pageNum)) {
      searchParams.put("pageNum", pageNum);
    }
    if (!ValidationChecker.isEmpty(searchWord)) {
      searchParams.put("searchWord", searchWord);
    }
    if (!ValidationChecker.isEmpty(categoryId)) {
      searchParams.put("categoryId", categoryId);
    }
    if (!ValidationChecker.isEmpty(startDate)) {
      searchParams.put("startDate", startDate);
    }
    if (!ValidationChecker.isEmpty(endDate)) {
      searchParams.put("endDate", endDate);
    }
    return searchParams;
  }

  /**
   * 저장된 검색 조건을 Url에 붙여서 사용하기 위한 String을 생성함
   *
   * @return String 저장된 검색 조건을 붙여서 만든 query String
   * @throws UnsupportedEncodingException the unsupported encoding exception
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  public String getSearchParamsQuery() throws UnsupportedEncodingException {
    String searchParams = "";
    if (!ValidationChecker.isEmpty(pageNum)) {
      searchParams += "&pageNum=" + pageNum;
    }
    if (!ValidationChecker.isEmpty(searchWord)) {
      searchParams += "&searchWord=" + URLEncoder.encode(searchWord, "UTF-8");
    }
    if (!ValidationChecker.isEmpty(categoryId)) {
      searchParams += "&categoryId=" + categoryId;
    }
    if (!ValidationChecker.isEmpty(startDate)) {
      searchParams += "&startDate=" + startDate;
    }
    if (!ValidationChecker.isEmpty(endDate)) {
      searchParams += "&endDate=" + endDate;
    }
    return searchParams;
  }


  /**
   * 저장된 검색 조건을 Url에 붙여서 사용하기 위한 String을 생성함
   * <p>pageNum을 제외한 나머지 검색 조건을 붙여서 만듬
   * <p>Paging.jsp에서는 이미 PageNum을 조건으로 붙이기 때문에 중복이 발생해서 따로 만듬
   * <p>기존 방식으로도 동작은 하지만 보기 안좋아서 따로 만듬
   * <p>좀더 좋은 설계가 있겠지만 이 문제로 오래 고민하는 대신 다른 기능 작업하려는 판단
   *
   * @return String 저장된 검색 조건을 붙여서 만든 query String
   * @throws UnsupportedEncodingException the unsupported encoding exception
   * @author yoony
   * @version 1.0
   * @since 2023. 02. 26.
   */
  public String getSearchParamsQueryWithOutPageNum() throws UnsupportedEncodingException {
    String searchParams = "";
    if (!ValidationChecker.isEmpty(searchWord)) {
      searchParams += "&searchWord=" + URLEncoder.encode(searchWord, "UTF-8");
    }
    if (!ValidationChecker.isEmpty(categoryId)) {
      searchParams += "&categoryId=" + categoryId;
    }
    if (!ValidationChecker.isEmpty(startDate)) {
      searchParams += "&startDate=" + startDate;
    }
    if (!ValidationChecker.isEmpty(endDate)) {
      searchParams += "&endDate=" + endDate;
    }
    return searchParams;
  }
}
