package me.zhengjie.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.domain.ProductParameter;
import me.zhengjie.domain.TechniqueInfo;
import me.zhengjie.service.dto.ProductParameterDto;
import me.zhengjie.service.dto.TechniqueInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author HL
 * @create 2021/4/23 11:17
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductParameterMapper extends BaseMapper<ProductParameterDto, ProductParameter> {
}
