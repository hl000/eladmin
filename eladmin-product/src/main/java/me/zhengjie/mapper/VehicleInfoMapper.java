package me.zhengjie.mapper;

import me.zhengjie.domain.VehicleInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author HL
 * @create 2021/9/14 10:32
 */
public interface VehicleInfoMapper {
    List<VehicleInfo> queryAll(@Param("table") String table);
}
