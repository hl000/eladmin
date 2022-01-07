package me.zhengjie.repository;

import me.zhengjie.domain.WorkPlanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/12/30 8:37
 */
public interface WorkPlanTypeRepository extends JpaRepository<WorkPlanType, Integer>, JpaSpecificationExecutor<WorkPlanType> {
}
