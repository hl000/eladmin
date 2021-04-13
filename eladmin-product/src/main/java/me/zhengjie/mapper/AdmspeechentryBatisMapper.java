package me.zhengjie.mapper;


import me.zhengjie.service.dto.AdmspeechentryDto;
import me.zhengjie.service.dto.AdmspeechentryQueryCriteria;

import java.util.List;

public interface AdmspeechentryBatisMapper {
    int deleteByPrimaryKey(Integer fid);

    int insert(AdmspeechentryDto record);

    int insertSelective(AdmspeechentryDto record);

    AdmspeechentryDto selectByPrimaryKey(Integer fid);

    int updateByPrimaryKeySelective(AdmspeechentryDto record);

    int updateByPrimaryKey(AdmspeechentryDto record);

    List<AdmspeechentryDto> getAdmspeechentry(AdmspeechentryQueryCriteria criteria);

    int deleteByHeadId(Integer fHeadid);
}
