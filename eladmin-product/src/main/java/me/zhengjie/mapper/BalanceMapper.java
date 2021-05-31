package me.zhengjie.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.domain.Balance;
import me.zhengjie.service.dto.BalanceDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author HL
 * @create 2021/4/13 18:32
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BalanceMapper extends BaseMapper<BalanceDto, Balance> {
}
