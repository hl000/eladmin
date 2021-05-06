package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2021/4/13 15:09
 */
@Data
public class DailyPlanQueryCriteria {
//    @Query
//    private String batchNumber;

    @Query
    private String planNumber;

    @Query(propName = "id",joinName = "batchPlan")
    private Integer batchPlanId;

    @Query(propName = "batchNumber",joinName = "batchPlan")
    private String batchNumber;

    @Query
    private Long userId;

    @Query
    private String manufactureName;

    @Query
    private String startDate;

    @Query
    private String manufactureAddress;
}
