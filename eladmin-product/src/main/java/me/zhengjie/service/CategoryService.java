package me.zhengjie.service;

import me.zhengjie.domain.Category;
import me.zhengjie.service.dto.CategoryDto;
import me.zhengjie.service.dto.CategoryQueryCriteria;

import java.util.List;

/**
 * @author HL
 * @create 2021/4/15 13:48
 */
public interface CategoryService {
    List<CategoryDto> queryAll(CategoryQueryCriteria criteria);

    Category create(Category resources);

    List<String> getProcessName();
}
