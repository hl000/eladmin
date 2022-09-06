package me.zhengjie.service;

import me.zhengjie.domain.UpdateStack;
import me.zhengjie.service.dto.UpdateStackQueryCriteria;

import java.util.List;

/**
 * @author HL
 * @create 2021/5/11 15:54
 */
public interface UpdateStackService {
    List<UpdateStack> findUpdateStackByDate(UpdateStackQueryCriteria criteria);
}