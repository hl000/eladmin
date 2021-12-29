package me.zhengjie.mapper;

import me.zhengjie.annotation.DatabaseType;
import me.zhengjie.annotation.DynamicDao;
import me.zhengjie.service.dto.WorkStepDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author HL
 * @create 2021/12/23 16:44
 */
@DynamicDao(type = DatabaseType.second)
public interface WorkStepMapper {
//    @DynamicDao(type = DatabaseType.second)
    List<WorkStepDto> queryWorkStep(@Param("table") String table);
}
