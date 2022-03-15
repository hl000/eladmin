package me.zhengjie.mapper;

import io.lettuce.core.dynamic.annotation.Param;
import me.zhengjie.annotation.DatabaseType;
import me.zhengjie.annotation.DynamicDao;
import me.zhengjie.domain.WorkCompletQtySubmit;

import java.util.List;
import java.util.Map;

/**
 * @author HL
 * @create 2022/1/4 10:14
 */
@DynamicDao(type = DatabaseType.jpaDatasource)
public interface WorkListMapper {

    List<Map<String, Object>> findWorkList(@Param("workShop") String workShop, @Param("fArcName") String fArcName, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("workOrder") String workOrder, @Param("displayModel") Integer displayModel);

    int insert(WorkCompletQtySubmit workCompletQtySubmit);

    int update(WorkCompletQtySubmit workCompletQtySubmit);
}
