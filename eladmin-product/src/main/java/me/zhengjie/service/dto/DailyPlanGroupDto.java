package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.domain.DailyPlan;

import java.util.List;

/**
 * @author HL
 * @create 2021/6/28 10:24
 */

@Data
public class DailyPlanGroupDto {
    private String startDate;

    private List<DailyPlan> dailyPlanList;

    public DailyPlanGroupDto(String key, List<DailyPlan> value) {
        this.startDate = key;
        this.dailyPlanList = value;
    }
}
