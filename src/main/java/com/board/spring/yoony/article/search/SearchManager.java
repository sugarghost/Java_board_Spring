package com.board.spring.yoony.article.search;

import com.board.spring.yoony.util.ValidationChecker;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class SearchManager {

  private String pageNum;
  private String searchWord;
  private String categoryId;
  private String startDate;
  private String endDate;

  public SearchManager(String pageNum, String searchWord, String categoryId, String startDate,
      String endDate) {
    this.pageNum = !ValidationChecker.isEmpty(pageNum) ? pageNum : "";
    this.searchWord = !ValidationChecker.isEmpty(searchWord) ? searchWord : "";
    this.categoryId = !ValidationChecker.isEmpty(categoryId) ? categoryId : "";
    this.startDate = !ValidationChecker.isEmpty(startDate) ? startDate : "";
    this.endDate = !ValidationChecker.isEmpty(endDate) ? endDate : "";
  }

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
