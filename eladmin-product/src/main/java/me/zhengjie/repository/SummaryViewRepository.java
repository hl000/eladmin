package me.zhengjie.repository;

import me.zhengjie.domain.SummaryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/4/23 11:18
 */
public interface SummaryViewRepository extends JpaRepository<SummaryView, Integer>, JpaSpecificationExecutor<SummaryView> {
}
