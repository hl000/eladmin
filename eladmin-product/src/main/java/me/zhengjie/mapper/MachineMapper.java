package me.zhengjie.mapper;




import me.zhengjie.domain.ExpStackInfo;
import me.zhengjie.domain.MachineOriginAction;
import me.zhengjie.statistics.CommonStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MachineMapper {


    List<MachineOriginAction> getMachineOrigin(@Param("table") String table, @Param("dayId") long dayId   , @Param("base") String base);
}
