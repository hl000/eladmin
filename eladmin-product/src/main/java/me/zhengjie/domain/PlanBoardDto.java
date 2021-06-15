package me.zhengjie.domain;

import lombok.Data;

/**
 * @author HL
 * @create 2021/6/15 7:51
 */
@Data
public class PlanBoardDto {

    private String startDate;

    private String endDate;

//    private Integer batchPlanQuantity;

    private String batchPlanName;

    private String manufactureAddress;

    private String manufactureName;

    private Integer completedQuantity;

    private Integer remainQuantity;

    private Integer todayPlan;

    private Integer tomorrowPlan;

    private Integer planQuantity;
}
