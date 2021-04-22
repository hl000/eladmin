package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2021/4/13 15:09
 */
@Data
public class CategoryQueryCriteria {

    @Query
    private String primaryType;

    @Query
    private String secondaryType;
}
