package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.domain.BatchPlan;
import me.zhengjie.domain.DailyPlan;

import java.io.Serializable;
import java.util.List;

/**
 * @author HL
 * @create 2021/4/25 9:55
 */
@Data
public class DailyPlanBatchDto implements Serializable {

    /**
     * 计划生产日期
     **/
    private String startDate;

    /**
     * 批量计划
     */
    private BatchPlan batchPlan;


    private List<DailyPlan> dailyPlanList;
}
