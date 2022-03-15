package me.zhengjie.repository;

import me.zhengjie.domain.WorkShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/4/15 14:09
 */
public interface WorkShopRepository extends JpaRepository<WorkShop, Integer>, JpaSpecificationExecutor<WorkShop> {
}
