package me.zhengjie.service;

import me.zhengjie.domain.WorkPlan;
import me.zhengjie.domain.WorkPlanDetail;
import me.zhengjie.domain.WorkPlanDetailOutputType;
import me.zhengjie.domain.WorkPlanType;
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

    List<WorkPlanType> queryAllWorkPlanType();

    List<WorkPlanDetailOutputType> queryAllOutPut();

    WorkPlanGroupDto createWorkPlan(WorkPlanGroupDto resources);

    Boolean isExist(WorkPlanGroupDto resources);

    WorkPlanGroupDto getWorkPlanDetailByPlanId(Integer id);

    WorkPlanGroupDto updateWorkPlan(WorkPlanGroupDto resources);

    List<WorkPlanDetail> getWorkPlanDetails(WorkPlanDetailQueryCriteria workPlanDetailQueryCriteria);

    Object deleteWorkPlanDetail(Integer id);

    Object deleteWorkPlan(Integer id);

    Object getWorkPlanDetailHistory(Integer id);

    Object findWorkPlanDetails(WorkPlanDetailQueryCriteria criteria, Pageable pageable);

    void downloadWorkPlanDetails(HttpServletResponse response, WorkPlanDetailQueryCriteria criteria) throws IOException;

    Object queryDutyPerson();
}
