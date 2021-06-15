package me.zhengjie.repository;

import me.zhengjie.domain.BalanceLog;
import me.zhengjie.domain.Reasons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/4/13 16:15
 */
public interface ReasonRepository extends JpaRepository<Reasons, Integer>, JpaSpecificationExecutor<Reasons> {
}
