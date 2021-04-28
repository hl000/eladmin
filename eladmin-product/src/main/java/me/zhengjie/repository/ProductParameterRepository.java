package me.zhengjie.repository;

import me.zhengjie.domain.ProductParameter;
import me.zhengjie.domain.TechniqueInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/4/23 11:18
 */
public interface ProductParameterRepository extends JpaRepository<ProductParameter, Integer>, JpaSpecificationExecutor<ProductParameter> {
}
