package me.zhengjie.repository;

import me.zhengjie.domain.WorkGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2022/9/13 17:49
 */
public interface WorkGroupRepository extends JpaRepository<WorkGroup, Integer>, JpaSpecificationExecutor<WorkGroup> {
}
