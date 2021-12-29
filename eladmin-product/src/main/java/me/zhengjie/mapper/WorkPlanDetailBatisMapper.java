package me.zhengjie.mapper;

import me.zhengjie.domain.WorkPlanDetail;
import me.zhengjie.service.dto.WorkPlanDetailQueryCriteria;

import java.util.List;

/**
 * @author HL
 * @create 2021/12/15 10:18
 */
public interface WorkPlanDetailBatisMapper{
    List<WorkPlanDetail> getWorkPlanDetail(WorkPlanDetailQueryCriteria workPlanDetailQueryCriteria);
}
