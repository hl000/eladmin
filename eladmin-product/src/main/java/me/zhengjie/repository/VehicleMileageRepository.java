package me.zhengjie.repository;

import me.zhengjie.domain.VehicleMileage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/4/15 14:09
 */
public interface VehicleMileageRepository extends JpaRepository<VehicleMileage, Integer>, JpaSpecificationExecutor<VehicleMileage> {
}
