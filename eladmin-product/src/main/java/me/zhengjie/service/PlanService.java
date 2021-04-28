package me.zhengjie.service;

import me.zhengjie.domain.BatchPlan;
import me.zhengjie.domain.DailyPlan;
import me.zhengjie.service.dto.*;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author HL
 * @create 2021/4/14 14:57
 */
public interface PlanService {


    void updateBatchPlan(BatchPlan resources);

    BatchPlan createBatchPlan(BatchPlan resources);

    void updateDailyPlan(DailyPlan resources) throws ParseException;

    DailyPlan createDailyPlan(DailyPlan resources);

    List<BatchPlanDto>  findBatchPlan(BatchPlanQueryCriteria criteria);

    List<DailyPlanDto>  findDailyPlan(DailyPlanQueryCriteria criteria);

    Map<String,Object> queryDailyPlan(DailyPlanQueryCriteria criteria, Pageable pageable);

    Map<String,Object>  queryBatchPlan(BatchPlanQueryCriteria criteria, Pageable pageable);

    void download(HttpServletResponse response, BatchPlanQueryCriteria criteria);

    void downloadDailyPlan(HttpServletResponse response, DailyPlanQueryCriteria criteria);

    List<DailyPlanDto> getDailyPlanSelector();

    List<RemainBatchQuantityDto> getRemainBatchQuantity(ProductParameterQueryCriteria criteria);

    List<DailyPlan> createDailyPlanBatch(DailyPlanBatchDto resources);
}
