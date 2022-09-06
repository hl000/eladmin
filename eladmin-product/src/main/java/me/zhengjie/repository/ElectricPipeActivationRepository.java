package me.zhengjie.repository;

import me.zhengjie.domain.ElectricPipeActivation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2022/8/30 15:11
 */
public interface ElectricPipeActivationRepository extends JpaRepository<ElectricPipeActivation, Integer>, JpaSpecificationExecutor<ElectricPipeActivation> {
}
