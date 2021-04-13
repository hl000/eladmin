package me.zhengjie.base;

public class PageRequest {

  /**
   * 当前页码
   */
  private int page;

  /**
   * 每页数量
   */
  private int size;

  public int getPage() {
    return page;
  }

  public void setPage(int pageNum) {
    this.page = pageNum;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

}
