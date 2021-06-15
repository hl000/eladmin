package me.zhengjie.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.domain.StackRemainView;
import me.zhengjie.domain.StackWorkView;
import me.zhengjie.service.dto.StackSummary;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author HL
 * @create 2021/4/23 11:17
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StackRemainMapper extends BaseMapper<StackSummary, StackRemainView> {
}
