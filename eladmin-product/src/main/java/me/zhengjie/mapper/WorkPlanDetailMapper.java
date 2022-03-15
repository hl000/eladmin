package me.zhengjie.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.domain.WorkPlanDetail;
import me.zhengjie.service.dto.WorkPlanDetailDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author HL
 * @create 2022/1/17 9:14
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WorkPlanDetailMapper extends BaseMapper<WorkPlanDetailDto, WorkPlanDetail> {
}
