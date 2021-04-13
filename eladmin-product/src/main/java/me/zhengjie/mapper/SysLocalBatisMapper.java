package me.zhengjie.mapper;

import me.zhengjie.service.dto.SysLocalDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface SysLocalBatisMapper {

    List<SysLocalDto> getLocal();

    Set<String> getLocalByUser(@Param("table1")String table1, @Param("table2")String tabel2, @Param("id")long id);
}
