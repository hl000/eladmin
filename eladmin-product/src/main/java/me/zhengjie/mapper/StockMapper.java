package me.zhengjie.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.domain.Manufacture;
import me.zhengjie.domain.Stock;
import me.zhengjie.service.dto.ManufactureDto;
import me.zhengjie.service.dto.StockDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author HL
 * @create 2021/4/13 18:32
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StockMapper extends BaseMapper<StockDto, Stock> {
}
