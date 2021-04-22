package me.zhengjie.repository;

import me.zhengjie.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/4/15 14:09
 */
public interface CategoryRepository  extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {
}
