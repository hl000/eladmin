package me.zhengjie.repository;

import me.zhengjie.domain.FuelCellComponents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/4/15 14:09
 */
public interface FuelCellComponentsRepository extends JpaRepository<FuelCellComponents, Integer>, JpaSpecificationExecutor<FuelCellComponents> {
}
