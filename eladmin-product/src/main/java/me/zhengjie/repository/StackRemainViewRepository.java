package me.zhengjie.repository;

import me.zhengjie.domain.StackRemainView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author HL
 * @create 2021/4/13 16:15
 */
public interface StackRemainViewRepository extends JpaRepository<StackRemainView, String> {
    @Query(value = "select * from view_stack_remain", nativeQuery = true)
    List<StackRemainView> findStackRemainViews();
}