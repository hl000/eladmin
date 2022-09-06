package me.zhengjie.repository;

import me.zhengjie.domain.Manufacture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/4/13 18:32
 */
public interface ManufactureRepository extends JpaRepository<Manufacture, Integer>, JpaSpecificationExecutor<Manufacture> {
}
