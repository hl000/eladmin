package me.zhengjie.base;

import java.util.List;

public class PageResult<T> {

  /**
   * 当前页码
   */
  private int pageNum;
  /**
   * 每页数量
   */
  private int pageSize;
  /**
   * 记录总数
   */
  private long totalElements;
  /**
   * 页码总数
   */
  private int totalPages;
  /**
   * 数据模型
   */
  private List<?> content;
  public int getPageNum() {
    return pageNum;
  }
  public void setPageNum(int pageNum) {
    this.pageNum = pageNum;
  }
  public int getPageSize() {
    return pageSize;
  }
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }
  public long getTotalElements() {
    return totalElements;
  }
  public void setTotalElements(long totalElements) {
    this.totalElements = totalElements;
  }
  public int getTotalPages() {
    return totalPages;
  }
  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }
  public List<?> getContent() {
    return content;
  }
  public void setContent(List<?> content) {
    this.content = content;
  }

}
