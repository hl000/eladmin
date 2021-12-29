package me.zhengjie.repository;

import me.zhengjie.domain.WorkPlan;
import me.zhengjie.domain.WorkPlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/4/15 14:09
 */
public interface WorkPlanDetailRepository extends JpaRepository<WorkPlanDetail, Integer>, JpaSpecificationExecutor<WorkPlanDetail> {
}
