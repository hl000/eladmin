package me.zhengjie.repository;

import me.zhengjie.domain.BalanceLog;
import me.zhengjie.domain.StackWorkView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/4/13 16:15
 */
public interface StackWorkViewRepository extends JpaRepository<StackWorkView, String>, JpaSpecificationExecutor<StackWorkView> {
}
