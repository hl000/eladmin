package me.zhengjie.service;

import me.zhengjie.domain.WorkPlan;
import me.zhengjie.domain.WorkPlanDetail;
import me.zhengjie.domain.WorkPlanDetailOutputType;
import me.zhengjie.service.dto.WorkPlanDetailQueryCriteria;
import me.zhengjie.service.dto.WorkPlanGroupDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author HL
 * @create 2021/12/13 10:21
 */
public interface WorkPlanService {
    List<WorkPlan> queryAllWorkPlan();

    List<WorkPlanDetailOutputType> queryAllOutPut();

    Integer createWorkPlan(WorkPlanGroupDto resources);

    WorkPlanGroupDto isExist(WorkPlanGroupDto resources);

    WorkPlanGroupDto getWorkPlanDetailByPlanId(Integer id);

    Integer updateWorkPlan(WorkPlanGroupDto resources);

    List<WorkPlanDetail> getWorkPlanDetails(WorkPlanDetailQueryCriteria workPlanDetailQueryCriteria);

    void deleteWorkPlanDetail(Integer id);

    void deleteWorkPlan(Integer id);

    Object getWorkPlanDetailHistory(Integer id);

    Object findWorkPlanDetails(WorkPlanDetailQueryCriteria criteria, Pageable pageable);

    void downloadWorkPlanDetails(HttpServletResponse response, WorkPlanDetailQueryCriteria criteria) throws IOException;
}
