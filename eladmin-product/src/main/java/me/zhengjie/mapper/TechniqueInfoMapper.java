package me.zhengjie.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.domain.TechniqueInfo;
import me.zhengjie.service.dto.TechniqueInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author HL
 * @create 2021/4/13 16:15
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TechniqueInfoMapper extends BaseMapper<TechniqueInfoDto, TechniqueInfo> {
//    int insert(TechniqueInfoDto record);
}
