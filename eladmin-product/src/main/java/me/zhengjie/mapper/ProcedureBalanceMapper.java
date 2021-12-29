package me.zhengjie.mapper;

import me.zhengjie.annotation.DatabaseType;
import me.zhengjie.annotation.DynamicDao;
import me.zhengjie.service.dto.ProcedureBalanceDto;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author HL
 * @create 2021/12/15 10:18
 */
@DynamicDao(type = DatabaseType.second)
public interface ProcedureBalanceMapper {
    List<ProcedureBalanceDto> getProcedureBalance( @Param("arcName") String arcName);
}
