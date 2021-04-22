package me.zhengjie.repository;

import io.swagger.annotations.ApiModelProperty;
import me.zhengjie.domain.Manufacture;
import me.zhengjie.domain.TechniqueInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.Column;

/**
 * @author HL
 * @create 2021/4/13 18:32
 */
public interface ManufactureRepository extends JpaRepository<Manufacture, Integer>, JpaSpecificationExecutor<Manufacture> {
}
