package me.zhengjie.repository;

import me.zhengjie.domain.BatchPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/4/14 16:48
 */
public interface BatchPlanRepository extends JpaRepository<BatchPlan, Integer>, JpaSpecificationExecutor<BatchPlan> {
}
