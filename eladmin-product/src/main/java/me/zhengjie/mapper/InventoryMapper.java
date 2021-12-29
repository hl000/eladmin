package me.zhengjie.mapper;

import me.zhengjie.annotation.DatabaseType;
import me.zhengjie.annotation.DynamicDao;
import me.zhengjie.service.dto.InventoryDto;
import me.zhengjie.service.dto.WorkStepDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author HL
 * @create 2021/12/27 20:29
 */
@DynamicDao(type = DatabaseType.second)
public interface InventoryMapper {
    List<InventoryDto> queryInventory(@Param("table") String table);

    List<InventoryDto> getInventoryByCode(@Param("table") String table, @Param("cInvCode") String cInvCode);
}
