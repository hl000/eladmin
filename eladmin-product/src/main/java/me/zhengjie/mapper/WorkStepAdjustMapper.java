package me.zhengjie.mapper;

import me.zhengjie.annotation.DatabaseType;
import me.zhengjie.annotation.DynamicDao;
import me.zhengjie.domain.ExpSystemInfo;
import me.zhengjie.domain.WorkStepAdjust;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author HL
 * @create 2021/12/27 20:41
 */
@DynamicDao(type = DatabaseType.second)
public interface WorkStepAdjustMapper {
    void insertWorkStepAdjust(@Param("table") String table, @Param("workStepAdjust") WorkStepAdjust workStepAdjust);

    void updateWorkStepAdjust(@Param("table") String table,  @Param("workStepAdjust") WorkStepAdjust workStepAdjust);

    List<WorkStepAdjust> getWorkStepAdjust(@Param("table") String table, @Param("arcCode") String arcCode, @Param("invCode") String invCode);

    WorkStepAdjust getWorkStepAdjustById(@Param("table") String table, @Param("id") Integer id);
}
