package me.zhengjie.utils;

import com.github.pagehelper.PageInfo;
import me.zhengjie.base.PageRequest;
import me.zhengjie.base.PageResult;

public class PageMyBatisUtil {

  /**
   * 将分页信息封装到统一的接口
   * @param page
   * @param pageInfo
   * @return
   */
  public static PageResult getPageResult(PageRequest page, PageInfo<?> pageInfo) {
    PageResult pageResult = new PageResult();
    pageResult.setPageNum(pageInfo.getPageNum());
    pageResult.setPageSize(pageInfo.getPageSize());
    pageResult.setTotalElements(pageInfo.getTotal());
    pageResult.setTotalPages(pageInfo.getPages());
    pageResult.setContent(pageInfo.getList());
    return pageResult;
  }


}
