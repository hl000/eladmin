package me.zhengjie.repository;

import me.zhengjie.domain.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2023/3/28 12:00
 */
public interface VisitRepository  extends JpaRepository<Visit, Integer>, JpaSpecificationExecutor<Visit> {
}
