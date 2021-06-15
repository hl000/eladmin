package me.zhengjie.repository;

import me.zhengjie.domain.BalanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/4/13 16:15
 */
public interface BalanceLogRepository extends JpaRepository<BalanceLog, Integer>, JpaSpecificationExecutor<BalanceLog> {
}
