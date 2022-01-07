package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2021/12/30 10:14
 */
@Data
public class WorkPlanTypeQueryCriteria {
    @Query
    private Integer row;
}
