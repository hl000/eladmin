package me.zhengjie.mapper;




import me.zhengjie.domain.ExpStackInfo;
import me.zhengjie.domain.ExpSystemInfo;
import me.zhengjie.statistics.CommonStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SystemExpMapper {

    Integer insertRecord(@Param("table") String table, @Param("expStackInfo") ExpSystemInfo expSystemInfo);

    List<ExpSystemInfo> queryOne(@Param("table") String table, @Param("code") String code, @Param("base") String base);

    List<ExpSystemInfo> queryAll(@Param("table") String table, @Param("start") long start, @Param("end") long end, @Param("base") String base);

    ExpSystemInfo queryByFid(@Param("table") String table, @Param("fid") long fid);

    Integer updateRecord(@Param("table") String table, @Param("stackInfo") ExpSystemInfo expSystemInfo);

    List<CommonStatistics> getStatistics(@Param("table") String table, @Param("start") String start, @Param("end") String end);

}
