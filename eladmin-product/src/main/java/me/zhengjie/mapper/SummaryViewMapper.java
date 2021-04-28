package me.zhengjie.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.domain.ProductParameter;
import me.zhengjie.domain.SummaryView;
import me.zhengjie.service.dto.ProductParameterDto;
import me.zhengjie.service.dto.SummaryViewDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author HL
 * @create 2021/4/27 22:21
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SummaryViewMapper extends BaseMapper<SummaryViewDto, SummaryView> {
}
