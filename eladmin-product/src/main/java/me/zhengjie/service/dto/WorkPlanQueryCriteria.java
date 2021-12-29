package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2021/12/13 10:22
 */
@Data
public class WorkPlanQueryCriteria {

    @Query
    private String planName;

    @Query
    private Integer planCode;
}
