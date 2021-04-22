package me.zhengjie.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.domain.BatchPlan;
import me.zhengjie.service.dto.BatchPlanDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author HL
 * @create 2021/4/14 15:19
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BatchPlanMapper extends BaseMapper<BatchPlanDto, BatchPlan> {
//    int insert(BatchPlan record);
}

