package me.zhengjie.repository;

import me.zhengjie.domain.Manufacture;
import me.zhengjie.domain.ManufactureSummary;
import me.zhengjie.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/4/13 18:32
 */
public interface ManufactureSummaryRepository extends JpaRepository<ManufactureSummary, Integer>, JpaSpecificationExecutor<ManufactureSummary> {


//    ManufactureSummary findBy(String username);
}
