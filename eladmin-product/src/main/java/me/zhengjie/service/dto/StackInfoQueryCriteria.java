package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2021/5/10 9:44
 */
@Data
public class StackInfoQueryCriteria {

    @Query
    private String FNumber;
}
