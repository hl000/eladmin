package me.zhengjie.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MybatisMapper {

    List<Map> getList();
}
