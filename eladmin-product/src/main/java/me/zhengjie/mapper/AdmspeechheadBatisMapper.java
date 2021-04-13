package me.zhengjie.mapper;


import me.zhengjie.service.dto.AdmspeechheadDto;
import me.zhengjie.service.dto.AdmspeechheadQueryCriteria;

import java.util.List;

public interface AdmspeechheadBatisMapper {

    int deleteByPrimaryKey(Integer fId);

    int insert(AdmspeechheadDto record);

    int insertSelective(AdmspeechheadDto record);

    AdmspeechheadDto selectByPrimaryKey(Integer fId);

    int updateByPrimaryKeySelective(AdmspeechheadDto record);

    int updateByPrimaryKey(AdmspeechheadDto record);

    List<AdmspeechheadDto> getAdmspeechhead(AdmspeechheadQueryCriteria record);

    List<AdmspeechheadDto> getAdmspeechheadByAddress(AdmspeechheadQueryCriteria record);
}
