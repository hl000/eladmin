package me.zhengjie.repository;

import me.zhengjie.domain.Balance;
import me.zhengjie.domain.BalanceView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/4/13 16:15
 */
public interface BalanceViewRepository extends JpaRepository<BalanceView, Integer>, JpaSpecificationExecutor<BalanceView> {
}
