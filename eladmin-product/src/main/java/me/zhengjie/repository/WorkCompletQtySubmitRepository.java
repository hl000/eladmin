package me.zhengjie.repository;

import me.zhengjie.domain.WorkCompletQtySubmit;
import me.zhengjie.domain.WorkSubmitTimeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/12/29 15:07
 */
public interface WorkCompletQtySubmitRepository extends JpaRepository<WorkCompletQtySubmit, Integer>, JpaSpecificationExecutor<WorkCompletQtySubmit> {
}
