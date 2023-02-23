package com.board.spring.yoony.article.page;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class PageDTO {

  private int pageNum;
  private int pageSize;
  private int blockPage;
  private int totalCount;
  private int totalPage;
  private int startPageNum;
  private int endPageNum;
  private int startRowNum;

  public PageDTO(int pageNum, int pageSize, int blockPage, int totalCount) {
    this.pageNum = pageNum;
    this.pageSize = pageSize;
    this.blockPage = blockPage;
    this.totalCount = totalCount;
    // 전체 페이지 수 = 전체 게시물 수 / 페이지당 보여줄 게시물 수
    this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
    // 페이지 블록의 시작 페이지 번호 = (((현재 페이지 번호 - 1) / 페이지 블록당 보여줄 페이지 수) * 페이지 블록당 보여줄 페이지 수) + 1
    this.startPageNum = (((pageNum - 1) / blockPage) * blockPage) + 1;
    // 페이지 블록의 끝 페이지 번호 = 시작 페이지 번호 + 페이지 블록당 보여줄 페이지 수 - 1
    this.endPageNum = startPageNum + blockPage - 1;
    // 페이지 번호에 따른 시작 게시물 번호 = (현재 페이지 번호 - 1) * 페이지당 보여줄 게시물 수
    this.startRowNum = (pageNum - 1) * pageSize;
  }
}
