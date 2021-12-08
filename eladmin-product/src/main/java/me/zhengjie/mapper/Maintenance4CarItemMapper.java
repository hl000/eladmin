package me.zhengjie.mapper;

import me.zhengjie.domain.FixRecordInfo;
import me.zhengjie.domain.Maintenance4CarItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author HL
 * @create 2021/9/14 10:31
 */
public interface Maintenance4CarItemMapper {
    List<Maintenance4CarItem> queryAll(@Param("table") String table);
}
