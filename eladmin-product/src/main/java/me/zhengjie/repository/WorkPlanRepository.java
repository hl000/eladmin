package me.zhengjie.repository;

import me.zhengjie.domain.Category;
import me.zhengjie.domain.WorkPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/4/15 14:09
 */
public interface WorkPlanRepository extends JpaRepository<WorkPlan, Integer>, JpaSpecificationExecutor<WorkPlan> {
}
