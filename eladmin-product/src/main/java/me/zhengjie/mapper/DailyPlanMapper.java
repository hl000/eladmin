package me.zhengjie.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.domain.DailyPlan;
import me.zhengjie.service.dto.DailyPlanDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author HL
 * @create 2021/4/14 15:20
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DailyPlanMapper extends BaseMapper<DailyPlanDto, DailyPlan> {
//    int insert(DailyPlan record);
}