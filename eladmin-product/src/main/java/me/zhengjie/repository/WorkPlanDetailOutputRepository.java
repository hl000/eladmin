package me.zhengjie.repository;

import me.zhengjie.domain.WorkPlanDetail;
import me.zhengjie.domain.WorkPlanDetailOutputType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/4/15 14:09
 */
public interface WorkPlanDetailOutputRepository extends JpaRepository<WorkPlanDetailOutputType, Integer>, JpaSpecificationExecutor<WorkPlanDetailOutputType> {
}
