package me.zhengjie.mapper;



import me.zhengjie.domain.FixRecordInfo;
import me.zhengjie.domain.StackReplaceInfo;
import me.zhengjie.statistics.CommonStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FixHistoryMapper {


    List<FixRecordInfo> queryOne(@Param("table") String table, @Param("car") String car, @Param("base") String base);

    List<FixRecordInfo> queryAll(@Param("table") String table, @Param("start") String start, @Param("end") String end, @Param("base") String base);

    FixRecordInfo queryByFid(@Param("table") String table, @Param("id") long id);

    StackReplaceInfo queryStackReplaceById(@Param("table") String table, @Param("id") long id);

    List<StackReplaceInfo> queryStackReplaceOne(@Param("table") String table, @Param("car") String car, @Param("base") String base );

    List<StackReplaceInfo> queryStackReplaceAll(@Param("table") String table, @Param("start") String start, @Param("end") String end, @Param("base") String base);

    List<StackReplaceInfo> queryByStack(@Param("table") String table, @Param("code") String code);

    Integer insertCarFixRecord(@Param("table")String table, @Param("fixInfo") FixRecordInfo fixRecordInfo);

    Integer insertStackFixRecord(@Param("table")String table, @Param("fixInfo") StackReplaceInfo stackReplaceInfo);

    Integer updateCarFixRecord(@Param("table")String table, @Param("fixInfo") FixRecordInfo fixRecordInfo);

    Integer updateStackFixRecord(@Param("table")String table, @Param("fixInfo") StackReplaceInfo stackReplaceInfo);

    List<CommonStatistics> getFixStatistics(@Param("table") String table, @Param("start") String start, @Param("end") String end);

    List<CommonStatistics> getStackReplaceStatistics(@Param("table") String table, @Param("start") String start, @Param("end") String end);

}
