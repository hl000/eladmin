package me.zhengjie.repository;

import me.zhengjie.domain.WorkFactoryProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2022/2/18 9:15
 */
public interface WorkFactoryProcessRepository extends JpaRepository<WorkFactoryProcess, Integer>, JpaSpecificationExecutor<WorkFactoryProcess> {
}
