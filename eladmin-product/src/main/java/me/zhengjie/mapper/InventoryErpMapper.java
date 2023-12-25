package me.zhengjie.mapper;

import me.zhengjie.annotation.DatabaseType;
import me.zhengjie.annotation.DynamicDao;
import me.zhengjie.domain.InventoryErp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author HL
 * @create 2023/8/31 15:38
 */
@DynamicDao(type = DatabaseType.third)
public interface InventoryErpMapper {

    List<InventoryErp> queryInventoryErp(@Param("code") String code,@Param("name") String name,@Param("specs") String specs);

}
