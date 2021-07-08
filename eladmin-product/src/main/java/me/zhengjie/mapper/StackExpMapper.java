package me.zhengjie.mapper;




import me.zhengjie.domain.ExpStackInfo;
import me.zhengjie.statistics.CommonStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StackExpMapper {

    Integer insertRecord(@Param("table") String table, @Param("expStackInfo") ExpStackInfo expStackInfo);

    List<ExpStackInfo> queryOne(@Param("table")String table, @Param("code")String code, @Param("base")String base);

    List<ExpStackInfo> queryAll(@Param("table")String table,  @Param("start")long start,  @Param("end")long end, @Param("base")String base);

    ExpStackInfo queryByFid(@Param("table")String table, @Param("fid")long fid);

    Integer updateRecord(@Param("table")String table,  @Param("stackInfo")ExpStackInfo stackInfo);

    List<CommonStatistics> getStatistics(@Param("table")String table, @Param("start")String start, @Param("end")String end);

    List<ExpStackInfo> queryExpStacks(@Param("table")String table,  @Param("start")String start,  @Param("end")String end, @Param("base")String base);
}
