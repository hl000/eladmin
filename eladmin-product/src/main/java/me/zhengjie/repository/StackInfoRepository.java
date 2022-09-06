package me.zhengjie.repository;

import me.zhengjie.domain.StackInfo;
import me.zhengjie.domain.StackReplaceInfo;
import me.zhengjie.domain.UpdateStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author HL
 * @create 2021/4/15 14:09
 */
public interface StackInfoRepository extends JpaRepository<StackInfo, Integer>, JpaSpecificationExecutor<StackInfo> {
}
