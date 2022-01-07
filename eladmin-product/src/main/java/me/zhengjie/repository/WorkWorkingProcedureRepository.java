package me.zhengjie.repository;

import me.zhengjie.domain.WorkWorkingProcedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/12/29 15:11
 */
public interface WorkWorkingProcedureRepository extends JpaRepository<WorkWorkingProcedure, Integer>, JpaSpecificationExecutor<WorkWorkingProcedure> {
}
