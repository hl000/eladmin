package me.zhengjie.repository;

import me.zhengjie.domain.ProductionReportDetailsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/4/23 11:18
 */
public interface ProductionReportDetailsViewRepository extends JpaRepository<ProductionReportDetailsView,String>, JpaSpecificationExecutor<ProductionReportDetailsView>{
}
